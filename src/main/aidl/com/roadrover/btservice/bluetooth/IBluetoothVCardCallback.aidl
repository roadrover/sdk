// IBluetoothPhoneBookCallback.aidl.aidl
package com.roadrover.btservice.bluetooth;

import com.roadrover.btservice.bluetooth.BluetoothVCardBook;

// 蓝牙通讯录查询的接口类

interface IBluetoothVCardCallback {

    /**
     * 电话本下载进度
     * @param books 下载进度，根据模块不同，可以不同，一次十条左右
     */
    void onProgress(in List<BluetoothVCardBook> books);

    /**
     * 蓝牙命令执行失败
     * @param errorCode 错误码 {@link IVIBluetooth.BluetoothExecErrorMsg}
     */
    void onFailure(int errorCode);

    /**
     * 蓝牙电话本下载成功
     */
    void onSuccess(String msg);
}
