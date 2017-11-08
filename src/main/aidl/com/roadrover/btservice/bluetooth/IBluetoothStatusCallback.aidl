// IBluetoothStatusCallback.aidl
package com.roadrover.btservice.bluetooth;

// 蓝牙状态查询回调接口

interface IBluetoothStatusCallback {

    /**
     * 状态查询命令执行成功
     * @param status 状态 IVIBluetooth.BluetoothModuleStatus
     * @param hfpstatus  IVIBluetooth.BluetoothHIDStatus
     * @param a2dpStatus IVIBluetooth.BluetoothA2DPStatus
     */
    void onSuccess(int status, int hfpStatus, int a2dpStatus);

    /**
     * 蓝牙命令执行失败
     * @param errorCode 对应 IVIBluetooth.BluetoothExecErrorMsg 里面的错误码
     */
    void onFailure(int errorCode);
}
