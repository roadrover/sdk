// IBluetoothExecCallback.aidl
package com.roadrover.btservice.bluetooth;

// 蓝牙命令执行结果

interface IBluetoothExecCallback {

    /**
     * 蓝牙命令执行成功
     * @param msg 执行成功的消息，有些命令是查询设备名等等，通过该参数返回
     */
    void onSuccess(String msg);

    /**
     * 蓝牙命令执行失败
     * @param errorCode 对应 IVIBluetooth.BluetoothExecErrorMsg 里面的错误码
     */
    void onFailure(int errorCode);
}
