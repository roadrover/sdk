package com.roadrover.sdk.system;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.services.system.IGpsCallback;
import com.roadrover.services.system.ISystem;
import com.roadrover.services.system.ISystemCallback;
import com.roadrover.sdk.utils.Logcat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置的管理类
 */
public class SystemManager extends BaseManager {

    private ISystem mSystemInterface = null; // 系统设置接口类

    private SystemUpgrade mSystemUpgrade = null; // 系统升级类，需要移入服务内

    public SystemManager(Context context, ConnectListener listener) {
        super(context, listener, true);

        mSystemUpgrade = new SystemUpgrade(context);
    }

    @Override
    public void disconnect() {
        mSystemUpgrade = null;
        mISettingCallback = null;
        mGpsCallback = null;
        mUserGpsCallback = null;

        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.SYSTEM_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mSystemInterface = ISystem.Stub.asInterface(service);

        // 如果服务挂了，重启，必须在该位置重新注册回调，否则会没反应
        registerSystemCallback();

        // 用户已经设过 gps 监听
        if (mUserGpsCallback != null) {
            registerGpsListener();
        }
    }

    @Override
    protected void onServiceDisconnected() {
        mSystemInterface = null;
    }

    /**
     * 设置屏幕亮度，范围为0 ~ getMaxScreenBrightness()
     * @param id {@link com.roadrover.sdk.system.IVISystem.ScreenBrightnessId}
     * @param brightness
     */
    public void setScreenBrightness(int id, int brightness) {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.setScreenBrightness(id, brightness);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取屏幕亮度
     * @param id {@link com.roadrover.sdk.system.IVISystem.ScreenBrightnessId}
     * @return
     */
    public int getScreenBrightness(int id) {
        if (mSystemInterface != null) {
            try {
                return mSystemInterface.getScreenBrightness(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取屏幕最大亮度
     * @return
     */
    public int getMaxScreenBrightness() {
        if (mSystemInterface != null) {
            try {
                return mSystemInterface.getMaxScreenBrightness();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取屏幕亮度调节为白天还是黑夜
     * @return
     */
    public int getCurrentBrightnessId() {
        if (mSystemInterface != null) {
            try {
                return mSystemInterface.getCurrentBrightnessId();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return IVISystem.ScreenBrightnessId.UNKNOWN;
    }

    /**
     * 注册监听设置回调
     */
    public void registerSystemCallback(ISystemCallback.Stub callback) {
        if (callback != null) {
            registerCallback(callback);
        }
    }

    /**
     * 注销监听设置回调
     */
    public void unRegisterSystemCallback(ISystemCallback.Stub callback) {
        if (callback != null) {
            unRegisterCallback(callback);
        }
    }

    /**
     * 开始打印日志 </br>
     * @param filePath 打印到指定目录，会在指定目录下打三个文件，分别是logcat，内核，收音机的打印
     */
    public void startPrintLogcat(String filePath) {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.startPrintLogcat(filePath);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止打印日志
     */
    public void stopPrintLogcat() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.stopPrintLogcat();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重启
     */
    public void reboot() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.reboot();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 亮屏
     */
    public void openBackLight() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.openBackLight();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭屏幕
     */
    public void closeBackLight() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.closeBackLight();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 屏幕是否是打开
     * @return
     */
    public boolean isOpenBackLight() {
        if (mSystemInterface != null) {
            try {
                return mSystemInterface.isOpenBackLight();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // 设置回调
    private ISystemCallback.Stub mISettingCallback = new ISystemCallback.Stub() {
        @Override
        public void onOpenScreen() throws RemoteException {
            post(new IVISystem.EventScreenOperate(IVISystem.EventScreenOperate.OPEN_TYPE));
        }

        @Override
        public void onCloseScreen(int from) throws RemoteException {
            post(new IVISystem.EventScreenOperate(IVISystem.EventScreenOperate.CLOSE_TYPE, from));
        }

        @Override
        public void onScreenBrightnessChange(int id, int brightness) throws RemoteException {
            post(new IVISystem.EventScreenBrightnessChange(id, brightness));
        }

        @Override
        public void onCurrentScreenBrightnessChange(int id, int brightness) throws RemoteException {
            post(new IVISystem.EventCurrentScreenBrightnessChange(id, brightness));
        }

        @Override
        public void quitApp() {
            post(new IVISystem.EventQuitApp());
        }

        @Override
        public void startNavigationApp() {
            post(new IVISystem.EventStartNavigationApp());
        }

        @Override
        public void onMediaAppChanged(String packageName, boolean isOpen) {
            post(new IVISystem.EventMediaAppChanged(packageName, isOpen));
        }

        @Override
        public void gotoSleep() {
            post(new IVISystem.EventGoToSleep());
        }

        @Override
        public void wakeUp() {
            post(new IVISystem.EventWakeUp());
        }

        @Override
        public void onFloatBarVisibility(int visibility) {
            post(new IVISystem.EventFloatBarVisibility(visibility));
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenOperate(IVISystem.EventScreenOperate event) {
        if (event != null) {
            for (IInterface callback : mICallbackS) {
                if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                    try {
                        if (event.mType == IVISystem.EventScreenOperate.OPEN_TYPE) {
                            ((ISystemCallback.Stub) callback).onOpenScreen();
                        } else {
                            ((ISystemCallback.Stub) callback).onCloseScreen(event.mFrom);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenBrightnessChange(IVISystem.EventScreenBrightnessChange event) {
        if (event != null) {
            for (IInterface callback : mICallbackS) {
                if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                    try {
                        ((ISystemCallback.Stub) callback).onScreenBrightnessChange(event.mId, event.mBrightness);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrentScreenBrightnessChange(IVISystem.EventCurrentScreenBrightnessChange event) {
        if (event != null) {
            for (IInterface callback : mICallbackS) {
                if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                    try {
                        ((ISystemCallback.Stub) callback).onCurrentScreenBrightnessChange(event.mId, event.mBrightness);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventQuitApp(IVISystem.EventQuitApp event) {
        for (IInterface callback : mICallbackS) {
            if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                try {
                    ((ISystemCallback.Stub) callback).quitApp();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventStartNavigationApp(IVISystem.EventStartNavigationApp event) {
        for (IInterface callback : mICallbackS) {
            if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                try {
                    ((ISystemCallback.Stub) callback).startNavigationApp();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGoToSleep(IVISystem.EventGoToSleep event) {
        for (IInterface callback : mICallbackS) {
            if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                try {
                    ((ISystemCallback.Stub) callback).gotoSleep();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGoToSleep(IVISystem.EventWakeUp event) {
        for (IInterface callback : mICallbackS) {
            if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                try {
                    ((ISystemCallback.Stub) callback).wakeUp();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFloatBarVisibility(IVISystem.EventFloatBarVisibility event) {
        for (IInterface callback : mICallbackS) {
            if (callback instanceof ISystemCallback.Stub) { // 回调应用通知
                try {
                    ((ISystemCallback.Stub) callback).onFloatBarVisibility(event.mVisibility);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 系统升级 </br>
     * @param filePath 升级文件
     * @param listener 升级状态，见{@link IVISystem.UpgradeStatus}
     * @return true表示执行升级动作成功，false表示升级校验动作失败
     */
    public boolean upgradePackage(String filePath, IVISystem.ProgressListener listener) {
        boolean ret = false;
        if (null != mSystemUpgrade) {
            ret = mSystemUpgrade.upgradePackage(filePath, listener);
        }
        return ret;
    }

    /**
     * 取消系统升级 </br>
     * 在进入 recovery 之前可以取消，后续就不能取消 </br>
     * @return true成功打断升级，false打断失败或升级动作未执行
     */
    public boolean cancelUpgradePackage() {
        boolean ret = false;
        if (null != mSystemUpgrade) {
            ret = mSystemUpgrade.cancelUpgradePackage();
        }
        return ret;
    }

    /**
     * 恢复出厂设置
     * @param isClearINand 是否在恢复出厂设置的时候去清除INand
     */
    public void recoverySystem(boolean isClearINand) {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.recoverySystem(isClearINand);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭指定App
     * @param packageName 包名
     */
    public void closeApp(String packageName) {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.closeApp(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭所有App
     */
    public void closeAllApp() {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.closeAllApp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取没有运行的App列表，该接口一般是给SystemUI使用
     * @param historyPackages 传入历史包名列表
     * @return 返回historyPackages中，没有运行的app列表，用于给systemUI做过滤
     */
    public List<String> getNotRunActivityPackageNames(List<String> historyPackages) {
        if (null != mSystemInterface) {
            try {
                return mSystemInterface.getNotRunActivityPackageNames(historyPackages);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 该接口给SystemUi调用，其他应用不要调用
     */
    public void openMemoryApp() {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.openMemoryApp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前记忆的APP的包名
     * @return APP包名
     */
    public String getMemoryAppPackageName() {
        if (null != mSystemInterface) {
            try {
                return mSystemInterface.getMemoryAppPackageName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 该接口给其他应用调用，打开导航
     */
    public void openNaviApp() {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.openNaviApp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重启mute动作，在上位机进行自重启时，需要进行mute动作，防止爆音
     */
    public void resetMute() {
        if (null != mSystemInterface) {
            try {
                mSystemInterface.resetMute();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * gps 回调监听
     */
    private IGpsCallback.Stub mGpsCallback = new IGpsCallback.Stub() {

        @Override
        public void onGpsLocationInfoChanged(String longitude, String latitude, float accuracy, double altitude, float fSpeed) {
            post(new IVISystem.EventGpsChanged(longitude, latitude, accuracy, altitude, fSpeed));
        }

        @Override
        public void onGpsCountChanged(int iGpsInView, int iGpsInUse, int iGlonassInView, int iGLonassInUse) {
            post(new IVISystem.EventGpsCountChanged(iGpsInView, iGpsInUse, iGlonassInView, iGLonassInUse));
        }
    };
    private IGpsCallback.Stub mUserGpsCallback = null;

    /**
     * 监听 gps 信息变化
     * @param callback
     */
    public void setGpsInfoListener(IGpsCallback.Stub callback) {
        mUserGpsCallback = callback;
        if (callback != null) {
            if (mSystemInterface != null) {
                registerGpsListener();
            }
        } else {
            unregisterGpsListener();
        }
    }

    /**
     * 当前是否已经定位
     * @return 定位返回true，否则返回false
     */
    public boolean isInPosition() {
        if (mSystemInterface != null) {
            try {
                return mSystemInterface.isInPosition();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 启用/禁用TP触摸
     * @return
     */
    public void setTPTouch(boolean enable) {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.setTPTouch(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * GPS数据发生改变的 Event监听方法
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGpsChanged(IVISystem.EventGpsChanged event) {
        if (mUserGpsCallback != null) {
            try {
                mUserGpsCallback.onGpsLocationInfoChanged(event.mLongitude,
                        event.mLatitude, event.mAccuracy, event.mAltitude, event.mSpeed);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * GPS数量发生改变的Event监听方法
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGpsCountChanged(IVISystem.EventGpsCountChanged event) {
        if (mUserGpsCallback != null) {
            try {
                mUserGpsCallback.onGpsCountChanged(event.mGpsInView,
                        event.mGpsInUse, event.mGlonassInView, event.mGlonassInUse);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监听gps回调
     */
    private void registerGpsListener() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.registerGpsLocationInfoListener(mGpsCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注销gps监听
     */
    private void unregisterGpsListener() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.unregisterGpsLocationInfoListener(mGpsCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册system回调
     */
    private void registerSystemCallback() {
        if (mSystemInterface != null && mContext != null) {
            try {
                mSystemInterface.registerSystemCallback(mISettingCallback, mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("mSystemInterface = " + mSystemInterface + " mContext = " + mContext);
        }
    }

    /**
     * 注销system回调
     */
    private void unRegisterSystemCallback() {
        if (mSystemInterface != null) {
            try {
                mSystemInterface.unRegisterSystemCallback(mISettingCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
