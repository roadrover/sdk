package com.roadrover.sdk.bluetooth;

import android.os.IInterface;
import android.os.RemoteException;

import com.roadrover.btservice.bluetooth.BluetoothDevice;
import com.roadrover.btservice.bluetooth.BluetoothVCardBook;
import com.roadrover.btservice.bluetooth.IBluetoothExecCallback;
import com.roadrover.btservice.bluetooth.IBluetoothLinkDeviceCallback;
import com.roadrover.btservice.bluetooth.IBluetoothStatusCallback;
import com.roadrover.btservice.bluetooth.IBluetoothVCardCallback;
import com.roadrover.btservice.bluetooth.IDeviceCallback;
import com.roadrover.btservice.bluetooth.ISearchDeviceCallback;

import java.util.List;

/**
 * 命令执行返回工具类
 * 该类主要是Services回调给sdk时使用
 */

public class CmdExecResultUtil {

    /**
     * 执行出错，通知应用
     * @param callback 应用回调
     * @param errorCode 错误码
     */
    public static void execError(IInterface callback, int errorCode) {
        if (null != callback) {
            try {
                if (callback instanceof IBluetoothExecCallback) { // 执行命令
                    ((IBluetoothExecCallback) callback).onFailure(errorCode);
                } else if (callback instanceof IDeviceCallback) { // 查找设备
                    ((IDeviceCallback) callback).onFailure(errorCode);
                } else if (callback instanceof IBluetoothLinkDeviceCallback) { // 连接设备
                    ((IBluetoothLinkDeviceCallback) callback).onFailure(errorCode);
                } else if (callback instanceof IBluetoothVCardCallback) { // 获取蓝牙电话本
                    ((IBluetoothVCardCallback) callback).onFailure(errorCode);
                } else if (callback instanceof IBluetoothStatusCallback) { // 获取蓝牙状态
                    ((IBluetoothStatusCallback) callback).onFailure(errorCode);
                } else if (callback instanceof ISearchDeviceCallback) { // 搜索蓝牙设备
                    ((ISearchDeviceCallback) callback).onFailure(errorCode);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行查询设备列表成功
     * @param callback
     * @param btDevices 蓝牙列表
     * @param curBluetoothDevices 当前连接的蓝牙设备
     */
    public static void execSearchDeviceListSuccess(IDeviceCallback callback, List<BluetoothDevice> btDevices, BluetoothDevice curBluetoothDevices) {
        if (null != callback) {
            try {
                callback.onSuccess(btDevices, curBluetoothDevices);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行连接设备成功
     * @param callback
     * @param status 连接设备状态
     * @param addr 目标地址
     * @param name 目标名字
     */
    public static void execLinkeDeviceSuccess(IBluetoothLinkDeviceCallback callback, int status, String addr, String name) {
        if (null != callback) {
            try {
                callback.onSuccess(status, addr, name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 命令执行成功
     * @param callback 回调
     * @param msg 消息
     */
    public static void execSuccess(IBluetoothExecCallback callback, String msg) {
        if (null != callback) {
            try {
                callback.onSuccess(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行命令成功
     * @param msg
     */
    public static void execSuccess(IBluetoothVCardCallback callback, String msg) {
        if (null != callback) {
            try {
                callback.onSuccess(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询状态命令执行成功
     * @param callback
     * @param status 状态 {@link IVIBluetooth.BluetoothModuleStatus}
     * @param hfpStatus hf的状态 {@link IVIBluetooth.BluetoothHIDStatus}
     * @param avStatus  a2dp的状态 {@link IVIBluetooth.BluetoothA2DPStatus}
     */
    public static void execGetBluetoothStatusSuccess(IBluetoothStatusCallback callback, int status, int hfpStatus, int avStatus) {
        if (null != callback) {
            try {
                callback.onSuccess(status, hfpStatus, avStatus);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取电话本，通话记录等信息执行进度回调
     * @param callback
     * @param books 下载进度
     */
    public static void execGetECardProgress(IBluetoothVCardCallback callback, List<BluetoothVCardBook> books) {
        if (null != callback) {
            try {
                callback.onProgress(books);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
