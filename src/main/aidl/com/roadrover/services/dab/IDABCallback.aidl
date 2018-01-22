// IDABCallback.aidl
package com.roadrover.services.dab;

interface IDABCallback {

    /**
     * 系统状态通知
     *
     * @param systemState 见{@link com.roadrover.sdk.dab.IVIDAB.SystemState}
     */
    void onSystemNotification(int systemState);
}
