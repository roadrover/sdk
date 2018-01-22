// ISystem.aidl
package com.roadrover.services.system;

import com.roadrover.services.system.IGpsCallback;
import com.roadrover.services.system.ISystemCallback;

// 设置需要使用的aidl文件

interface ISystem {

    /**
     * 设置屏幕亮度
     * @param id {@link com.roadrover.sdk.setting.IVISetting.ScreenBrightnessId}
     * @param brightness 屏幕亮度级别
     */
    void setScreenBrightness(int id, int brightness);

    /**
     * 获取当前屏幕亮度
     * @param id {@link com.roadrover.sdk.setting.IVISetting.ScreenBrightnessId}
     */
    int getScreenBrightness(int id);

    /**
     * 获取最大的屏幕亮度，最小为0
     */
    int getMaxScreenBrightness();

    /**
     * 获取屏幕亮度调节为白天还是黑夜
     */
    int getCurrentBrightnessId();

    /**
     * 注册监听设置回调
     * @param callback
     * @param packageName 当前应用的包名
     */
    void registerSystemCallback(ISystemCallback callback, String packageName);

    /**
     * 注销监听设置回调
     */
    void unRegisterSystemCallback(ISystemCallback callback);

    /**
     * 开始打印日志到文件
     */
    void startPrintLogcat(String filePath);

    /**
     * 停止打印
     */
    void stopPrintLogcat();

    /**
     * 重启
     */
    void reboot();

    /**
     * 关屏
     */
    void closeBackLight();

    /**
     * 点亮屏幕
     */
    void openBackLight();

    /**
     * 屏幕是否是关闭
     */
    boolean isOpenBackLight();

    /**
     * 恢复出厂设置
     * @param isClearINand 是否清理本机
     */
    void recoverySystem(boolean isClearINand);

    /**
     * 关闭指定App
     */
    void closeApp(String packageName);

    /**
     * 关闭所有App
     */
    void closeAllApp();

    /**
     * 获取正在运行的列表，SystemUI获取
     * @param historyPackages 历史列表
     */
    List<String> getNotRunActivityPackageNames(in List<String> historyPackages);

    /**
     * 打开记忆的app
     */
    void openMemoryApp();

    /**
     * 获取记忆的app包名
     */
    String getMemoryAppPackageName();

    /**
     * 打开导航应用，供其他应用想打开导航使用
     */
    void openNaviApp();

    /**
     * 重启mute动作，在上位机进行自重启时，需要进行mute动作，防止爆音
     */
    void resetMute();

    /**
     * 监听 gps 数据
     */
    void registerGpsLocationInfoListener(IGpsCallback callback);

    /**
     * 注销监听 gps 数据
     */
    void unregisterGpsLocationInfoListener(IGpsCallback callback);

    /**
     * 是否已经定位
     */
    boolean isInPosition();

    /**
     * 启用/禁用TP触摸
     */
    void setTPTouch(boolean enable);

    /**
     * 打开还是关闭操作
     * @param isOpen {@link com.roadrover.sdk.system.IVISystem.EventTboxOpen}
     */
    void setTboxOpen(boolean isOpen);

    /**
     * 获取T-box开关状态值
     */
    boolean isTboxOpen();

}
