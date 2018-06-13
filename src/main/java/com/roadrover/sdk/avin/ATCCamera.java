package com.roadrover.sdk.avin;

import android.content.Context;
import android.graphics.ImageFormat;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.roadrover.sdk.system.IVIConfig;
import com.roadrover.sdk.utils.FieldUtil;
import com.roadrover.sdk.utils.Logcat;
import com.roadrover.sdk.utils.TimerUtil;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ATC平台，封装 InputSourceClient，接口和标准Camera统一
 */

public class ATCCamera {

    private Object mInputSourceClientV = null; // atc AVIN对象
    private SurfaceHolder mSurfaceHolder = null; // 最终显示到前台的控件
    private int mCameraId = -1;

    /** 有信号 */
    private static final int SIGNAL_READY  = 0;
    /** 信号丢失 */
    private static final int SIGNAL_LOST   = 1;
    /** 信号改变 */
    private static final int SIGNAL_CHANGE = 2;

    /** AV状态 */
    private static final int STATUS_NONE    = 0;
    /** AV状态，停止 */
    private static final int STATUS_STOPED  = 1;
    /** AV状态，开始 */
    private static final int STATUS_STARTED = 2;

    /** 类型 */
    private static final int DEST_TYPE_NONE       = 0;
    /** 显示到前台 */
    private static final int DEST_TYPE_FRONT      = 1;
    /** 显示到后台 */
    private static final int DEST_TYPE_REAR       = 2;
    /** 前后台同时显示 */
    private static final int DEST_TYPE_FRONT_REAR = 3;

    /** 失败 */
    private static final int ERR_FAILED = -6004;
    /** 成功 */
    private static final int ERR_OK     = -6005;

    /** 端口定义 */
    public static final int PORT_NONE = 0;

    /** 源定义 */
    public static final int SOURCE_TYPE_NONE      = 0;
    /** AVIN */
    public static final int SOURCE_TYPE_AVIN      = 1;
    /** 数字输入 */
    public static final int SOURCE_TYPE_DIGITALIN = 2;
    /** HDMI高清输入 */
    public static final int SOURCE_TYPE_HDMI      = 3;

    private int mWidth;
    private int mHeight;

    private int mCurrentVideoStatus = STATUS_NONE; // 当前视频状态

    private IVIAVIn.AVInListener mAVInCallback = null;
    /** inputSourceClient回调 */
    private InputSourceClientCallback mInputSourceClientCallback = new InputSourceClientCallback();

    /** InputSourceClient类名 */
    private static final String INPUT_SOURCE_CLIENT_CLASS = "com.autochips.inputsource.InputSourceClient";
    /** onSignalListener回调类名 */
    private static final String ON_SIGNAL_LISTENER_CLASS = INPUT_SOURCE_CLIENT_CLASS + "$OnSignalListener";
    /** AVIN类名 */
    private static final String AVIN_CLASS = "com.autochips.inputsource.AVIN";

    /** 内容提供者 */
    private Context mContext;

    /** 视频是否有信号 */
    private int mSignal = IVIAVIn.Signal.HAS_SIGNAL;
    /** 视频信号检测定时器 */
    private TimerUtil mVideoSignalTimer = null;
    /** 检测时间3s */
    private static final int VIDEO_SIGNAL_DETECTION_MS = 3 * 1000;

    /** ATC摄像头调节的对象 */
    private ATCCameraVideoSetting mATCCameraVideoSetting = new ATCCameraVideoSetting();

    /**
     * Creates a new Camera object to access a particular hardware camera. If
     * the same camera is opened by other applications, this will throw a
     * RuntimeException.
     */
    public static ATCCamera open(int cameraId, Context context) {
        return new ATCCamera(cameraId, context);
    }

    /**
     * 检测是否是ATC平台
     * @return
     */
    public static boolean detectionIsATCPlatform() {
        try {
            Class.forName(AVIN_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return false;
    }

    /** used by Camera#open, Camera#open(int) */
    ATCCamera(int cameraId, Context context) {
        mCameraId = cameraId;
        if (context != null) {
            mContext = context.getApplicationContext();
        }
    }

    /**
     * Starts capturing and drawing preview frames to the screen.
     * Preview will not actually start until a surface is supplied
     */
    public void startPreview() {
        if (mInputSourceClientV == null) {
            try {
                mInputSourceClientV = FieldUtil.createObject(AVIN_CLASS);
                setOnSignalListener(mInputSourceClientCallback);
                setDestination(DEST_TYPE_FRONT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logcat.d("mCameraId:" + mCameraId);
        int retValueVideo = setSource(SOURCE_TYPE_AVIN, mCameraId, PORT_NONE, 0);
        if (ERR_FAILED == retValueVideo) {
            Logcat.e("setVideoSource ERR_FAILED!!!");
            stopPreview();
            return;
        }

        Logcat.d("retValueVideo:" + retValueVideo);
        startVideoSignalDetectionTimer();
        if (mCurrentVideoStatus == STATUS_STOPED || mCurrentVideoStatus == STATUS_NONE) {
            mCurrentVideoStatus = STATUS_STARTED;
            play();
        }
    }

    /**
     * Stops capturing and drawing preview frames to the surface, and
     * resets the camera for a future call to {@link #startPreview()}.
     */
    public final void stopPreview() {
        Logcat.d();
        stopVideoSignalDetectionTimer();
        if (mInputSourceClientV != null) {
            mCurrentVideoStatus = STATUS_STOPED;
            stop();
        }
    }

    /**
     * Disconnects and releases the Camera object resources.
     *
     * <p>You must call this as soon as you're done with the Camera object.</p>
     */
    public final void release() {
        stopPreview();

        if (mInputSourceClientV != null) {
            mCurrentVideoStatus = STATUS_NONE;
            stop();
            invoke("release"); // 执行实际的释放动作
            mInputSourceClientV = null;
        }

        Logcat.d("mSurfaceHolder:" + mSurfaceHolder);
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(mSurfaceHolderCallback);
            mSurfaceHolder = null;
        }
    }

    /**
     * Sets the {@link Surface} to be used for live preview.
     * Either a surface or surface texture is necessary for preview, and
     * preview is necessary to take pictures.  The same surface can be re-set
     * without harm.  Setting a preview surface will un-set any preview surface
     * texture that was set via .
     */
    public final void setPreviewDisplay(SurfaceHolder holder) throws IOException {
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(mSurfaceHolderCallback);
        }
        mSurfaceHolder = holder;
        if (mSurfaceHolder != null) {
            mSurfaceHolder.addCallback(mSurfaceHolderCallback);
            mSurfaceHolder.setFormat(ImageFormat.YV12);
        }
        Logcat.d("mSurfaceHolder:" + mSurfaceHolder);
    }

    /**
     * 监听surface创建回调
     */
    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logcat.d();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Logcat.d("width:" + width + " height:" + height + " mWidth:" + mWidth + " mHeight:" + mHeight +
                    " RectLeft : " + IVIConfig.getCameraRectLeft() +
                    " RectTop : " + IVIConfig.getCameraRectTop());
            if (mSurfaceHolder != null && mInputSourceClientV != null && width != 0 && height != 0) {
                setAVINSignalType(width, height);
                if (mSurfaceHolder.getSurface() != null) {
                    try {
                        setDisplay(mSurfaceHolder);
                        setSourceRect(DEST_TYPE_FRONT, IVIConfig.getCameraRectLeft(), IVIConfig.getCameraRectTop(), width, height);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                setDisplay(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void setAVINSignalType(int width, int height) {
        Logcat.d("width:" + width + " height:" + height);
        mWidth  = width;
        mHeight = height;
        if (mSurfaceHolder != null) {
            mSurfaceHolder.setFixedSize(width, height);
        }
    }

    /**
     * 设置视频监听回调
     * @param listener
     */
    public void setAVInCallback(IVIAVIn.AVInListener listener) {
        mAVInCallback = listener;
    }

    /**
     * 获取视频有无信号
     * @return
     */
    public int getVideoSignal() {
        return mSignal;
    }

    /**
     * 设置信号监听回调
     * @param listener
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void setOnSignalListener(Object listener)
            throws Exception {
        if (mInputSourceClientV == null || listener == null) {
            Logcat.d("mInputSourceClientV is " + mInputSourceClientV + ", setOnSignalListener is " + listener);
            return;
        }
        // 使用代理的方式设置回调
        final Class<?> callbackClass = Class.forName(ON_SIGNAL_LISTENER_CLASS);
        Object mObj = Proxy.newProxyInstance(ATCCamera.class.getClassLoader(), new Class[] { callbackClass }, (InputSourceClientCallback) listener);
        Method method = Class.forName(INPUT_SOURCE_CLIENT_CLASS)
                .getMethod("setOnSignalListener", new Class[]{callbackClass});
        method.invoke(mInputSourceClientV, new Object[] {mObj});
    }

    /**
     * 设置目标方向
     * @param destination DEST_TYPE_NONE, DEST_TYPE_FRONT
     * @retur
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private int setDestination(int destination)
            throws Exception {
        return (int) FieldUtil.invoke(mInputSourceClientV, "setDestination", destination);
    }

    /**
     * 播放
     * @return
     */
    private void play() {
        invoke("play");
    }

    /**
     * 停止播放
     * @return
     */
    private void stop() {
        invoke("stop");
    }

    /**
     * 执行一次无参数反射操作
     * @param methodName 方法名
     * @return
     */
    private void invoke(String methodName) {
        FieldUtil.invoke(mInputSourceClientV, methodName);
    }

    /**
     * 设置显示器
     * @param sh
     */
    private void setDisplay(SurfaceHolder sh) throws Exception {
        if (mInputSourceClientV == null) {
            Logcat.d("mInputSourceClientV is null!");
            return;
        }
        Logcat.d("sh:" + sh.getClass().getName());
        FieldUtil.invoke(mInputSourceClientV, "setDisplay",
                new Class<?>[]{SurfaceHolder.class}, new Object[]{sh});
    }

    /**
     * 设置播放源的范围
     * @param destination
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     */
    private int setSourceRect(int destination, int left, int top, int width, int height)
            throws Exception {
        return (int) FieldUtil.invoke(mInputSourceClientV, "setSourceRect", destination, left, top, width, height);
    }

    /**
     * 设置源
     * @param inputSrcType
     * @param portV
     * @param portA
     * @param priority
     * @return
     */
    private int setSource(int inputSrcType, int portV, int portA, int priority) {
        return (int) FieldUtil.invoke(mInputSourceClientV, "setSource", inputSrcType, portV, portA, priority);
    }

    /**
     * 通过代理的方式，反射 InputSourceClientCallback
     */
    public class InputSourceClientCallback implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method == null || args == null || args.length < 2) {
                Logcat.d("method is " + method + ", args is " + args);
                return null;
            }
            Logcat.d("method:" + method.getName());
            if (TextUtils.equals(method.getName(), "onSignal")) {
                stopVideoSignalDetectionTimer(); // 停止检测有无信号定时器

                int msg    = (int) args[0];
                int param1 = (int) args[1];
                int param2 = (int) args[2];
                switch (msg) {
                    case SIGNAL_READY:
                        Logcat.d("Get SIGNAL_READY");
                        mSignal = IVIAVIn.Signal.HAS_SIGNAL;
                        setAVINSignalType(param1, param2);
                        if (mAVInCallback != null) {
                            mAVInCallback.onVideoSignalChanged(IVIAVIn.Id.AV, IVIAVIn.Signal.HAS_SIGNAL);
                        }
                        mSurfaceHolderCallback.surfaceChanged(mSurfaceHolder, 0, mWidth, mHeight);
                        break;

                    case SIGNAL_LOST:
                        mSignal = IVIAVIn.Signal.NO_SIGNAL;
                        Logcat.d("Get SIGNAL_LOST");
                        if (mAVInCallback != null) {
                            mAVInCallback.onVideoSignalChanged(IVIAVIn.Id.AV, IVIAVIn.Signal.NO_SIGNAL);
                        }
                        break;

                    case SIGNAL_CHANGE:
                        mSignal = IVIAVIn.Signal.UNSTABLE_SIGNAL;
                        Logcat.d("Get SIGNAL_CHANGE, new resolution: " + param1 + "x" + param2);
                        setAVINSignalType(param1, param2);
                        if (mAVInCallback != null) {
                            mAVInCallback.onVideoSignalChanged(IVIAVIn.Id.AV, IVIAVIn.Signal.UNSTABLE_SIGNAL);
                        }
                        break;

                    default:
                        break;
                }
            }
            return null;
        }
    }

    /**
     * 设置video视频参数
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @param value 值
     */
    public void setParam(int id, int value) {
        mATCCameraVideoSetting.setParam(mCameraId, id, value);
    }

    /**
     * 获取video视频参数
     * @param id 视频ID，通过{@link VideoParam#makeId(int, int)}获得
     * @return
     */
    public int getParamValue(int id) {
        return mATCCameraVideoSetting.getParamValue(mCameraId, id);
    }

    /**
     * 开启视频有无信号检测定时器，如果时间到，则认为无信号
     */
    private void startVideoSignalDetectionTimer() {
        if (mVideoSignalTimer == null) {
            mVideoSignalTimer = new TimerUtil(new TimerUtil.TimerCallback() {
                @Override
                public void timeout() {
                    if (mVideoSignalTimer != null) {
                        mVideoSignalTimer.stop();
                    }

                    if (mAVInCallback != null) {
                        mAVInCallback.onVideoSignalChanged(IVIAVIn.Id.AV, IVIAVIn.Signal.NO_SIGNAL);
                    }
                }
            });
        }
        mVideoSignalTimer.start(VIDEO_SIGNAL_DETECTION_MS);
    }

    /**
     * 停止检测定时器
     */
    private void stopVideoSignalDetectionTimer() {
        if (mVideoSignalTimer != null) {
            mVideoSignalTimer.stop();
            mVideoSignalTimer = null;
        }
    }
}
