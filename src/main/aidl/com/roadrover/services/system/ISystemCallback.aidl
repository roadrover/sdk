// ISystemCallback.aidl
package com.roadrover.services.system;

// 设置的回调

interface ISystemCallback {

    /**
     * 打开屏幕
     */
    void onOpenScreen();

    /**
     * 关闭屏幕
     * @param from {@link IVISystem.EventScreenOperate} 是应用调用还是系统调用关闭屏幕
     */
    void onCloseScreen(int from);

    /**
     * 屏幕亮度发生改变
     * @param id {@link com.roadrover.sdk.setting.IVISetting.ScreenBrightnessId}
     * @param brightness 亮度
     */
    void onScreenBrightnessChange(int id, int brightness);

    /**
     * 屏幕亮度ID发生改变
     * @param id {@link com.roadrover.sdk.setting.IVISetting.ScreenBrightnessId}
     * @param brightness 亮度
     */
    void onCurrentScreenBrightnessChange(int id, int brightness);

    /**
     * 退出应用
     */
    void quitApp();

    /**
     * 启动导航app
     */
    void startNavigationApp();

    /**
     * 媒体app发生改变
     * @param packageName 当前是什么媒体在操作
     * @param isOpen 打开还是关闭操作
     */
    void onMediaAppChanged(String packageName, boolean isOpen);

    /**
     * 系统被休眠
     */
    void gotoSleep();

    /**
     * 系统被唤醒
     */
    void wakeUp();

    /**
     * 该接口竖屏项目才用到
     * 用来控制中间的悬浮窗显示或者隐藏
     * @param visibility 对应 View.GONE View.VISIBILITY 等参数
     */
    void onFloatBarVisibility(int visibility);
}
