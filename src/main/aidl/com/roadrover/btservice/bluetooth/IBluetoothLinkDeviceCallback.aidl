// IBluetoothLinkDeviceCallback.aidl
package com.roadrover.btservice.bluetooth;

// 蓝牙连接设备的回调

interface IBluetoothLinkDeviceCallback {

    /**
     * 调用linkDevice 成功
     * @param status 对应 IVIBluetooth.BuletoothConnectStats 状态
     * @param addr 配对装置的蓝牙地址
     * @param name 配对装置的蓝牙名称
     */
    void onSuccess(int status, String addr, String name);

    /**
     * 蓝牙命令执行失败
     * @param errorCode 对应 IVIBluetooth.BluetoothExecErrorMsg 里面的错误码
     */
    void onFailure(int errorCode);
}
