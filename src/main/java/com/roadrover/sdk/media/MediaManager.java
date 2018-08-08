package com.roadrover.sdk.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.roadrover.sdk.BaseManager;
import com.roadrover.sdk.utils.LogNameUtil;
import com.roadrover.sdk.utils.Logcat;
import com.roadrover.services.media.IGetMediaListCallback;
import com.roadrover.services.media.IMedia;
import com.roadrover.services.media.IMediaControlCallback;
import com.roadrover.services.media.IMediaInfoCallback;
import com.roadrover.services.media.IMediaScannerCallback;
import com.roadrover.services.media.IMusicControlCallback;
import com.roadrover.services.media.StMusic;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 媒体管理类，为多媒体应用提供工具对象
 *
 * 1. 车机开机，媒体扫描回调流程 <br/>
 *    1) onScanStart(MediaScannerType.SCAN_ALL_TYPE) <br/>
 *    2) onScanFinish(MediaScannerType.SCAN_ALL_TYPE) <br/>
 *    <b>建议在这种情况下，媒体app需要MediaSqlManager.queryAudioFile()获取所有的媒体列表</b></br><br/>
 * 2. 插入盘符：<br/>
 *    1) onScanStart(MediaScannerType.MOUNT_TYPE, MediaSqlDataType.DIR_TYPE)<br/>
 *    2) onMount("路径")<br/>
 *    3) onScanFinish(MediaScannerType.MOUNT_TYPE, MediaSqlDataType.DIR_TYPE, "路径")<br/>
 *    4) MediaSqlManager.queryAudioFile() 获取所有AudioFile列表，无id3信息<br/>
 *    5) 如果要获取id3信息，则可以在这里调用 getAllMediaList(MediaSqlDataType.AUDIO_TYPE)，通过回调获取 id3信息<br/>
 *    <b>注：除Audio类型外，其他类型不支持id3信息获取</b></br><br/>
 * 3. 卸载盘符：</br>
 *    1) onScanStart(MediaScannerType.EJECT_TYPE, MediaSqlDataType.DIR_TYPE)</br>
 *    2) onEject("路径")，<b>如果当前播放的媒体就是卸载的盘符，建议在该位置停止媒体播放，并且release掉播放器，否则很容易被系统把APP给强制kill掉</b></br>
 *    3) onScanFinish(MediaScannerType.EJECT_TYPE, MediaSqlDataType.DIR_TYPE, "路径")</br><br/>
 * 4. 删除文件：</br>
 *    1) onScanStart(MediaScannerType.FILE_DELETE_TYPE, MediaSqlDataType)</br>
 *    2) onScanFinish(MediaScannerType.FILE_DELETE_TYPE, MediaSqlDataType, "路径")</br>
 *    <b>如果删除的文件正好是当前播放的文件，建议直接跳下一曲</b></br><br/>
 * 5. 创建文件，拷贝文件：</br>
 *    1) onScanStart(MediaScannerType.FILE_CREATE_TYPE, MediaSqlDataType)</br>
 *    2) onScanFinish(MediaScannerType.FILE_CREATE_TYPE, MediaSqlDataType, "路径")</br><br/>
 * 6. 剪切文件，同盘符拷贝：</br>
 *    1) onScanStart(MediaScannerType.FILE_RENAME_TYPE, MediaSqlDataType)</br>
 *    2) onScanFinish(MediaScannerType.FILE_RENAME_TYPE, MediaSqlDataType, "新的路径", "老的路径")</br>
 *    不同盘符拷贝：</br>
 *    会走创建新文件，删除老文件的流程</br>
 *
 * 媒体播放控制流程参考 {@link com.roadrover.sdk.media.IVIMedia.MediaControlListener} 定义
 */
public class MediaManager extends BaseManager {

    private IMedia mMediaInterface;
    private IVIMedia.MediaControlListener mMediaControlListener;
    private Set<IMediaInfoCallback.Stub> mMediaInfoListeners = new HashSet<>();
    private Set<MediaScannerCallback> mMediaScannerListeners = new HashSet<>();
    private int mMediaType = IVIMedia.Type.NONE; // 记录 mediaType，如果服务挂掉了，重新open
    private int mMediaZone = IVIMedia.Zone.UNKNOWN;

    /**
     * 媒体扫描回调的接口类
     */
    public static class MediaScannerCallback {
        /**
         * 实际和Services通信的接口对象
         */
        public IMediaScannerCallback.Stub mCallback;

        /**
         * type 类型列表 {@link com.roadrover.sdk.media.IVIMedia.Type}
         */
        public List<Integer> mTypes;

        /**
         * 构造函数
         * @param types 类型列表 {@link com.roadrover.sdk.media.IVIMedia.Type}
         * @param callback 和服务通信的接口对象
         */
        public MediaScannerCallback(int[] types, IMediaScannerCallback.Stub callback) {
            mTypes = new ArrayList<>();
            if (types != null) {
                for (int type : types) {
                    mTypes.add(type);
                }
            }
            mCallback = callback;
        }

        /**
         * 判断两个对象是否一样，判断标准，callback一样认为一样
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && o instanceof MediaScannerCallback) {
                MediaScannerCallback other = (MediaScannerCallback) o;
                return this.mCallback.equals(other.mCallback);
            }
            return false;
        }
    }

    /**
     * 扫描开始的Event类，用于做EventBus通信
     */
    public static class EventScanStart {
        public int mScanType;
        public int mSqlType;

        /**
         * 构造函数
         * @param scanType 媒体扫描类型，{@link com.roadrover.sdk.media.IVIMedia.MediaScannerType}
         * @param sqlType  媒体数据库类型，{@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
         */
        public EventScanStart(int scanType, int sqlType) {
            mScanType = scanType;
            mSqlType = sqlType;
        }

        public String toString() {
            return "scanType:" + IVIMedia.MediaScannerType.getName(mScanType) +
                    " sqlType:" + IVIMedia.MediaSqlDataType.getName(mSqlType);
        }
    }

    /**
     * 扫描结束的Event类，用于做EventBus通信
     */
    public static class EventScanFinished {
        public int mScanType;
        public int mSqlType;
        public String mPath;
        public String mOldPath;

        /**
         * 媒体扫描的构造函数
         * @param scanType 媒体扫描类型，{@link com.roadrover.sdk.media.IVIMedia.MediaScannerType}
         * @param sqlType  媒体数据库类型，{@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
         * @param path     当前路径
         * @param oldPath  老的路径，只有在拷贝文件的时候才存在
         */
        public EventScanFinished(int scanType, int sqlType, String path, String oldPath) {
            mScanType = scanType;
            mSqlType = sqlType;
            mPath = path;
            mOldPath = oldPath;
        }

        /**
         * 将整个对象打印成一个String
         * @return
         */
        public String toString() {
            return "mScanType:" + IVIMedia.MediaScannerType.getName(mScanType) +
                    " mSqlType:" + IVIMedia.MediaSqlDataType.getName(mSqlType) +
                    " mPath:" + mPath +
                    " mOldPath:" + mOldPath;
        }
    }

    /**
     * 盘符拔除的Event类，用于做EventBus通信
     */
    public static class EventEject {
        public String mPath;
        public boolean mIsDiskPowerDown;

        /**
         * 盘符拔除类的构造函数
         * @param path 当前拔除的路径
         * @param isDiskPowerDown 是否是电源关闭的时候拔除，如果是电源关闭的时候拔除的，APP应该不做处理
         */
        public EventEject(String path, boolean isDiskPowerDown) {
            mPath = path;
            mIsDiskPowerDown = isDiskPowerDown;
        }

        public String toString() {
            return LogNameUtil.toString(this);
        }
    }

    /**
     * 盘符插入的Event类，用于做EventBus通信
     */
    public static class EventMount {
        public String mPath;

        /**
         * 构造函数
         * @param path 插入的盘符路径
         */
        public EventMount(String path) {
            mPath = path;
        }

        public String toString() {
            return LogNameUtil.toString(this);
        }
    }

    /**
     * 媒体控制的Event类，用于做EventBus通信
     */
    public static class EventMediaControl {
        /**
         * 播放控制，一般方控播放或者声控发送播放指令，值为{@value}
         */
        public static final int PLAY = 1;

        /**
         * 暂停控制，一般方控暂停或者声控发送暂停播放指令，值为{@value}
         */
        public static final int PAUSE = 2;

        /**
         * 停止控制，一般为ACC，值为{@value}
         */
        public static final int STOP = 3;

        /**
         * 下一曲，方控下一曲或者声控下一曲，值为{@value}
         */
        public static final int NEXT = 4;

        /**
         * 上一曲，方控上一曲或者声控上一曲，值为{@value}
         */
        public static final int PREV = 5;

        /**
         * 挂起媒体，在蓝牙电话开始、Power键锁屏、声控开启的时候调用，值为{@value}
         */
        public static final int SUSPEND = 6;

        /**
         * 恢复媒体，值为{@value} </br>
         * 蓝牙电话结束、Power键结束锁屏，ACC恢复的时候调用， </br>
         * 媒体恢复到suspend或者stop调用前的状态 </br>
         */
        public static final int RESUME = 7;

        /**
         * 选择第几个媒体，一般声控调用，值为{@value}
         */
        public static final int SELECT = 8;

        /**
         * 选择播放模式，一般声控调用，值为{@value}
         */
        public static final int SET_PLAY_MODE = 9;

        /**
         * 随便听听，一般声控调用，值为{@value}
         */
        public static final int PLAY_RANDOM = 10;

        /**
         * 设置收藏状态，一般声控调用，值为{@value}
         */
        public static final int SET_FAVOUR = 11;

        /**
         * 过滤媒体，通过歌曲名，或者专辑名等进行过滤，一般声控调用，值为{@value}
         */
        public static final int FILTER = 12;

        /**
         * 设置媒体音量，一般为导航发声音时，需要当前媒体降音处理时调用，值为{@value}
         */
        public static final int SET_VOLUME = 13;

        /**
         * 退出APP，一般为从SystemUI划掉当前的APP时调用，值为{@value}
         */
        public static final int QUIT_APP = 14;

        /**
         * 视频是否被允许显示，比如手刹控制，值为{@value}
         */
        public static final int VIDEO_PERMIT = 15;

        /**
         * 跳转进度，其他APP控制音乐，值为{@value}
         */
        public static final int SEEK_TO = 16;

        /**
         * 播放暂停，值为{@value}
         */
        public static final int PLAY_PAUSE = 17;

        public int mControl;
        public int mValue;
        public float mVolume;
        public String mTitle;
        public String mSinger;

        /**
         * 构造函数
         * @param control 控制类型，{@link EventMediaControl}
         */
        public EventMediaControl(int control) {
            mControl = control;
        }

        /**
         * 构造函数
         * @param control 控制类型，{@link EventMediaControl}
         * @param value 带int参数
         */
        public EventMediaControl(int control, int value) {
            mControl = control;
            mValue = value;
        }

        /**
         * 打印当前的控制动作的名字，一般用于log打印
         * @return 例：设置音量 返回 "SET_VOLUME"
         */
        public String getName() {
            return getName(mControl);
        }

        /**
         * 设置音量的静态构造方法
         * @param volume 需要设置的音量百分比，范围{0.0->1.0}
         * @return 返回 {@link EventMediaControl} 对象
         */
        public static EventMediaControl setVolume(float volume) {
            EventMediaControl event = new EventMediaControl(SET_VOLUME);
            event.mVolume = volume;
            return event;
        }

        /**
         * 过滤媒体的静态构造方法
         * @param title 标题
         * @param singer 歌手名
         * @return 返回 {@link EventMediaControl} 对象
         */
        public static EventMediaControl filter(String title, String singer) {
            EventMediaControl event = new EventMediaControl(FILTER);
            event.mTitle = title;
            event.mSinger = singer;
            return event;
        }

        /**
         * 返回指定control对应的名字，一般用于打印log
         * @param control {@link EventMediaControl}
         * @return 例：13 返回 "SET_VOLUME"
         */
        public static String getName(int control) {
            return LogNameUtil.getName(control, EventMediaControl.class);
        }
    }

    /**
     * 当前消息是否发送给应用，如果是文件创建，删除，重命名消息，则需要判断创建的文件类型是否是应用需要的，如果是，返回true，否则返回false
     * @param scanType 扫描类型
     * @param sqlType  对应的数据库类型
     * @param mediaScannerCallback
     * @return
     */
    private boolean isSendScannerInfoToApp(int scanType, int sqlType, MediaScannerCallback mediaScannerCallback) {
        if (mediaScannerCallback != null) {
            switch (scanType) {
                case IVIMedia.MediaScannerType.FILE_CREATE_TYPE:
                case IVIMedia.MediaScannerType.FILE_DELETE_TYPE:
                case IVIMedia.MediaScannerType.FILE_RENAME_TYPE:
                    if (!mediaScannerCallback.mTypes.contains(sqlType)) {
                        Logcat.d("type:" + sqlType + " app not need!");
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * 媒体扫描开始的EventBus接收类
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanStart(EventScanStart event) {
        if (event != null) {
            if (mMediaScannerListeners != null) {
                for (MediaScannerCallback mediaScannerCallback : mMediaScannerListeners) {
                    if (mediaScannerCallback != null && mediaScannerCallback.mCallback != null) {
                        try {
                            if (isSendScannerInfoToApp(event.mScanType, event.mSqlType, mediaScannerCallback)) {
                                mediaScannerCallback.mCallback.onScanStart(event.mScanType, event.mSqlType);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 媒体扫描完成的EventBus类
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanFinished(EventScanFinished event) {
        if (event != null) {
            if (mMediaScannerListeners != null) {
                for (MediaScannerCallback mediaScannerCallback : mMediaScannerListeners) {
                    if (mediaScannerCallback != null && mediaScannerCallback.mCallback != null) {
                        try {
                            if (isSendScannerInfoToApp(event.mScanType, event.mSqlType, mediaScannerCallback)) {
                                mediaScannerCallback.mCallback.onScanFinish(event.mScanType, event.mSqlType, event.mPath, event.mOldPath);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 盘符卸载的EventBus接收类
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEject(EventEject event) {
        if (event != null) {
            if (mMediaScannerListeners != null) {
                for (MediaScannerCallback mediaScannerCallback : mMediaScannerListeners) {
                    if (mediaScannerCallback != null && mediaScannerCallback.mCallback != null) {
                        try {
                            mediaScannerCallback.mCallback.onEject(event.mPath, event.mIsDiskPowerDown);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 盘符mount的EventBus接收类
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMount(EventMount event) {
        if (event != null) {
            if (mMediaScannerListeners != null) {
                for (MediaScannerCallback mediaScannerCallback : mMediaScannerListeners) {
                    if (mediaScannerCallback != null && mediaScannerCallback.mCallback != null) {
                        try {
                            mediaScannerCallback.mCallback.onMount(event.mPath);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 媒体控制的EventBus接收类
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaControl(EventMediaControl event) {
        if (mMediaControlListener != null) {
            switch (event.mControl) {
                case EventMediaControl.SUSPEND:
                    mMediaControlListener.suspend();
                    break;

                case EventMediaControl.RESUME:
                    mMediaControlListener.resume();
                    break;

                case EventMediaControl.PLAY:
                    mMediaControlListener.play();
                    break;

                case EventMediaControl.PAUSE:
                    mMediaControlListener.pause();
                    break;

                case EventMediaControl.PLAY_PAUSE:
                    mMediaControlListener.playPause();
                    break;

                case EventMediaControl.STOP:
                    mMediaControlListener.stop();
                    break;

                case EventMediaControl.NEXT:
                    mMediaControlListener.next();
                    break;

                case EventMediaControl.PREV:
                    mMediaControlListener.prev();
                    break;

                case EventMediaControl.SELECT:
                    mMediaControlListener.select(event.mValue);
                    break;

                case EventMediaControl.PLAY_RANDOM:
                    mMediaControlListener.playRandom();
                    break;

                case EventMediaControl.SET_PLAY_MODE:
                    mMediaControlListener.setPlayMode(event.mValue);
                    break;

                case EventMediaControl.SET_FAVOUR:
                    mMediaControlListener.setFavour(event.mValue != 0);
                    break;

                case EventMediaControl.SET_VOLUME:
                    mMediaControlListener.setVolume(event.mVolume);
                    break;

                case EventMediaControl.QUIT_APP:
                    mMediaControlListener.quitApp(event.mValue);
                    break;

                case EventMediaControl.FILTER:
                    mMediaControlListener.filter(event.mTitle, event.mSinger);
                    break;

                case EventMediaControl.VIDEO_PERMIT:
                    mMediaControlListener.onVideoPermitChanged(event.mValue == 1);
                    break;

                case EventMediaControl.SEEK_TO:
                    mMediaControlListener.seekTo(event.mValue);
                    break;

                default:
                    Logcat.d("Unknown action " + event.mControl);
                    break;
            }
        }
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param listener 连接服务的监听，监听服务是否连接成功
     * @param mediaControlListener 媒体控制的回调监听接口
     */
    public MediaManager(Context context, ConnectListener listener, IVIMedia.MediaControlListener mediaControlListener) {
        this (context, listener, mediaControlListener, true);
    }

    /**
     * 构造函数
     * @param context 上下问
     * @param listener 连接服务的监听，监听服务是否连接成功
     * @param mediaControlListener 媒体控制的回调监听接口
     * @param useDefaultEventBus 是否使用默认的EventBus，如果需要不使用默认的EventBus，可以使用false
     */
    public MediaManager(Context context, ConnectListener listener, IVIMedia.MediaControlListener mediaControlListener, boolean useDefaultEventBus) {
        super(context, listener, useDefaultEventBus);
        mMediaControlListener = mediaControlListener;
    }

    /**
     * 断开连接，在不使用该Manager对象时，必须调用 disconnect()
     */
    @Override
    public void disconnect() {
        mMediaInterface = null;
        mMediaInfoListeners = null;
        mAppGetAllMediaListCallbackMaps = null;
        mAppGetAppointPathMediaListCallbackMaps = null;
        mIGetMediaListCallback = null;
        mGetAppointPathMediaInfoCallback = null;
        super.disconnect();
        unRegisterScannerCallback();
        unregisterMediaInfoCallback();
        unregisterMediaControlCallback();
        unregisterMusicControlCallback();
        mMediaScannerCallback = null;
        mMediaInfoCallback = null;
        mMediaControlCallback = null;
        mMusicControlCallback = null;
        if (null != mMediaScannerListeners) {
            mMediaScannerListeners.clear();
            mMediaScannerListeners = null;
        }
        mMediaControlListener = null;
        mGetAllMediaListCallback = null;
        mAppGetAllMediaListCallback = null;
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.MEDIA_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mMediaInterface = IMedia.Stub.asInterface(service);
        registerScannerCallback();

        if (mMediaInfoListeners != null) {
            if (mMediaInfoListeners.size() > 0) {
                registerMediaInfoCallback();
                requestMediaInfoAndStateEvent();
            }
        }

        // 重新打开 media
        if (mMediaType != IVIMedia.Type.NONE) {
            if (mMediaZone == IVIMedia.Zone.UNKNOWN) {
                open(mMediaType);
            } else {
                openMediaInZone(mMediaType, mMediaZone);
            }
        }

        if (mMusicControlCallback != null) {
            registerMusicControlCallback();
        }

        // 主动请求一次能否播放视频的消息
        requestVideoPermitEvent();
    }

    @Override
    protected void onServiceDisconnected() {
        mMediaInterface = null;
    }

    private IMediaControlCallback.Stub mMediaControlCallback = new IMediaControlCallback.Stub() {
        @Override
        public void suspend() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.SUSPEND));
        }

        @Override
        public void resume() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.RESUME));
        }

        @Override
        public void quitApp(int quitSource) throws RemoteException {
            post(new EventMediaControl(EventMediaControl.QUIT_APP, quitSource));
        }

        @Override
        public void pause() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.PAUSE));
        }

        @Override
        public void play() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.PLAY));
        }

        @Override
        public void playPause() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.PLAY_PAUSE));
        }

        @Override
        public void stop() throws RemoteException {
            mMediaType = IVIMedia.Type.NONE;
            post(new EventMediaControl(EventMediaControl.STOP));
        }

        @Override
        public void next() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.NEXT));
        }

        @Override
        public void prev() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.PREV));
        }

        @Override
        public void setVolume(float volume) throws RemoteException {
            post(EventMediaControl.setVolume(volume));
        }

        @Override
        public void select(int index) throws RemoteException {
            post(new EventMediaControl(EventMediaControl.SELECT, index));
        }

        @Override
        public void playRandom() throws RemoteException {
            post(new EventMediaControl(EventMediaControl.PLAY_RANDOM));
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            post(new EventMediaControl(EventMediaControl.SET_PLAY_MODE, mode));
        }

        @Override
        public void filter(String title, String singer) throws RemoteException {
            post(EventMediaControl.filter(title, singer));
        }

        @Override
        public void setFavour(boolean favour) throws RemoteException {
            post(new EventMediaControl(EventMediaControl.SET_FAVOUR, favour ? 1 : 0));
        }

        @Override
        public void onVideoPermitChanged(boolean show) {
            post(new EventMediaControl(EventMediaControl.VIDEO_PERMIT, show ? 1 : 0));
        }

        @Override
        public void seekTo(int msec) {
            post(new EventMediaControl(EventMediaControl.SEEK_TO, msec));
        }
    };

    /**
     * 注册扫描监听，通过EventBus获取相关信息
     * @param callback 注册扫描回调的监听对象
     * @param type 需要监听的类型文件 {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
     */
    public void registerScannerListener(IMediaScannerCallback.Stub callback, int type) {
        registerScannerListener(callback, new int[]{type});
    }

    /**
     * 注册扫描监听，通过EventBus获取相关信息
     * @param callback 注册扫描回调的监听对象
     * @param types 可以同时监听几个类型文件 {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
     */
    public void registerScannerListener(IMediaScannerCallback.Stub callback, int[] types) {
        if (mMediaScannerListeners != null) {
            mMediaScannerListeners.add(new MediaScannerCallback(types, callback));
        }
    }

    /**
     * 取消注册扫描监听
     * @param callback 注册扫描回调的监听对象
     */
    public void unRegisterScannerListener(IMediaScannerCallback.Stub callback) {
        if (mMediaScannerListeners != null) {
            mMediaScannerListeners.remove(new MediaScannerCallback(null, callback));
        }
    }

    /**
     * 获取所有媒体列表的ID3信息， 包含子目录，视频，图片等可以不需要获取</br>
     * <b>注：该方法需要在onScanFinish回调之后获取，否则会获取不到数据</br>
     * @param type {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}， 目前只支持 MediaSqlDataType.AUDIO_TYPE
     * @param startWithPath 获取的路径，例：/mnt/sdcard/
     * @param callback 回调监听结果返回
     */
    public void getAllMediaList(int type, String startWithPath, IGetMediaListCallback.Stub callback) {
        if (mMediaInterface != null) {
            if (TextUtils.isEmpty(startWithPath)) {
                mAppGetAllMediaListCallback = callback;
            } else {
                if (mAppGetAllMediaListCallbackMaps != null) {
                    mAppGetAllMediaListCallbackMaps.put(startWithPath, callback);
                }
            }
            try {
                if (type < MediaSqlManager.MENU_TYPES.length && type >= 0) {
                    mMediaInterface.getAllMediaList(MediaSqlManager.MENU_TYPES[type].replace("\'", ""), startWithPath, mGetAllMediaListCallback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取当前目录的媒体列表，不包含子目录
    private Map<String, IGetMediaListCallback.Stub> mAppGetAllMediaListCallbackMaps = new HashMap<>();
    private IGetMediaListCallback.Stub mAppGetAllMediaListCallback = null; // 获取所有的数据，没有指定目录
    private IGetMediaListCallback.Stub mGetAllMediaListCallback = new IGetMediaListCallback.Stub() {
        @Override
        public void onProgress(List<StMusic> stAudios, String path) throws RemoteException {
            post(new EventGetMediaListCallback(EventGetMediaListCallback.PROGRESS_TYPE,
                    EventGetMediaListCallback.GET_APP_MEDIA, stAudios, path));
        }

        @Override
        public void onFinish(List<StMusic> stAudios, String path) throws RemoteException {
            post(new EventGetMediaListCallback(EventGetMediaListCallback.FINISH_TYPE,
                    EventGetMediaListCallback.GET_APP_MEDIA, stAudios, path));
        }
    };

    /**
     * 获取当前目录的媒体列表的id3信息，不包含子目录
     * @param type {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}， 目前只支持 MediaSqlDataType.AUDIO_TYPE
     * @param path 需要获取的路径，例：/mnt/sdcard/
     * @param callback 结果返回的回调
     */
    public void getAppointPathMediaList(int type, String path, IGetMediaListCallback.Stub callback) {
        if (mMediaInterface != null) {
            if (TextUtils.isEmpty(path)) {
                mIGetMediaListCallback = callback;
            } else {
                if (mAppGetAppointPathMediaListCallbackMaps != null) {
                    mAppGetAppointPathMediaListCallbackMaps.put(path, callback);
                }
            }
            try {
                if (type < MediaSqlManager.MENU_TYPES.length && type >= 0) {
                    mMediaInterface.getAppointPathMediaList(MediaSqlManager.MENU_TYPES[type].replace("\'", ""), path, mGetAppointPathMediaInfoCallback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取当前目录的媒体列表，不包含子目录
    private Map<String, IGetMediaListCallback.Stub> mAppGetAppointPathMediaListCallbackMaps = new HashMap<>(); // 获取指定目录的数据
    private IGetMediaListCallback.Stub mIGetMediaListCallback = null; // 获取所有目录的数据，传入的path为空
    private IGetMediaListCallback.Stub mGetAppointPathMediaInfoCallback = new IGetMediaListCallback.Stub() {
        @Override
        public void onProgress(List<StMusic> stAudios, String path) throws RemoteException {
            post(new EventGetMediaListCallback(EventGetMediaListCallback.PROGRESS_TYPE,
                    EventGetMediaListCallback.GET_APPOINT_PATH, stAudios, path));
        }

        @Override
        public void onFinish(List<StMusic> stAudios, String path) throws RemoteException {
            post(new EventGetMediaListCallback(EventGetMediaListCallback.FINISH_TYPE,
                    EventGetMediaListCallback.GET_APPOINT_PATH, stAudios, path));
        }
    };

    /**
     * 获取媒体的ID3信息列表的Event类
     */
    public static class EventGetMediaListCallback {

        /**
         * 获取进度，值为{@value}
         */
        public static final int PROGRESS_TYPE = 0;

        /**
         * 获取完成，值为{@value}
         */
        public static final int FINISH_TYPE   = 1;
        public int mType = PROGRESS_TYPE;

        /**
         * 获取的仅仅只是当前目录，调用 getAppointPathMediaList 接口
         */
        public static final int GET_APPOINT_PATH = 0;

        /**
         * 获取当前目录以及所有子目录，调用 getAllMediaList 接口
         */
        public static final int GET_APP_MEDIA    = 1;
        public int mUserType;

        /**
         * 媒体数据列表
         */
        public List<StMusic> mStMusics = new ArrayList<>();
        public String mPath = "";

        /**
         * 构造函数
         * @param type 操作类型 {@link EventGetMediaListCallback} PROGRESS_TYPE,FINISH_TYPE
         * @param userType 用户调用接口类型 {@link EventGetMediaListCallback} GET_APPOINT_PATH,getAllMediaList
         * @param stMusics 获取到的结果
         * @param path 获取的路径
         */
        public EventGetMediaListCallback(int type, int userType, List<StMusic> stMusics, String path) {
            mType = type;
            mUserType = userType;
            mStMusics = stMusics;
            mPath = path;
        }
    }

    /**
     * 获取到媒体id3信息列表的结果 EventBus 接收方法
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetMediaListCallback(EventGetMediaListCallback event) {
        if (event != null) {
            try {
                IGetMediaListCallback.Stub callback = null;
                switch (event.mUserType) {
                    case EventGetMediaListCallback.GET_APPOINT_PATH: // 获取指定目录的路径，不包括子目录
                        if (TextUtils.isEmpty(event.mPath)) {
                            callback = mIGetMediaListCallback;
                        } else {
                            if (mAppGetAppointPathMediaListCallbackMaps != null) {
                                callback = mAppGetAppointPathMediaListCallbackMaps.get(event.mPath);
                            }
                        }
                        break;

                    case EventGetMediaListCallback.GET_APP_MEDIA: // 获取指定目录下所有的媒体，包括子目录
                        if (TextUtils.isEmpty(event.mPath)) {
                            callback = mAppGetAllMediaListCallback;
                        } else {
                            if (mAppGetAllMediaListCallbackMaps != null) {
                                callback = mAppGetAllMediaListCallbackMaps.get(event.mPath);
                            }
                        }
                        break;
                }

                switch (event.mType) {
                    case EventGetMediaListCallback.PROGRESS_TYPE:
                        if (callback != null) {
                            callback.onProgress(event.mStMusics, event.mPath);
                        }
                        break;
                    case EventGetMediaListCallback.FINISH_TYPE:
                        if (callback != null) {
                            callback.onFinish(event.mStMusics, event.mPath);
                        }

                        if (!TextUtils.isEmpty(event.mPath)) { // 完成之后，剔除
                            if (mAppGetAppointPathMediaListCallbackMaps != null) {
                                if (mAppGetAppointPathMediaListCallbackMaps.containsValue(callback)) {
                                    mAppGetAppointPathMediaListCallbackMaps.remove(event.mPath);
                                }
                            }
                            if (mAppGetAllMediaListCallbackMaps != null) {
                                if (mAppGetAllMediaListCallbackMaps.containsValue(callback)) {
                                    mAppGetAllMediaListCallbackMaps.remove(event.mPath);
                                }
                            }
                        }
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册扫描监听
     */
    private void registerScannerCallback() {
        if (null != mMediaScannerListeners && mMediaScannerListeners.size() == 0) {
            Logcat.e("listeners is null");
        }

        super.registerCallback(mMediaScannerCallback);
        if (mMediaInterface != null) {
            try {
                mMediaInterface.registerScannerCallback(mMediaScannerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注销扫描监听
     */
    private void unRegisterScannerCallback() {
        super.unRegisterCallback(mMediaScannerCallback);
        if (mMediaInterface != null) {
            try {
                mMediaInterface.unRegisterScannerCallback(mMediaScannerCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IMediaScannerCallback.Stub mMediaScannerCallback = new IMediaScannerCallback.Stub() {

        @Override
        public void onScanStart(int type, int sqlType) throws RemoteException {
            post(new EventScanStart(type, sqlType));
        }

        @Override
        public void onScanFinish(int type, int sqlType, String path, String oldPath) throws RemoteException {
            post(new EventScanFinished(type, sqlType, path, oldPath));
        }

        @Override
        public void onEject(String path, boolean isDiskPowerDown) throws RemoteException {
            post(new EventEject(path, isDiskPowerDown));
        }

        @Override
        public void onMount(String path) throws RemoteException {
            post(new EventMount(path));
        }
    };

    private IMediaInfoCallback.Stub mMediaInfoCallback = new IMediaInfoCallback.Stub() {
        @Override
        public void onMediaChange(int mediaType, String name, String info, int artWidth, int artHeight, byte[] artBytes, int index, int totalCount, boolean popup) {
            post(new IVIMedia.MediaInfo(mediaType, name, info, artWidth, artHeight, artBytes, index, totalCount, popup));
        }

        @Override
        public void onPlayStateChange(int mediaType, int playState, int position, int duration) {
            post(new IVIMedia.MediaState(mediaType, playState, position, duration));
        }

        @Override
        public void onMediaZoneChanged(int mediaType, int zone) {
            post(new IVIMedia.MediaZone(mediaType, zone));
        }

        @Override
        public void onCurrentShownMediaTypeChanged(int mediaType) throws RemoteException {
            post(new IVIMedia.MediaTypeShownInfo(mediaType));
        }
    };

    /**
     * 注册媒体信息监听，通过EventBus获取媒体数据的更新
     * @param callback 监听媒体信息改变的广播
     */
    public void registerMediaInfoListener(IMediaInfoCallback.Stub callback) {
        if (callback != null) {
            if (mMediaInfoListeners != null) {
                mMediaInfoListeners.add(callback);
            }
            registerMediaInfoCallback();
        }
    }

    /**
     * 取消注册媒体信息监听
     * @param callback 媒体信息改变的广播，注：要和 registerMediaInfoListener 指针对应
     */
    public void unregisterMediaInfoListener(IMediaInfoCallback.Stub callback) {
        if (callback != null) {
            if (mMediaInfoListeners != null) {
                mMediaInfoListeners.remove(callback);
                if (mMediaInfoListeners.size() == 0) {
                    unregisterMediaInfoCallback();
                }
            }
        }
    }

    /**
     * 注册数据监听回调，该方法主要是在非媒体界面，希望获取媒体的数据，通过监听该回调
     */
    private void registerMediaInfoCallback() {
        super.registerCallback(mMediaInfoCallback);
        if (mMediaInterface != null) {
            if (mMediaInfoCallback != null) {
                try {
                    mMediaInterface.registerMediaInfoCallback(mMediaInfoCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注销数据监听回调
     */
    private void unregisterMediaInfoCallback() {
        super.unRegisterCallback(mMediaInfoCallback);
        if (mMediaInterface != null) {
            if (null != mMediaInfoCallback) {
                try {
                    mMediaInterface.unRegisterMediaInfoCallback(mMediaInfoCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注销媒体控制回调
     */
    private void unregisterMediaControlCallback() {
        super.unRegisterCallback(mMediaControlCallback);
        if (mMediaInterface != null) {
            if (null != mMediaControlCallback) {
                try {
                    mMediaInterface.unRegisterMediaControlCallback(mMediaControlCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断当前是否允许播放 </br>
     * <b>在进行播放媒体之前，需要调用该方法进行判断是否允许播放，有时在蓝牙通话等状态，是不允许播放的</b></br>
     * @return 允许播放返回 true，否则返回 false
     */
    public boolean canPlayMedia() {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.canPlayMedia();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("service not connected");
        }

        return true;
    }

    /**
     * 打开媒体 </br>
     * 此时，sdk会帮助你申请音频焦点，通道切换等一系列动作。 </br>
     * <b>调用该方法，sdk并不会帮你进行播放，例，音乐还是需要自己进行播放，该方法主要作用是通知sdk，我要播放了。</b></br>
     * @param mediaType {@link com.roadrover.sdk.media.IVIMedia.Type}
     */
    public void open(int mediaType) {
        mMediaType = mediaType;
        if (mMediaInterface != null) {
            try {
                mMediaInterface.open(mediaType, mMediaControlCallback, mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        super.registerCallback(mMediaControlCallback);
    }

    /**
     * 关闭媒体，调用该方法之后，sdk会帮你将焦点释放，并且通道会切换回PC状态 </br>
     * <b>调用该方法不会真正的暂停媒体，比如MediaPlayer的停止，需要APP进行调用</b></br>
     * @param mediaType {@link com.roadrover.sdk.media.IVIMedia.Type}
     */
    public void close(int mediaType) {
        if (mMediaType == mediaType) {
            mMediaType = IVIMedia.Type.NONE;
        }

        if (null != mMediaInterface) {
            try {
                mMediaInterface.close(mediaType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当前媒体是否被打开
     * @param mediaType {@link com.roadrover.sdk.media.IVIMedia.Type}
     * @return
     */
    public boolean isOpened(int mediaType) {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.isOpened(mediaType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 设置媒体信息，通过该方法调用后，其他APP如果有注册 MediaInfoCallback 回调，则会收到 onMediaChanged 回调 </br>
     * 同时，如果原车有小屏，也会将该消息通知到小屏显示 </br>
     * @param mediaType {@link com.roadrover.sdk.media.IVIMedia.Type}
     * @param name 文件名
     * @param info 播放信息
     * @param artImage 专辑图片
     * @param index 第几首
     * @param totalCount 总歌曲数
     * @param popup 是否弹出媒体信息小窗口，一般在上下曲的时候为true，其他为false
     */
    public void setMediaInfo(int mediaType, String name, String info, Bitmap artImage, int index, int totalCount, boolean popup) {
        if (mMediaInterface != null) {
            try {
                IVIMedia.ArtImage image = new IVIMedia.ArtImage(artImage);
                mMediaInterface.setMediaInfo(mediaType, name, info, image.mWidth, image.mHeight, image.mPixels, index, totalCount, popup);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前播放状态
     * @param playState {@link IVIMedia.MediaState}
     * @param position 当前播放进度 （单位秒）
     * @param duration 总时间 (单位秒）
     */
    public void setMediaState(int mediaType, int playState, int position, int duration) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.setMediaState(mediaType, playState, position, duration);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前播放状态信息
     * 仅作为当前不能作为媒体源的媒体类型信息，如图库。
     */
    public void setCurrentShownMediaType(int mediaType) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.setCurrentShownMediaType(mediaType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否是否允许播放，一般如果是设置里面，手刹状态开关打开，并且此时是手刹拉起状态，该方法会返回 false
     * @return
     */
    public boolean isVideoPermit() {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.isVideoPermit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return true;
    }

    /**
     * 控制播放，例：Launcher有一个播控键，点击进行播放音乐时候调用该方法。</br></br>
     * 流程如下：</br>
     *    1) launcher调用play</br>
     *    2) 音乐Manager收到 MediaControl 消息，通过MediaControlListener回调 play() 接口给音乐</br>
     *    3) 音乐收到 play() 消息之后进行音乐的播放动作</br>
     */
    public void play() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 控制暂停播放，例：Launcher有一个播控键，点击进行暂停播放时候调用该方法。</br></br>
     * 流程如下：</br>
     *    1) launcher调用pause</br>
     *    2) 音乐Manager收到 MediaControl 消息，通过MediaControlListener回调 pause() 接口给音乐</br>
     *    3) 音乐收到 pause() 消息之后进行音乐的暂停播放动作</br>
     */
    public void pause() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 播放，暂停动作，调用一次进行播放，再次调用进行暂停 </br>
     * 流程同play(),pause()方法 </br>
     */
    public void playPause() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.playPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 上一曲 </br>
     * 流程同play(),pause()方法 </br>
     */
    public void prev() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.prev();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 下一曲 </br>
     * 流程同play(),pause()方法 </br>
     */
    public void next() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 拖动当前播放的音乐的进度
     * @param msec 毫秒
     */
    public void seekTo(int msec) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.seekTo(msec);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 请求获取当前的媒体信息，例：Launcher播控键起来后需要刷新当前的媒体信息，可以通过调用该方法进行获取 </br></br>
     * 流程如下：</br>
     *    1) <b>首先需要监听 IVIMedia.MediaInfo，IVIMedia.MediaState 两个EventBus消息</b></br>
     *    2) 调用 requestMediaInfoAndStateEvent 方法，Services依次会发送IVIMedia.MediaInfo和IVIMedia.MediaState消息</br>
     *    3) 监听到消息之后进行UI刷新</br>
     */
    public void requestMediaInfoAndStateEvent() {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.requestMediaInfoAndStateEvent();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 主动请求发送一次是否允许视频播放的消息，例：视频进入之后，需要知道当前是否可以播放视频，通过调用该接口进行获取 </br></br>
     * 流程如下：</br>
     *    1) <b>首先需要监听 EventMediaControl EventBus消息，或者监听MediaControlListener回调</b></br>
     *    2) 调用 requestVideoPermitEvent() 方法，sdk会获取当前是否可以播放状态之后发送一个Event消息</br>
     *    3) 在onVideoPermitChanged回调进行视频的遮盖操作</br>
     */
    public void requestVideoPermitEvent() {
        if (mMediaInterface != null) {
            try {
                boolean permit = mMediaInterface.isVideoPermit();
                post(new EventMediaControl(EventMediaControl.VIDEO_PERMIT, permit ? 1 : 0));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 打开媒体所属的UI（APK）</br>
     * 一般为桌面widget想要进入当前播放的媒体的apk时调用</br>
     */
    public void launchApp(int mediaType) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.launchApp(mediaType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 选择媒体输出区，必须在open相同的mediaType之后才起作用
     * @param mediaType {@link IVIMedia.Type}
     * @param zone {@link IVIMedia.Zone}
     */
    public void setMediaZone(int mediaType, int zone) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.setMediaZone(mediaType, zone);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 选择某个媒体输出区是Enable还是Disable，必须在open mediaType之后才起作用
     * @param mediaType {@link IVIMedia.Type}, 只支持MASTER和SECONDARY，
     * @param targetZone {@link IVIMedia.Zone} 目的Zone
     * @param enable true 打开，false 关闭
     */
    public void switchMediaZone(int mediaType, int targetZone, boolean enable) {
        if (targetZone == IVIMedia.Zone.ALL ||targetZone == IVIMedia.Zone.UNKNOWN) {
            Logcat.e(IVIMedia.Type.getName(targetZone) + " is not support");
            return;
        }

        if (mMediaInterface != null) {
            try {
                mMediaInterface.switchMediaZone(mediaType, targetZone, enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 得到媒体的输出区
     * @param mediaType {@link IVIMedia.Type}
     * @return {@link IVIMedia.Zone}
     */
    public int getMediaZone(int mediaType) {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.getMediaZone(mediaType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return IVIMedia.Zone.UNKNOWN;
    }

    /**
     * 判断当前媒体是否在指定的区域
     * @param mediaType {@link IVIMedia.Type}
     * @param targetZone {@link IVIMedia.Zone}
     * @return
     */
    public boolean isMediaInZone(int mediaType, int targetZone) {
        int zone = getMediaZone(mMediaType);
        return IVIMedia.Zone.isZoneInZone(zone, targetZone);
    }

    /**
     * 打开媒体，双区媒体专用
     * @param mediaType 媒体类型 {@link IVIMedia.Type}
     * @param zone {@link IVIMedia.Zone}
     */
    public void openMediaInZone(int mediaType, int zone) {
        mMediaType = mediaType;
        mMediaZone = zone;
        if (mMediaInterface != null) {
            try {
                mMediaInterface.openMediaInZone(mediaType, mMediaControlCallback, mContext.getPackageName(), zone);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.e("service not connected");
        }

        super.registerCallback(mMediaControlCallback);
    }

    /**
     *  得到双区媒体某个区域的媒体
     *  @param zone {@link IVIMedia.Zone}，只支持MASTER和SECONDARY
     *  @return {@link IVIMedia.Type} 中的类型
     */
    public int getMediaFromZone(int zone) {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.getMediaFromZone(zone);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return IVIMedia.Type.NONE;
    }

    /**
     * 监听mediaInfo改变 event 事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaInfoChanged(IVIMedia.MediaInfo event) {
        if (event != null && event.mArtImage != null) {
            if (mMediaInfoListeners != null) {
                for (IMediaInfoCallback.Stub callback : mMediaInfoListeners) {
                    try {
                        callback.onMediaChange(event.mMediaType,
                                event.mName,
                                event.mInfo,
                                event.mArtImage.mWidth,
                                event.mArtImage.mHeight,
                                event.mArtImage.mPixels,
                                event.mIndex,
                                event.mTotalCount,
                                event.mPopup);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 监听mediaState改变的event事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaStateChanged(IVIMedia.MediaState event) {
        if (event != null) {
            if (mMediaInfoListeners != null) {
                for (IMediaInfoCallback.Stub callback : mMediaInfoListeners) {
                    try {
                        callback.onPlayStateChange(event.mMediaType,
                                event.mState,
                                event.mPosition,
                                event.mDuration);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 监听mediaZone改变的event事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMediaZoneChanged(IVIMedia.MediaZone event) {
        if (event != null) {
            if (mMediaInfoListeners != null) {
                for (IMediaInfoCallback.Stub callback : mMediaInfoListeners) {
                    try {
                        callback.onMediaZoneChanged(event.mMediaType,
                                event.mZone);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除文件 </br>
     * 部分平台刚刚插入U盘时，删除文件，通过MultiFileObserver没办法监听到，所以增加接口，在删除文件时通知services </br>
     * 目前IMX6存在该问题，T3不存在 </br>
     * @param path 文件路径
     */
    public void sendDeleteFileEvent(String path) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.sendDeleteFileEvent(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件写入完成 </br>
     * 部分平台刚刚插入U盘时，拷贝文件，通过MultiFileObserver没办法监听到，所以增加接口，在拷贝文件，写入文件时候通知Services </br>
     * @param path 文件路径
     */
    public void sendWriteFinishedEvent(String path) {
        if (mMediaInterface != null) {
            try {
                mMediaInterface.sendWriteFinishedEvent(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回当前的媒体类型
     * @return {@link com.roadrover.sdk.media.IVIMedia.Type}
     */
    public int getMediaType() {
        return mMediaType;
    }

    /**
     * 获取当前整个系统的媒体类型，该方法和getMediaType()方法区别在于，getMediaType()是内部标记用的，该方法是获取整个系统的当前有效媒体
     * @return {@link com.roadrover.sdk.media.IVIMedia.Type}
     */
    public int getActiveMedia() {
        if (mMediaInterface != null) {
            try {
                return mMediaInterface.getActiveMedia();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return IVIMedia.Type.NONE;
    }

    /**
     * 监听语音控制的回调对象
     */
    private IMusicControlCallback.Stub mMusicControlCallback = null;

    /**
     * 注册监听音乐的语音控制回调 </br>
     * 与 registerMediaControlCallback 区别在于，该接口注册后，及时当前不在音频焦点状态，也可以获取到回调信息 </br>
     */
    public void registerMusicControlCallback() {
        if (mMusicControlCallback == null) {
            mMusicControlCallback = new IMusicControlCallback.Stub() {
                @Override
                public void setPlayMode(int mode) throws RemoteException {
                    post(new EventMediaControl(EventMediaControl.SET_PLAY_MODE, mode));
                }

                @Override
                public void playRandom() throws RemoteException {
                    post(new EventMediaControl(EventMediaControl.PLAY_RANDOM));
                }

                @Override
                public void filter(String title, String singer) throws RemoteException {
                    post(EventMediaControl.filter(title, singer));
                }
            };
        }

        super.registerCallback(mMusicControlCallback);
        if (mMediaInterface != null) {
            try {
                mMediaInterface.registerMusicControlCallback(mMusicControlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注销监听音乐的语音控制回调接口
     */
    public void unregisterMusicControlCallback() {
        super.unRegisterCallback(mMusicControlCallback);
        if (mMediaInterface != null) {
            try {
                mMediaInterface.unregisterMusicControlCallback(mMusicControlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMusicControlCallback = null;
    }
}
