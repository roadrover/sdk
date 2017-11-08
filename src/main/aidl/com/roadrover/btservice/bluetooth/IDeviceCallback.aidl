// IDeviceCallback.aidl
package com.roadrover.btservice.bluetooth;

import com.roadrover.btservice.bluetooth.BluetoothDevice;

// 获取配对设备回调

interface IDeviceCallback {

    /**
     * 执行成功，直接返回设备列表
     * @param bluetoothDevices 设备列表
     * @param curBluetoothDevices 当前连接的蓝牙设备
     */
    void onSuccess(in List<BluetoothDevice> bluetoothDevices, in BluetoothDevice curBluetoothDevices);

    /**
     * 蓝牙查找设备执行失败
     * @param errorCode 对应{@link IVIBluetooth.BluetoothExecErrorMsg} 里面的错误码
     */
    void onFailure(int errorCode);
}
