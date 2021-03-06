package com.roadrover.sdk.avin;

import android.content.Context;
import android.hardware.Camera;
import android.os.IBinder;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.sdk.system.IVIConfig;
import com.roadrover.sdk.utils.Logcat;
import com.roadrover.services.avin.IAVIn;
import com.roadrover.services.avin.IAVInCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * AVIn管理类
 */
public class AVInManager extends BaseManager {
    /**
     * {@link IAVIn}接口对象
     */
    private IAVIn mAVInInterface;
    /**{@link com.roadrover.sdk.avin.IVIAVIn.AVInListener} 监听对象*/
    private IVIAVIn.AVInListener mAvInListener;
    /**{@link com.roadrover.sdk.avin.IVIAVIn.Id}*/
    private int mAvId = IVIAVIn.Id.NONE;
    /**媒体是否打开*/
    private boolean mMediaIsOpen = false;
    /**{@link com.roadrover.sdk.avin.IVIAVIn.Id}，记录倒车时，在services还未连上上时，调用setAndroidCameraOpenPrepared*/
    private int mPreparedAvId = IVIAVIn.Id.NONE;

    /** 记录打开的摄像头 */
    private CameraUtil mCameraUtil;

    /**请求视频信号EventBus事件类*/
    private static class EventRequestVideoSignal {
        /**{@link com.roadrover.sdk.avin.IVIAVIn.Id}*/
        public int mAvId;

        public EventRequestVideoSignal(int avId) {
            mAvId = avId;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestVideoSignal(EventRequestVideoSignal event) {
        if (mAVInInterface == null) {
            Logcat.d("service is not connected");
            return;
        }

        int signal = getVideoSignal(event.mAvId);
        Logcat.d(IVIAVIn.Id.getName(event.mAvId) + " signal is " + IVIAVIn.Signal.getName(signal));
        post(new IVIAVIn.EventVideoSignalChanged(event.mAvId, signal));
    }

    /**
     * 构造函数
     * @param context           上下文
     * @param connectListener   连接监听对象，见{@link com.roadrover.sdk.BaseManager.ConnectListener}
     * @param avInListener      AVIN监听对象，见{@link com.roadrover.sdk.avin.IVIAVIn.AVInListener}
     */
    public AVInManager(Context context, ConnectListener connectListener, IVIAVIn.AVInListener avInListener) {
        this(context, connectListener, avInListener, true);
    }

    /**
     * 构造函数
     * @param context               上下文
     * @param connectListener       连接监听对象，见{@link com.roadrover.sdk.BaseManager.ConnectListener}
     * @param avInListener          AVIN监听对象，见{@link com.roadrover.sdk.avin.IVIAVIn.AVInListener}
     * @param useDefaultEventBus    是否默认使用EventBus
     */
    public AVInManager(Context context, ConnectListener connectListener, IVIAVIn.AVInListener avInListener, boolean useDefaultEventBus) {
        super(context, connectListener, useDefaultEventBus);
        mAvInListener = avInListener;
    }

    @Override
    public void disconnect() {
        if (mAVInInterface != null) {
            try {
                mAVInInterface.unRegisterCallback(mAVInCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        mAVInInterface = null;
        mAvInListener = null;
        mAVInCallback = null;

        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.AVIN_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mAVInInterface = IAVIn.Stub.asInterface(service);
        registerCallback();

        // 对是媒体类型的AVIN，重新打开它
        if (mMediaIsOpen) {
            open(mAvId);
        }
        if (mPreparedAvId != IVIAVIn.Id.NONE) {
            setAndroidCameraOpenPrepared(mPreparedAvId);
            mPreparedAvId = IVIAVIn.Id.NONE;
        }
    }

    @Override
    protected void onServiceDisconnected() {
        mAVInInterface = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoSignalChanged(IVIAVIn.EventVideoSignalChanged event) {
        if (mAvInListener != null) {
            mAvInListener.onVideoSignalChanged(event.mAvId, event.mSignal);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCvbsTypeChanged(IVIAVIn.EventCvbsTypeChanged event) {
        if (mAvInListener != null) {
            mAvInListener.onCvbsTypeChanged(event.mAvId, event.mCvbsType);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControlEvent(IVIAVIn.EventControl event) {
        if (mAvInListener != null) {
            switch (event.mAction) {
                case IVIAVIn.EventControl.Action.QUIT_APP:
                    mAvInListener.quitApp();
                    break;

                case IVIAVIn.EventControl.Action.STOP:
                    mAvInListener.stop();
                    break;

                case IVIAVIn.EventControl.Action.RESUME:
                    mAvInListener.resume();
                    break;

                case IVIAVIn.EventControl.Action.SELECT:
                    mAvInListener.select(event.mValue);
                    break;

                case IVIAVIn.EventControl.Action.NEXT:
                    mAvInListener.next();
                    break;

                case IVIAVIn.EventControl.Action.PREV:
                    mAvInListener.prev();
                    break;

                case IVIAVIn.EventControl.Action.VIDEO_PERMIT:
                    mAvInListener.onVideoPermitChanged(event.mValue == 1);
                    break;

                default:
                    Logcat.d("Unknown action " + event.mAction);
                    break;
            }
        }
    }

    /**
     * 打开AVIN
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public boolean open(int avId) {
        boolean isRet = false;
        mAvId = avId;
        Logcat.d(IVIAVIn.Id.getName(avId));
        mMediaIsOpen = IVIAVIn.Id.isMedia(avId);
        if (mAVInInterface != null) {
            try {
                mAVInInterface.open(avId, mAVInCallback, mContext.getPackageName());
                isRet = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        super.registerCallback(mAVInCallback);
        return isRet;
    }

    /**
     * AVIN回调对象
     */
    private IAVInCallback mAVInCallback = new IAVInCallback.Stub() {
        @Override
        public void onVideoSignalChanged(int avId, int signal) {
            post(new IVIAVIn.EventVideoSignalChanged(avId, signal));
        }

        @Override
        public void onVideoPermitChanged(boolean show) {
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.VIDEO_PERMIT, show ? 1 : 0));
        }

        @Override
        public void stop() throws RemoteException {
            mMediaIsOpen = false;
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.STOP));
        }

        @Override
        public void resume() throws RemoteException {
            mMediaIsOpen = true;
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.RESUME));
        }

        @Override
        public void quitApp() {
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.QUIT_APP));
        }

        @Override
        public void next() {
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.NEXT));
        }

        @Override
        public void prev() {
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.PREV));
        }

        @Override
        public void select(int index) {
            post(new IVIAVIn.EventControl(IVIAVIn.EventControl.Action.SELECT, index));
        }

        @Override
        public void onCvbsTypeChanged(int avId, int cvbsType) {
            post(new IVIAVIn.EventCvbsTypeChanged(avId, cvbsType));
        }

        @Override
        public void onSourcePluginChanged(int avId, boolean plugin) {
            Logcat.d(IVIAVIn.Id.getName(avId) + " plug in changed to " + plugin);
            post(new IVIAVIn.EventSourcePluginChanged(avId, plugin));
        }
    };

    /**
     * 关闭AVIN
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public void close(int avId) {
        mAvId = IVIAVIn.Id.NONE;
        mMediaIsOpen = false;

        if (mAVInInterface != null) {
            try {
                mAVInInterface.close(avId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 检查AVIN媒体是否打开
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public boolean isOpen(int avId) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.isOpen(avId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return false;
    }

    /**
     * 判断指定视频参数是否有效
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public boolean isParamAvailable(int id) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.isParamAvailable(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return false;
    }

    /**
     * 获取视频参数最小值
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public int getParamMinValue(int id) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.getParamMinValue(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return 0;
    }

    /**
     * 获取视频参数最大值
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public int getParamMaxValue(int id) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.getParamMaxValue(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return 0;
    }

    /**
     * 获取视频参数默认值
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public int getParamDefaultValue(int id) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.getParamDefaultValue(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return 0;
    }

    /**
     * 获取视频参数当前值
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public int getParamValue(int id) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.getParam(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return 0;
    }

    /**
     * 设置视频参数，必须在Camear.open之后才能使用
     * @param id    视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @param value 参数值
     */
    public void setParam(int id, int value) {
        if (mAVInInterface != null) {
            try { // 参数数据保存在服务
                mAVInInterface.setParam(id, value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (mCameraUtil != null && mCameraUtil.isATCCamera()) { // atc 摄像头，在应用内设置参数
                mCameraUtil.setATCCameraParam(id, value);
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 设置视频参数
     * @param param    视频参数，见{@link VideoParam}
     */
    public void setParam(VideoParam param) {
        if (param == null) {
            return;
        }
        setParam(param.mId, param.mValue);
    }

    /**
     * 设置默认视频参数
     * @param param    视频参数，见{@link VideoParam}
     */
    public void setDefaultParam(VideoParam param) {
        if (param == null) {
            return;
        }

        setParam(param.mId, param.mDefault);
    }

    /**
     * 获取CVBS类型
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return {@link com.roadrover.sdk.avin.VideoParam.CvbsType}
     */
    public int getCvbsType(int avId) {
        return getParamValue(VideoParam.makeId(avId, VideoParam.SubId.CVBS_TYPE));
    }

    /**
     * 通过视频ID获取其参数
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public VideoParam getParam(int id) {
        if (mAVInInterface != null) {
            try {
                if (!mAVInInterface.isParamAvailable(id)) {
                    return null;
                }

                int minValue = mAVInInterface.getParamMinValue(id);
                int maxValue = mAVInInterface.getParamMaxValue(id);
                int defaultValue = mAVInInterface.getParamDefaultValue(id);
                int value = mAVInInterface.getParam(id);

                VideoParam param = new VideoParam(id, minValue, maxValue, defaultValue);
                param.mValue = value;
                return param;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return null;
    }

    /**
     * 根据AVIN ID获取其安卓系统摄像头下表
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return
     */
    public static int getAndroidCameraIndex(int avId) {
        return IVIConfig.getCameraIndex(avId);
    }

    /**
     * 获取AV的视频信号有无，注意：必须在打开摄像头后Camera.open(index)后才能调用，否则返回的结果不对
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return true 有信号，false 无信号
     */
    public boolean hasVideoSignal(int avId) {
        return IVIAVIn.Signal.hasSignal(getVideoSignal(avId));
    }

    /**
     * 获取AV的视频信号状态
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return {@link com.roadrover.sdk.avin.IVIAVIn.Signal}
     */
    public int getVideoSignal(int avId) {
        if (mCameraUtil != null && mCameraUtil.isATCCamera()) {
            return mCameraUtil.getVideoSignal();
        } else {
            if (mAVInInterface != null) {
                try {
                    return mAVInInterface.getVideoSignal(avId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Logcat.d("Service not connected");
            }
        }

        return IVIAVIn.Signal.UNSTABLE_SIGNAL;
    }

    /**
     * 请求AVIN发送是否有信号的消息，最终通过EventVideoSignalChanged来回传给应用
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public void requestVideoSignalEvent(int avId) {
        Logcat.d(IVIAVIn.Id.getName(avId));
        post(new EventRequestVideoSignal(avId));
    }

    /**
     * 视频是否允许观看
     * @return 允许返回true，否则返回false
     */
    public boolean isVideoPermit() {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.isVideoPermit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return true;
    }

    /**
     * 初始化Camera参数
     * @param cameraParams
     * @param avId
     * @return
     */
    private Camera.Parameters getCameraParam(Camera.Parameters cameraParams, int avId) {
        if (cameraParams != null) {
            List<Camera.Size> supportedSizes = cameraParams.getSupportedPreviewSizes();
            final int cvbsType = getCvbsType(avId);
            int width = VideoParam.CvbsType.getVideoWidth(cvbsType);
            int height = VideoParam.CvbsType.getVideoHeight(cvbsType);

            boolean found = false;
            for (Camera.Size size : supportedSizes) {
                final int _width = size.width;
                final int _height = size.height;
                Logcat.d(IVIAVIn.Id.getName(avId) + " camera available size (" + _width + "x" + _height + ")");
                if (width == _width && height == _height) {
                    found = true;
                }
            }

            if (!found) {
                Logcat.d(IVIAVIn.Id.getName(avId) + " CVBS type size not support, select the first available size.");
                final int size = supportedSizes.size();
                if (size > 0) {
                    Camera.Size cameraSize = supportedSizes.get(0);
                    if (cameraSize != null) {
                        width = cameraSize.width;
                        height = cameraSize.height;
                    }
                }
            }

            Logcat.d(IVIAVIn.Id.getName(avId) + " set CVBS type to " + VideoParam.CvbsType.getName(cvbsType) +
                    ", size (" + width + ", " + height + ").");
            cameraParams.setPreviewSize(width, height);
        }
        return cameraParams;
    }

    /**
     * <b>打开指定AVIN的安卓摄像头（前提：在INDEX相同的安卓摄像头关闭的情况下）</br>
     * <b>step1.设置AVIN视频通道（如果有）</br>
     * <b>step2.打开摄像头</br>
     * <b>step3.视频参数同步、开关切换</br>
     *
     * <b>开始安卓摄像头预览（在AVIN视频有信号的前提下）</br>
     * <b>step1.获取视频制式，NTSC或PAL</br>
     * <b>step2.根据视频制式，获取支持的分辨率，设置视频预览尺寸</br>
     * <b>step3.打开预览图像</br>
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return 打开成功，返回Camera对象，否则返回 null
     */
    public Camera openCamera(int avId) {
        int index = getAndroidCameraIndex(avId);
        Logcat.d("open camera " + IVIAVIn.Id.getName(avId) + ", camera index = " + index);

        setAndroidCameraOpenPrepared(avId);

        Camera camera = null;
        try {
            camera = Camera.open(index);
            if (camera != null) {
                // 设置预览视频尺寸
                try {
                    Camera.Parameters cameraParams = camera.getParameters();
                    if (cameraParams != null) {
                        camera.setParameters(getCameraParam(cameraParams, avId));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 通知Service Android Camera打开了
                setAndroidCameraOpen(avId, true);

                // 强制请求信号有无检测
                requestVideoSignalEvent(avId);
            } else {
                Logcat.e("open camera " + index + " failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (camera != null) {
                camera.release();
            }
        }

        return camera;
    }

    /**
     * <b>打开指定AVIN的安卓摄像头（前提：在INDEX相同的安卓摄像头关闭的情况下）</br>
     * <b>step1.设置AVIN视频通道（如果有）</br>
     * <b>step2.打开摄像头</br>
     * <b>step3.视频参数同步、开关切换</br>
     *
     * <b>开始安卓摄像头预览（在AVIN视频有信号的前提下）</br>
     * <b>step1.获取视频制式，NTSC或PAL</br>
     * <b>step2.根据视频制式，获取支持的分辨率，设置视频预览尺寸</br>
     * <b>step3.打开预览图像</br>
     *
     * <b>有些平台不能通过Camera方式打开，使用该接口</b>
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return 打开成功，返回Camera对象，否则返回 null
     */
    public CameraUtil openAndroidCamera(int avId) {
        int index = getAndroidCameraIndex(avId);
        Logcat.d("open camera " + IVIAVIn.Id.getName(avId) + ", camera index = " + index);

        setAndroidCameraOpenPrepared(avId);

        try {
            mCameraUtil = CameraUtil.open(index, mContext);
            if (mCameraUtil != null) {
                // 部分平台需要先刷一遍参数
                mCameraUtil.initVideoParam(avId,
                        getParam(VideoParam.makeId(avId, VideoParam.SubId.BRIGHTNESS)),
                        getParam(VideoParam.makeId(avId, VideoParam.SubId.CONTRAST)),
                        getParam(VideoParam.makeId(avId, VideoParam.SubId.SATURATION)));
                mCameraUtil.setAVInCallback(mAvInListener);
                // 设置预览视频尺寸
                try {
                    Camera.Parameters cameraParams = mCameraUtil.getParameters();
                    if (cameraParams != null) {
                        mCameraUtil.setParameters(getCameraParam(cameraParams, avId));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 通知Service Android Camera打开了
                setAndroidCameraOpen(avId, true);

                // 强制请求信号有无检测
                requestVideoSignalEvent(avId);
            } else {
                Logcat.e("open camera " + index + " failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (mCameraUtil != null) {
                mCameraUtil.release();
            }
        }

        return mCameraUtil;
    }

    /**
     * 通知service avId对应的Android Camera已经被关闭，
     * 如果已经调用了AVInManager.close，可以不用调用该函数
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public void closeAndroidCamera(int avId) {
        setAndroidCameraOpen(avId, false);
    }

    /**
     * 通知Service，应用已经打开或者关闭了Android的摄像头
     *
     * @param avId   AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @param isOpen true 打开状态，false 关闭状态
     */
    public void setAndroidCameraOpen(int avId, boolean isOpen) {
        if (!isOpen) {
            if (mCameraUtil != null) {
                mCameraUtil = null;
            }
        }
        if (mAVInInterface != null) {
            try {
                mAVInInterface.setAndroidCameraOpen(avId, isOpen);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 获取AVIN源的插入状态，目前只支持导游麦克风插入状态
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return
     */
    public boolean getSourcePlugin(int avId) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.getSourcePlugin(avId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return false;
    }

    /**
     * 请求AVIN源插入状态的消息
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public void requestSourcePluginEvent(int avId) {
        if (mAVInInterface != null) {
            post(new IVIAVIn.EventSourcePluginChanged(avId, getSourcePlugin(avId)));
        }
    }

    /**
     * 临时开始播放avId的声音，不作为媒体
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @param zone IVIMedia Zone，见{@link com.roadrover.sdk.media.IVIMedia.Zone}
     * @param mix 是否混音，
     *   true：混音，媒体降低音量，类似导航提示音
     *   false：不混音，暂停当前媒体，类似电话和声控提示音
     */
    public void startTempSound(int avId, int zone, boolean mix) {
        if (mAVInInterface != null) {
            try {
                mAVInInterface.startTempSound(avId, zone, mix);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 结束临时播放的avId声音
     *
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    public void endTempSound(int avId) {
        if (mAVInInterface != null) {
            try {
                mAVInInterface.endTempSound(avId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 控制TV
     *
     * @param control 控制码，见{@link com.roadrover.sdk.avin.IVITV.Control}
     */
    public boolean controlTV(int control) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.controlTV(control);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return false;
    }

    /**
     * 发送触摸坐标，范围{255， 255}
     * @param x X坐标
     * @param y Y坐标
     * @return
     */
    public boolean sendTouch(int x, int y) {
        if (mAVInInterface != null) {
            try {
                return mAVInInterface.sendTouch(x, y);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return false;
    }

    /**
     * 注册回调
     */
    private void registerCallback() {
        if (mAVInInterface != null) {
            try {
                mAVInInterface.registerCallback(mAVInCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 设置安卓系统摄像头准备打开
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     */
    private void setAndroidCameraOpenPrepared(int avId) {
        if (mAVInInterface != null) {
            try {
                mAVInInterface.setAndroidCameraOpenPrepared(avId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mPreparedAvId = avId;
            Logcat.d("Service not connected");
        }
    }
}
