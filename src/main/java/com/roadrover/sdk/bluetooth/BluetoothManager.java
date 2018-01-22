package com.roadrover.sdk.bluetooth;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.btservice.bluetooth.BluetoothDevice;
import com.roadrover.btservice.bluetooth.BluetoothVCardBook;
import com.roadrover.btservice.bluetooth.IBluetooth;
import com.roadrover.btservice.bluetooth.IBluetoothCallback;
import com.roadrover.btservice.bluetooth.IBluetoothExecCallback;
import com.roadrover.btservice.bluetooth.IBluetoothLinkDeviceCallback;
import com.roadrover.btservice.bluetooth.IBluetoothStatusCallback;
import com.roadrover.btservice.bluetooth.IBluetoothVCardCallback;
import com.roadrover.btservice.bluetooth.IDeviceCallback;
import com.roadrover.btservice.bluetooth.ISearchDeviceCallback;
import com.roadrover.sdk.utils.ListUtils;
import com.roadrover.sdk.utils.Logcat;
import com.roadrover.sdk.utils.TimerUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙接口管理类
 * 所有接口的调用(除获取蓝牙版本的接口外)，必须在调用 openBluzModule 之后才能调用
 */
public class BluetoothManager extends BaseManager {
    private IBluetooth mIBluetooth;

    // 蓝牙执行命令超时的定时器
    private TimerUtil mBtCommandTimerUtil = null;
    private static final int BT_COMMAND_EXEC_TIME = 20 * 1000; // 一条命令最多执行的时间
    private boolean mModuleOpened = false;

    private IBluetoothExecCallback.Stub mLinkDeviceCallback; // 连接设备的定时器

    /**
     * 蓝牙接口类的构造函数
     * @param context 上下文
     * @param listener 连接的监听
     */
    public BluetoothManager(Context context, ConnectListener listener) {
        super(context, listener, true);
    }

    /**
     * 断开和服务的连接
     */
    @Override
    public void disconnect() {
        mIBluetooth = null;
        if (mBtCommandTimerUtil != null) {
            mBtCommandTimerUtil.stop();
            mBtCommandTimerUtil = null;
        }
        mLinkDeviceCallback = null;
        mAppIbluetoothCallback = null;
        mIBluetoothCallback = null;
        mUserOpenBluetoothModuleCallback = null;
        mOpenBluetoothModuleCallback = null;
        mUserModuleNameCallback = null;
        mUserModulePINCallback = null;
        mModuleNameCallback = null;
        mModulePINCallback = null;
        mUserUnlinkDeviceCallback = null;
        mDeleteDeviceCallback = null;
        mBluetoothLinkDeviceCallback = null;
        mUnlinkDeviceCallback = null;
        mUserBluetoothStateCallback = null;
        mBluetoothDeleteDeviceCallback = null;
        mBluetoothStateCallback = null;
        mVCardListeners = null;
        if (mGetVCardTimerUtil != null) {
            mGetVCardTimerUtil.stop();
            mGetVCardTimerUtil = null;
        }
        mISearchDeviceCallback = null;
        mIDeviceCallback = null;
        mBluetoothNameCallback = null;
        mBluetoothPinCallback = null;
        mAppVCardCallback = null;
        mIBluetoothVCardCallback = null;
        mAppSearchDeviceCallback = null;
        mUserDeviceCallback = null;
        mUserBluetoothNameCallback = null;
        mUserBluetoothPinCallback = null;
        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.BLUETOOTH_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mIBluetooth = IBluetooth.Stub.asInterface(service);
    }

    @Override
    protected void onServiceDisconnected() {
        mIBluetooth = null;
    }

    private IBluetoothCallback.Stub mAppIbluetoothCallback = null; // app 设过来的
    private IBluetoothCallback.Stub mIBluetoothCallback = new IBluetoothCallback.Stub() {
        @Override
        public void onConnectStatus(int status, String addr, String name) throws RemoteException {
            Logcat.d("nStatus:" + status + " addr:" + addr + " name:" + name);
            post(new IVIBluetooth.EventLinkDevice(status, addr, name));
        }

        @Override
        public void onCallStatus(int status, String phoneNumber, String contactName) throws RemoteException {
            post(new IVIBluetooth.CallStatus(status, phoneNumber, contactName));
        }

        @Override
        public void onVoiceChange(int type) throws RemoteException {
            post(new IVIBluetooth.EventVoiceChange(type));
        }

        @Override
        public void onA2DPConnectStatus(int a2dpStatus, boolean isStoped) throws RemoteException {
            post(new IVIBluetooth.EventModuleConnectStatus(a2dpStatus, isStoped));
        }

        @Override
        public void onBtMusicId3Info(String name, String artist, String album, long duration) throws RemoteException {
            post(new IVIBluetooth.EventMp3Id3Info(name, artist, album, duration));
        }

        @Override
        public void onBtBatteryValue(int value) throws RemoteException {
            Logcat.d("onBtBatteryValue  value="+value);
            post(new IVIBluetooth.EventBatteryValue(value));
        }

        @Override
        public void onBtSignalValue(int value) throws RemoteException {
            Logcat.d("onBtSignalValue  value="+value);
            post(new IVIBluetooth.EventSignalValue(value));
        }

        @Override
        public void onPowerStatus(boolean value) throws RemoteException {
            Logcat.d("onPowerStatus  value="+value);
            post(new IVIBluetooth.EventPowerState(value));
        }

    };

    /**
     * 获取蓝牙版本
     * @param callback 结果通过回调获取 onSuccess 方法返回
     */
    public void getBluetoothVer(IBluetoothExecCallback.Stub callback) {
        if (null == mIBluetooth) {
            CmdExecResultUtil.execError(callback, IVIBluetooth.BluetoothExecErrorMsg.ERROR_INITING);
            return;
        }
        try {
            mIBluetooth.getBluetoothVer(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开蓝牙模块
     * @param callback 结果通过回调获取 onSuccess 代表打开成功
     */
    public void openBluetoothModule(IBluetoothExecCallback.Stub callback) {
        mUserOpenBluetoothModuleCallback = callback;

        if (null == mIBluetooth) {
            CmdExecResultUtil.execError(mOpenBluetoothModuleCallback, IVIBluetooth.BluetoothExecErrorMsg.ERROR_INITING);
            return;
        }

        try {
            mIBluetooth.openBluetoothModule(mOpenBluetoothModuleCallback);
            mIBluetooth.requestBluetoothListener(mIBluetoothCallback);
            mModuleOpened = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IBluetoothExecCallback.Stub mOpenBluetoothModuleCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventOpenBluetoothModuleCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventOpenBluetoothModuleCallback(errorCode));
        }
    };

    private IBluetoothExecCallback.Stub mUserOpenBluetoothModuleCallback;

    private static class EventOpenBluetoothModuleCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventOpenBluetoothModuleCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventOpenBluetoothModuleCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenBluetoothModuleCallback(EventOpenBluetoothModuleCallback event) {
        if (event != null && mUserOpenBluetoothModuleCallback != null) {
            try {
                switch (event.mType) {
                    case EventOpenBluetoothModuleCallback.ON_SUCCESS:
                        mUserOpenBluetoothModuleCallback.onSuccess(event.mMsg);
                        break;

                    case EventOpenBluetoothModuleCallback.ON_FAILURE:
                        mUserOpenBluetoothModuleCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置蓝牙模块
     * @param callback 结果通过回调获取 onSuccess 代表重置成功
     */
    public void resetBluetoothModule(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.resetBluetoothModule(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭蓝牙模块，该接口暂时不要调用，目前模块都不支持关闭操作
     * @param callback 结果通过回调获取 onSuccess 代表关闭失败
     */
    public void closeBluetoothModule(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.unrequestBluetoothListener(mIBluetoothCallback);
                if (callback != null) {
                    mIBluetooth.closeBluetoothModule(callback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mModuleOpened = false;
        }
    }

    /**
     * 修改蓝牙模块名字
     * @param newName 指定名字
     * @param callback
     */
    public void modifyModuleName(String newName, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            mUserModuleNameCallback = callback;
            try {
                mIBluetooth.modifyModuleName(newName, mModuleNameCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mModuleNameCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventModuleNameCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventModuleNameCallback(errorCode));
        }
    };

    private IBluetoothExecCallback.Stub mUserModuleNameCallback;

    private static class EventModuleNameCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventModuleNameCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventModuleNameCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 蓝牙名字修改结果回调， 通过EventBus通知，外部如果想监听回调结果，也可以直接监听EventBus，不通过回调
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventModuleNameCallback(EventModuleNameCallback event) {
        if (event != null && mUserModuleNameCallback != null) {
            try {
                switch (event.mType) {
                    case EventModuleNameCallback.ON_SUCCESS:
                        mUserModuleNameCallback.onSuccess(event.mMsg);
                        break;

                    case EventModuleNameCallback.ON_FAILURE:
                        mUserModuleNameCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改蓝牙 pin 码
     * @param newPin
     * @param callback
     */
    public void modifyModulePIN(String newPin, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            mUserModulePINCallback = callback;

            try {
                mIBluetooth.modifyModulePIN(newPin, mModulePINCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mModulePINCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventModulePINCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventModulePINCallback(errorCode));
        }
    };
    private IBluetoothExecCallback.Stub mUserModulePINCallback;

    private static class EventModulePINCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventModulePINCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventModulePINCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 修改蓝牙pin码的结果返回，通过EventBus通知，如果不监听回调，也可以监听EventBus
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventModulePINCallback(EventModulePINCallback event) {
        if (event != null && mUserModulePINCallback != null) {
            try {
                switch (event.mType) {
                    case EventModulePINCallback.ON_SUCCESS:
                        mUserModulePINCallback.onSuccess(event.mMsg);
                        break;

                    case EventModulePINCallback.ON_FAILURE:
                        mUserModulePINCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 连接指定地址的设备
     * @param addr 蓝牙模块的地址
     * @param callback
     */
    public void linkDevice(String addr, final IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            mLinkDeviceCallback = callback;
            startLinkDeviceTimer(); // 开启定时器
            try {
                mIBluetooth.linkDevice(addr, mBluetoothLinkDeviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mBluetoothLinkDeviceCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventLinkDeviceCallback(msg));
            // 成功后的取消定时器操作在IBluetoothCallback onEventLinkDevice 中执行
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventLinkDeviceCallback(errorCode));
            stopLinkDeviceTimer(); // 如果出错了，直接停止定时器
        }
    };

    private static class EventLinkDeviceCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventLinkDeviceCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventLinkDeviceCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 连接设备的结果返回，通过EventBus通知，如果不监听回调，也可以监听EventBus
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLinkDeviceCallback(EventLinkDeviceCallback event) {
        if (event != null && mLinkDeviceCallback != null) {
            try {
                switch (event.mType) {
                    case EventLinkDeviceCallback.ON_SUCCESS:
                        // 设备连接成功，在该位置不做处理，状态数据通过IBluetoothCallback onEventLinkDevice 返回，该位置只是命令发送成功
                        mLinkDeviceCallback.onSuccess(event.mMsg);
                        break;

                    case EventLinkDeviceCallback.ON_FAILURE:
                        mLinkDeviceCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 断开蓝牙连接
     * @param callback
     */
    public void unlinkDevice(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            mUserUnlinkDeviceCallback = callback;

            try {
                mIBluetooth.unlinkDevice(mUnlinkDeviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mUnlinkDeviceCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventUnlinkDeviceCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventUnlinkDeviceCallback(errorCode));
        }
    };
    private IBluetoothExecCallback.Stub mUserUnlinkDeviceCallback;

    private static class EventUnlinkDeviceCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventUnlinkDeviceCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventUnlinkDeviceCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 断开连接的EventBus，如果未监听回调，可以通过监听EventBus获取，如果在调用unlinkDevice方法时有传入回调，可以不管该方法
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUnlinkDeviceCallback(EventUnlinkDeviceCallback event) {
        if (event != null && mUserUnlinkDeviceCallback != null) {
            try {
                switch (event.mType) {
                    case EventUnlinkDeviceCallback.ON_SUCCESS:
                        mUserUnlinkDeviceCallback.onSuccess(event.mMsg);
                        break;

                    case EventUnlinkDeviceCallback.ON_FAILURE:
                        mUserUnlinkDeviceCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定设备，通过序号删除
     * @param addr 设备地址
     * @param callback
     */
    public void deleteDevice(String addr, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            mDeleteDeviceCallback = callback;
            try {
                mIBluetooth.deleteDevice(addr, mBluetoothDeleteDeviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mBluetoothDeleteDeviceCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new IVIBluetooth.EventDeleteDevice(true, 0, msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new IVIBluetooth.EventDeleteDevice(false, errorCode, ""));
        }
    };

    /**
     * 删除所有设备
     * @param callback
     */
    public void deleteAllDevice(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.deleteAllDevice(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙音量，模块音量
     * @param callback
     */
    public void addVolume(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.addVolume(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 减少音量，模块音量
     * @param callback
     */
    public void delVolume(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.delVolume(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对模块进行mute 和 unmute操作
     * @param isMute
     * @param callback
     */
    public void muteBluetoothModule(boolean isMute, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.muteBluetoothModule(isMute, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打电话给指定号码
     * @param phoneNumber 指定电话号码
     * @param callback
     */
    public void callPhone(String phoneNumber, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.callPhone(phoneNumber, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重拨电话
     * @param callback
     */
    public void recallPhone(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.recallPhone(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 挂断电话
     * @param callback
     */
    public void hangPhone(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.hangPhone(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 来电拒接
     * @param callback
     */
    public void rejectPhone(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.rejectPhone(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接听电话
     * @param callback
     */
    public void listenPhone(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.listenPhone(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 语音拨号
     * @param callback
     */
    public void voiceDial(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.voiceDial(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 语音转换，手机端，车机端之间转换
     * @param callback
     */
    public void transferCall(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.transferCall(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 等待接听
     * @param type
     * @param callback
     */
    public void waitCall(int type, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.waitCall(type, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通话状态请求 DTMF
     * @param code {@link com.roadrover.sdk.bluetooth.IVIBluetooth.BluetoothDTMFCode}
     * @param callback
     */
    public void requestDTMF(int code, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.requestDTMF(code, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对mic进行mute操作，调用在mute和unMute之间来回切换
     * @param isMute 是否是mute
     * @param callback
     */
    public void muteMic(boolean isMute, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.muteMic(isMute, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取蓝牙设备的电量，部分模块不支持
     * @param callback
     */
    public void getDeviceBattery(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getDeviceBattery(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取手机设备的信号强度，部分模块不支持
     * @param callback
     */
    public void getDeviceSignalStrength(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getDeviceSignalStrength(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取蓝牙模块的状态，该方法会返回模块当前 hfp, a2dp等的状态
     * @param callback
     */
    public void getBluetoothModuleStatus(IBluetoothStatusCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getBluetoothModuleStatus(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取蓝牙当前连接的设备的状态 </br>
     * <b>该方法不同于getBluetoothModuleStatus，getBluetoothModuleStatus方法是获取当前整个蓝牙模块连接的设备的所有状态，包括 hf, a2dp等 </br>
     * 本方法主要是获取连接的设备，包括连接设备的地址，名字等 </br></b>
     * @param callback
     */
    public void getBluetoothState(IBluetoothLinkDeviceCallback.Stub callback) {
        if (isSendToService(callback)) {
            mUserBluetoothStateCallback = callback;

            try {
                mIBluetooth.getBluetoothState(mBluetoothStateCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothLinkDeviceCallback.Stub mBluetoothStateCallback = new IBluetoothLinkDeviceCallback.Stub() {
        @Override
        public void onSuccess(int status, String addr, String name) throws RemoteException {
            post(new EventBluetoothStateCallback(status, addr, name));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventBluetoothStateCallback(errorCode));
        }
    };

    private IBluetoothLinkDeviceCallback.Stub mUserBluetoothStateCallback;

    public static class EventBluetoothStateCallback {
        public int mStatus;
        public String mAddr;
        public String mName;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventBluetoothStateCallback(int status, String addr, String name) {
            mStatus = status;
            mAddr = addr;
            mName = name;
            mType = ON_SUCCESS;
        }

        public EventBluetoothStateCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 获取蓝牙状态的EventBus回调接口，如果在调用 getBluetoothState 方法，未传入回调，可以通过监听EventBus获取
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBluetoothStateCallback(EventBluetoothStateCallback event) {
        if (event != null && mUserBluetoothStateCallback != null) {
            try {
                switch (event.mType) {
                    case EventBluetoothStateCallback.ON_SUCCESS:
                        mUserBluetoothStateCallback.onSuccess(event.mStatus, event.mAddr, event.mName);
                        break;

                    case EventBluetoothStateCallback.ON_FAILURE:
                        mUserBluetoothStateCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 请求A2DP的连接，可以不用调用该接口，直接调用 linkDevice接口，会同时连接HFP和A2DP
     * @param isConnected 连接还是断开
     * @param callback
     */
    public void requestA2DPConnect(boolean isConnected, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.requestA2DPConnect(isConnected, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求HFP的连接，可以不用调用该接口，直接调用 linkDevice接口，会同时连接HFP和A2DP
     * @param isConnected
     * @param callback
     */
    public void requestHFPConnect(boolean isConnected, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.requestHFPConnect(isConnected, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置蓝牙监听回调
     * @param listener
     */
    public void registerBluetoothCallback(IBluetoothCallback.Stub listener) {
        mAppIbluetoothCallback = listener;
    }

    /**
     * 注销蓝牙回调监听
     * @param listener
     */
    public void unregisterBluetoothCallback(IBluetoothCallback.Stub listener) {
        mAppIbluetoothCallback = null;
    }

    /*************************** vCard 获取接口开始 *******************************/

    public static class VCardListener {
        public int type;
        public IBluetoothVCardCallback.Stub callback;
    }
    private List<VCardListener> mVCardListeners = new ArrayList<>(); // 命令列表
    private static final int GET_PHONE_CONTACTS         = 0; // 获取联系人
    private static final int GET_RECEIVED_PHONE_BOOK    = 1; // 获取已接
    private static final int GET_DIALED_PHONE_BOOK      = 2; // 获取已拨
    private static final int GET_MISSED_PHONE_BOOK      = 3; // 获取未接
    private static final int GET_ALL_HISTORY_PHONE_BOOK = 4; // 获取所有通话记录
    private TimerUtil mGetVCardTimerUtil = null;
    private static final int VCARD_TIMER_OUT = 30 * 1000; // 连续 VCARD_TIMER_OUT 没有数据过来，认为获取数据超时

    /**
     * 启动获取VCard超时的定时器
     */
    private void startGetVCardTimer() {
        if (null == mGetVCardTimerUtil) { // 获取VCard超时
            mGetVCardTimerUtil = new TimerUtil(new TimerUtil.TimerCallback() {
                @Override
                public void timeout() {
                    stopGetVCardTimer();
                    post(new IVIBluetooth.EventVCard(IVIBluetooth.EventVCard.ON_FAILURE,
                            IVIBluetooth.BluetoothExecErrorMsg.ERROR_TIMER_OUT, "", null)); // 超时
                }
            }, false);
        }
        mGetVCardTimerUtil.start(VCARD_TIMER_OUT);
    }

    /**
     * 停止定时器
     */
    private void stopGetVCardTimer() {
        if (null != mGetVCardTimerUtil) {
            mGetVCardTimerUtil.stop();
        }
    }

    /**
     * 执行VCardCmd
     * @param vCardListener
     */
    private void execVCardCmd(VCardListener vCardListener) {
        startGetVCardTimer(); // 开启检测定时器
        if (null != vCardListener) {
            mAppVCardCallback = vCardListener.callback;
            switch (vCardListener.type) {
                case GET_PHONE_CONTACTS: // 获取联系人
                    try {
                        mIBluetooth.getPhoneContacts(mIBluetoothVCardCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_RECEIVED_PHONE_BOOK: // 获取已接
                    try {
                        mIBluetooth.getReceivedCallRecord(mIBluetoothVCardCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_DIALED_PHONE_BOOK: // 获取已拨
                    try {
                        mIBluetooth.getDialedCallRecord(mIBluetoothVCardCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_MISSED_PHONE_BOOK: // 获取未接
                    try {
                        mIBluetooth.getMissedCallRecord(mIBluetoothVCardCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_ALL_HISTORY_PHONE_BOOK: // 获取所有通话记录
                    try {
                        mIBluetooth.getAllCallRecord(mIBluetoothVCardCallback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 获取蓝牙电话本
     * @param callback 结果通过callback，异步返回
     */
    public void getPhoneContacts(IBluetoothVCardCallback.Stub callback) {
        if (isSendToService(callback)) {
            putVCardCallback(GET_PHONE_CONTACTS, callback); // push 到命令列表
            if (mVCardListeners != null) {
                Logcat.d("mVCardListeners.size:" + mVCardListeners.size());
                if (mVCardListeners.size() == 1) {
                    execVCardCmd(getVCardCallback());
                }
            }
        }
    }

    /**
     * 获取已接通话记录
     * @param callback 结果通过callback，异步返回
     */
    public void getReceivedCallRecord(IBluetoothVCardCallback.Stub callback) {
        if (isSendToService(callback)) {
            putVCardCallback(GET_RECEIVED_PHONE_BOOK, callback); // push 到命令列表
            if (mVCardListeners != null) {
                if (mVCardListeners.size() == 1) {
                    execVCardCmd(getVCardCallback());
                }
            }
        }
    }

    /**
     * 获取已拨通话记录
     * @param callback 结果通过callback，异步返回
     */
    public void getDialedCallRecord(IBluetoothVCardCallback.Stub callback) {
        if (isSendToService(callback)) {
            putVCardCallback(GET_DIALED_PHONE_BOOK, callback); // push 到命令列表
            if (mVCardListeners != null) {
                if (mVCardListeners.size() == 1) {
                    execVCardCmd(getVCardCallback());
                }
            }
        }
    }

    /**
     * 获取未接通话记录
     * @param callback 结果通过callback，异步返回
     */
    public void getMissedCallRecord(IBluetoothVCardCallback.Stub callback) {
        if (isSendToService(callback)) {
            putVCardCallback(GET_MISSED_PHONE_BOOK, callback); // push 到命令列表
            if (mVCardListeners != null) {
                if (mVCardListeners.size() == 1) {
                    execVCardCmd(getVCardCallback());
                }
            }
        }
    }

    /**
     * 获取所有通话记录
     * @param callback 结果通过callback，异步返回
     */
    public void getAllCallRecord(IBluetoothVCardCallback.Stub callback) {
        if (isSendToService(callback)) {
            putVCardCallback(GET_ALL_HISTORY_PHONE_BOOK, callback); // push 到命令列表
            if (mVCardListeners != null) {
                if (mVCardListeners.size() == 1) {
                    execVCardCmd(getVCardCallback());
                }
            }
        }
    }

    /**
     * put 一个命令道堆栈
     * @param type 类型
     * @param callback
     */
    private void putVCardCallback(int type, IBluetoothVCardCallback.Stub callback) {
        VCardListener vCardListener = new VCardListener();
        vCardListener.callback = callback;
        vCardListener.type = type;
        if (mVCardListeners != null) {
            mVCardListeners.add(vCardListener);
        }
    }

    private VCardListener getVCardCallback() {
        if (mVCardListeners != null) {
            if (!ListUtils.isEmpty(mVCardListeners)) {
                return mVCardListeners.get(0);
            }
        }
        return null;
    }

    /*********************vCard 获取接口结束*****************************/

    /**
     * 获取查找的设备的总数
     * @param callback
     */
    public void getSearchedDeviceNum(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getSearchedDeviceNum(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定序号的设备的名字
     * @param sequence 序号
     * @param callback
     */
    public void getSearchedDeviceName(int sequence, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getSearchedDeviceName(sequence, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定序号的设备的地址
     * @param sequence 序号
     * @param callback
     */
    public void getSearchedDeviceAddr(int sequence, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getSearchedDeviceAddr(sequence, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 搜索设备的接口
    private ISearchDeviceCallback.Stub mISearchDeviceCallback = new ISearchDeviceCallback.Stub() {
        @Override
        public void onSuccess(BluetoothDevice btDevices) throws RemoteException {
            post(new EventSearchDevice(EventSearchDevice.SUCCESS_TYPE, btDevices));
        }

        @Override
        public void onProgress(BluetoothDevice btDevices) throws RemoteException {
            post(new EventSearchDevice(EventSearchDevice.PROGRESS_TYPE, btDevices));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventSearchDevice(EventSearchDevice.FAILUE_TYPE, errorCode));
        }
    };
    private ISearchDeviceCallback.Stub mAppSearchDeviceCallback = null; // 用户接口

    // 搜索设备的 event 类
    private class EventSearchDevice {
        public static final int SUCCESS_TYPE  = 0;
        public static final int PROGRESS_TYPE = 1;
        public static final int FAILUE_TYPE   = 2;
        public int mType;
        public int mErrorCode;
        public BluetoothDevice mBluetoothDevice;

        public EventSearchDevice(int type, int errorCode) {
            mType = type;
            mErrorCode = errorCode;
        }

        public EventSearchDevice(int type, BluetoothDevice btDevice) {
            mType = type;
            mBluetoothDevice = btDevice;
        }
    }

    /**
     * {@link BluetoothManager searchNewDevice}
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSearchDevice(EventSearchDevice event) {
        if (event != null && mAppSearchDeviceCallback != null) {
            try {
                switch (event.mType) {
                    case EventSearchDevice.SUCCESS_TYPE:
                        mAppSearchDeviceCallback.onSuccess(event.mBluetoothDevice);
                        break;
                    case EventSearchDevice.PROGRESS_TYPE:
                        mAppSearchDeviceCallback.onProgress(event.mBluetoothDevice);
                        break;
                    case EventSearchDevice.FAILUE_TYPE:
                        mAppSearchDeviceCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 搜索设备
     * @param deviceType 设备类型，该参数暂时未定义，直接传0就可以
     * @param callback 结果通过callback异步返回
     */
    public void searchNewDevice(int deviceType, ISearchDeviceCallback.Stub callback) {
        if (isSendToService(callback)) {
            mAppSearchDeviceCallback = callback;
            try {
                mIBluetooth.searchNewDevice(deviceType, mISearchDeviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取配对列表
    private IDeviceCallback.Stub mIDeviceCallback = new IDeviceCallback.Stub() {
        @Override
        public void onSuccess(List<BluetoothDevice> btDevices, BluetoothDevice curBluetoothDevice) throws RemoteException {
            post(new EventDeviceCallback(btDevices, curBluetoothDevice));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventDeviceCallback(errorCode));
        }
    };
    private IDeviceCallback.Stub mUserDeviceCallback;

    private static class EventDeviceCallback {
        public List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
        public BluetoothDevice mCurBluetoothDevice;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventDeviceCallback(List<BluetoothDevice> btDevices, BluetoothDevice btDevice) {
            mBluetoothDevices = btDevices;
            mCurBluetoothDevice = btDevice;
            mType = ON_SUCCESS;
        }

        public EventDeviceCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 获取已配对设备的EventBus回调通知，会在该方法回调 getPairedDevice 传入的 callback通知APP
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeviceCallback(EventDeviceCallback event) {
        if (event != null && mUserDeviceCallback != null) {
            try {
                switch (event.mType) {
                    case EventDeviceCallback.ON_SUCCESS:
                        mUserDeviceCallback.onSuccess(event.mBluetoothDevices, event.mCurBluetoothDevice);
                        break;

                    case EventDeviceCallback.ON_FAILURE:
                        mUserDeviceCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取已经配对的设备类型
     * @param deviceType
     * @param callback
     */
    public void getPairedDevice(int deviceType, IDeviceCallback.Stub callback) {
        if (isSendToService(callback)) {
            mUserDeviceCallback = callback;

            try {
                mIBluetooth.getPairedDevice(deviceType, mIDeviceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前设备的名字
     * @param callback
     */
    public void getCurrentDeviceName(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getCurrentDeviceName(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前设备的地址
     * @param callback
     */
    public void getCurrentDeviceAddr(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getCurrentDeviceAddr(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止当前命令
     * APP可以不要调用
     */
    public void stopCurrentCommand() {
        if (null != mIBluetooth) {
            try {
                mIBluetooth.stopCurrentCommand();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 异步获取蓝牙模组的名字
     * @param callback
     */
    public void getBluetoothName(IBluetoothExecCallback.Stub callback) {
        if (null != mIBluetooth) {
            mUserBluetoothNameCallback = callback;

            try {
                mIBluetooth.getBluetoothName(mBluetoothNameCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mBluetoothNameCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventBluetoothNameCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventBluetoothNameCallback(errorCode));
        }
    };

    private IBluetoothExecCallback.Stub mUserBluetoothNameCallback;

    private static class EventBluetoothNameCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventBluetoothNameCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventBluetoothNameCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    /**
     * 调用getBluetoothName该方法，异步获取蓝牙模块的名时，会通过该EventBus方法回调
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBluetoothNameCallback(EventBluetoothNameCallback event) {
        if (event != null && mUserBluetoothNameCallback != null) {
            try {
                switch (event.mType) {
                    case EventBluetoothNameCallback.ON_SUCCESS:
                        mUserBluetoothNameCallback.onSuccess(event.mMsg);
                        break;

                    case EventBluetoothNameCallback.ON_FAILURE:
                        mUserBluetoothNameCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步获取蓝牙模组的名字
     * @return 返回蓝牙模组名字
     */
    public String getBluetoothName() {
        if (null != mIBluetooth) {
            try {
                mIBluetooth.getBluetoothDeviceName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 异步获取蓝牙模组的pin码
     * @param callback
     */
    public void getBluetoothPin(IBluetoothExecCallback.Stub callback) {
        if (null != mIBluetooth) {
            mUserBluetoothPinCallback = callback;

            try {
                mIBluetooth.getBluetoothPin(mBluetoothPinCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mBluetoothPinCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {
            post(new EventBluetoothPinCallback(msg));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            post(new EventBluetoothPinCallback(errorCode));
        }
    };

    private IBluetoothExecCallback.Stub mUserBluetoothPinCallback;

    private static class EventBluetoothPinCallback {
        public String mMsg;
        public int mErrorCode;
        public int mType;

        public static final int ON_SUCCESS = 0;
        public static final int ON_FAILURE = 1;

        public EventBluetoothPinCallback(String msg) {
            mMsg = msg;
            mType = ON_SUCCESS;
        }

        public EventBluetoothPinCallback(int errorCode) {
            mErrorCode = errorCode;
            mType = ON_FAILURE;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBluetoothPinCallback(EventBluetoothPinCallback event) {
        if (event != null && mUserBluetoothPinCallback != null) {
            try {
                switch (event.mType) {
                    case EventBluetoothPinCallback.ON_SUCCESS:
                        mUserBluetoothPinCallback.onSuccess(event.mMsg);
                        break;

                    case EventBluetoothPinCallback.ON_FAILURE:
                        mUserBluetoothPinCallback.onFailure(event.mErrorCode);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步获取蓝牙模组的pin码
     * @return 直接返回蓝牙模组的pin码
     */
    public String getBluetoothPin() {
        if (null != mIBluetooth) {
            try {
                return mIBluetooth.getBluetoothDevicePin();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 设置蓝牙模块自动连接开关
     * @param on true 打开自动连接， false 关闭自动连接
     * @param callback
     */
    public void setAutoLink(boolean on, IBluetoothExecCallback.Stub callback){
        if (isSendToService(callback)){
            try {
                mIBluetooth.setAutoLink(on, callback);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取自动连接状态
     * @return true, 自动连接已开启；false, 自动连接未开启
     */
    public boolean isAutoLinkOn(){
        boolean autoLink = false;
        try {
            autoLink = mIBluetooth.isAutoLinkOn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return autoLink;
    }

    // AVRCP 控制指令 start
    /**
     * 下一首音乐
     * @param callback 执行结果返回
     */
    public void nextBtMusic(IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.nextBtMusic(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上一首音乐
     * @param callback 执行结果返回
     */
    public void prevBtMusic(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.prevBtMusic(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放暂停
     * @param callback 执行结果返回
     */
    public void playAndPause(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.playAndPause(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放蓝牙音乐
     * @param callback 执行结果返回
     */
    public void playBtMusic(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.playBtMusic(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停蓝牙音乐
     * @param callback 执行结果返回
     */
    public void pauseBtMusic(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.pauseBtMusic(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取蓝牙音乐，id3信息
     * @param callback 执行结果返回
     */
    public void getBtMusicId3Info(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.getBtMusicId3Info(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置蓝牙音量百分比
     * @param volume 值 0.0-1.0
     * @param callback 执行结果返回
     */
    public void setBtMusicVolumePercent(float volume, IBluetoothExecCallback.Stub callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.setBtMusicVolumePercent(volume, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止音乐
     * @param callback 回调通知是否成功
     */
    public void stopBtMusic(IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.stopBtMusic(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    // AVRCP 控制指令，停止

    /**
     * 是否可以发送数据给服务器
     * @param callback
     * @return
     */
    private boolean isSendToService(IInterface callback) {
        if (null == mIBluetooth) {
            CmdExecResultUtil.execError(callback, IVIBluetooth.BluetoothExecErrorMsg.ERROR_INITING);
            Logcat.d("return false because mIBluetooth == null ");
            return false;
        }

        if (!mModuleOpened) {
            CmdExecResultUtil.execError(callback, IVIBluetooth.BluetoothExecErrorMsg.ERROR_NOT_OPEN);
            Logcat.d("return false because mModuleOpened is false ");
            return false;
        }
        Logcat.d("return true ");
        return true;
    }

    /**
     * 蓝牙连接状态的EventBus通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLinkDevice(IVIBluetooth.EventLinkDevice event) {
        if (null != event) {
            Logcat.d("event.status:" + event.status);
            switch (event.status) {
                case IVIBluetooth.BluetoothConnectStatus.CONNECTFAIL: // 连接失败，或者连接成功，都停止定时器
                case IVIBluetooth.BluetoothConnectStatus.DISCONNECTED:
                    if (mVCardListeners != null) {
                        mVCardListeners.clear(); // 断开了，清空获取电话本，通话记录列表
                    }
                    // 继续往下执行
                case IVIBluetooth.BluetoothConnectStatus.CONNECTED:
                    stopLinkDeviceTimer();
                    stopGetVCardTimer();
                    break;
            }
            if (mAppIbluetoothCallback != null) {
                try {
                    mAppIbluetoothCallback.onConnectStatus(event.status, event.addr, event.name);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 蓝牙通话状态的EventBus通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPhoneStatus(IVIBluetooth.CallStatus event) {
        if (null != mAppIbluetoothCallback && null != event) {
            try {
                mAppIbluetoothCallback.onCallStatus(event.mStatus, event.mPhoneNumber, event.mContactName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙语音转换的状态的EventBus通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVoiceChange(IVIBluetooth.EventVoiceChange event) {
        if (null != mAppIbluetoothCallback && null != event) {
            try {
                mAppIbluetoothCallback.onVoiceChange(event.type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBluetoothExecCallback.Stub mDeleteDeviceCallback = null; // 删除设备的回调

    /**
     * 删除设备的event
     * 调用位置 deleteDevice onSuccess & onFailure
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDeleteDevice(IVIBluetooth.EventDeleteDevice event) {
        if (null != event && null != mDeleteDeviceCallback) {
            if (event.isSuccess) {
                CmdExecResultUtil.execSuccess(mDeleteDeviceCallback, event.msg);
            } else {
                CmdExecResultUtil.execError(mDeleteDeviceCallback, event.errorCode);
            }
        }
    }

    // 蓝牙联系人同步
    private IBluetoothVCardCallback.Stub mAppVCardCallback = null;

    // 实际传递给服务器的VCard回调
    private IBluetoothVCardCallback.Stub mIBluetoothVCardCallback = new IBluetoothVCardCallback.Stub() {
        @Override
        public void onProgress(List<BluetoothVCardBook> books) throws RemoteException {
            Logcat.d("books.size:" + books.size());
            startGetVCardTimer();
            post(new IVIBluetooth.EventVCard(IVIBluetooth.EventVCard.ON_PROGRESS, -1, "", books));
        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            stopGetVCardTimer();

            post(new IVIBluetooth.EventVCard(IVIBluetooth.EventVCard.ON_FAILURE, errorCode, "", null));
        }

        @Override
        public void onSuccess(String msg) throws RemoteException {
            stopGetVCardTimer();

            post(new IVIBluetooth.EventVCard(IVIBluetooth.EventVCard.ON_SUCCESS, -1, msg, null));
        }
    };

    /**
     * 电话本传递
     * 调用位置：mIBluetoothVCardCallback onProgress, onFailure, onSuccess
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVCard(IVIBluetooth.EventVCard event) {
        if (null != event && null != mAppVCardCallback) {
            switch (event.type) {
                case IVIBluetooth.EventVCard.ON_PROGRESS:
                    try {
                        mAppVCardCallback.onProgress(event.books);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case IVIBluetooth.EventVCard.ON_FAILURE:
                    try {
                        mAppVCardCallback.onFailure(event.errorCode);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (mVCardListeners != null) {
                        if (mVCardListeners.size() != 0) {
                            mVCardListeners.remove(0); // 执行完一条命令，从列表删除一个
                        }

                        Logcat.d("mVCardListeners.size:" + mVCardListeners.size());

                        // 执行下一条指令
                        if (mVCardListeners.size() != 0) {
                            execVCardCmd(getVCardCallback());
                        }
                    }
                    break;
                case IVIBluetooth.EventVCard.ON_SUCCESS:
                    try {
                        mAppVCardCallback.onSuccess(event.msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (mVCardListeners != null) {
                        if (mVCardListeners.size() != 0) {
                            mVCardListeners.remove(0); // 执行完一条命令，从列表删除一个
                        }

                        Logcat.d("mVCardListeners.size:" + mVCardListeners.size());

                        // 执行下一条指令
                        if (mVCardListeners.size() != 0) {
                            execVCardCmd(getVCardCallback());
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventModuleStatus(IVIBluetooth.EventModuleConnectStatus event) {
        if (mAppIbluetoothCallback != null && event != null) {
            try {
                mAppIbluetoothCallback.onA2DPConnectStatus(event.a2dpStatus, event.isStopped);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙音乐的 id3 信息 EventBus 通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMp3Id3Info(IVIBluetooth.EventMp3Id3Info event) {
        if (mAppIbluetoothCallback != null && event != null) {
            try {
                mAppIbluetoothCallback.onBtMusicId3Info(event.name, event.artist, event.album, event.duration);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 手机电量的EventBus通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBatteryValue(IVIBluetooth.EventBatteryValue event) {
        if (mAppIbluetoothCallback != null && event != null) {
            try {
                mAppIbluetoothCallback.onBtBatteryValue(event.value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 手机信号发生改变的EventBus通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSignalValue(IVIBluetooth.EventSignalValue event) {
        if (mAppIbluetoothCallback != null && event != null) {
            try {
                mAppIbluetoothCallback.onBtSignalValue(event.value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启连接设备的定时器
     */
    private void startLinkDeviceTimer() {
        if (mBtCommandTimerUtil == null) {
            mBtCommandTimerUtil = new TimerUtil(new TimerUtil.TimerCallback() {
                @Override
                public void timeout() {
                    mBtCommandTimerUtil.stop();

                    CmdExecResultUtil.execError(mBluetoothLinkDeviceCallback, IVIBluetooth.BluetoothExecErrorMsg.ERROR_TIMER_OUT);

                    // 连接超时
                    post(new IVIBluetooth.EventLinkDevice(
                            IVIBluetooth.BluetoothConnectStatus.CONNECTFAIL,
                            "",
                            ""
                    ));
                    Logcat.w("connect device time out!");
                }
            });
        }
        mBtCommandTimerUtil.start(BT_COMMAND_EXEC_TIME);
    }

    /**
     * 停止连接设备的定时器
     */
    private void stopLinkDeviceTimer() {
        if (mBtCommandTimerUtil != null) {
            mBtCommandTimerUtil.stop();
            mBtCommandTimerUtil = null;
        }
    }

    /**
     * 第三方通话时的操作接口
     * @param action {@link com.roadrover.sdk.bluetooth.IVIBluetooth.ThreePartyCallCtrl}
     * @param callback
     */
    public void threePartyCallCtrl(int action, IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.threePartyCallCtrl(action, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开蓝牙
     * APP尽量不要调用
     */
    public void powerOn() {
        if (mIBluetooth != null) {
            try {
                mIBluetooth.powerOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭蓝牙
     * APP尽量不要调用
     */
    public void powerOff() {
        if (mIBluetooth != null) {
            try {
                mIBluetooth.powerOff();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙开关状态
     */
    public boolean isPowerOn() {
        if (mIBluetooth != null) {
            try {
                return mIBluetooth.isPowerOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("mIBluetooth is null");
        }
        return false;
    }

    /**
     * 设置蓝牙铃声音量
     * @param percent 0.0f 最小，1.0f最大
     */
    public void setRingVolume(float percent) {
        if (mIBluetooth != null) {
            try {
                mIBluetooth.setRingVolume(percent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置蓝牙音乐静音，该接口部分平台可支持
     * @param isMute 是否静音
     */
    public void muteBtMusic(boolean isMute, IBluetoothExecCallback callback) {
        if (isSendToService(callback)) {
            try {
                mIBluetooth.muteBtMusic(isMute, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前地址是否已经配对，只要在配对列表，就会返回ture
     * @param address 蓝牙设备地址，可以是 00:11:22:33 这种带 : ,也可以不带
     * @return 判断是否已经在配对列表存在
     */
    public boolean isAlreadyPaired(String address) {
        if (mIBluetooth != null) {
            try {
                return mIBluetooth.isAlreadyPaired(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("return false because mIBluetooth == null ");
        }
        return false;
    }

    /**
     * 配对请求
     * 通过该方法去配对时，如果已经配对，则什么都不做
     * 如果配对的不是当前设备，则断开之前的设备
     * 如果正在配对中，等配对完成会判断是否是当前设备，如果不是断开，病例连接当前设备
     * @param address
     */
    public boolean pairingRequest(String address) {
        if (mIBluetooth != null) {
            try {
                return mIBluetooth.pairingRequest(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("mIBluetooth == null ");
        }
        return false;
    }

    /**
     * 打开录音
     * 目前只有lr181蓝牙模块支持该接口
     * 主要用于做调试用
     * @param callback
     */
    public void openRecording(IBluetoothExecCallback callback) {
        if (mIBluetooth != null) {
            try {
                mIBluetooth.openRecording(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("mIBluetooth == null ");
        }
    }

    /**
     * 关闭录音
     * @param callback
     */
    public void closeRecording(IBluetoothExecCallback callback) {
        if (mIBluetooth != null) {
            try {
                mIBluetooth.closeRecording(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("mIBluetooth == null ");
        }
    }
}
