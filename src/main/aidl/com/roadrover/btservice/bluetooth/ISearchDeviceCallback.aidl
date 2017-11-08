// IDeviceCallback.aidl
package com.roadrover.btservice.bluetooth;

import com.roadrover.btservice.bluetooth.BluetoothDevice;

// 查找设备列表

interface ISearchDeviceCallback {

    /**
     * 执行成功，直接返回设备列表
     * @param bluetoothDevices 设备列表
     */
    void onSuccess(in BluetoothDevice bluetoothDevices);

    /**
     * 搜索设备
     */
    void onProgress(in BluetoothDevice bluetoothDevices);

    /**
     * 蓝牙查找设备执行失败
     * @param errorCode {@link IVIBluetooth.BluetoothExecErrorMsg} 里面的错误码
     */
    void onFailure(int errorCode);
}
