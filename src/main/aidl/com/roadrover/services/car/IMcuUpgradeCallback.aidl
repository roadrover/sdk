// IMcuUpgradeCallback.aidl
package com.roadrover.services.car;

// mcu 升级回调

interface IMcuUpgradeCallback {

    /**
     * 整个升级过程成功
     */
    void onSuccess(String mcuVersion);

    /**
     * 等待mcu重启
     */
    void onWaitMcuReboot();

    /**
     * 升级失败
     * @param errorCode 对应 IVICar.McuUpgradeErrorMsg 定义
     */
    void onFailure(int errorCode);

    /**
     * 升级进度
     * @param progress 进度值
     */
    void onProgress(int progress);
}
