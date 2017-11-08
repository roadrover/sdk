package com.roadrover.btservice.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 蓝牙设备对象 </br>
 *   1) 用户调用 searchNewDevice 接口时，返回该对象 </br>
 *   2) 用户调用 getPairedDevice 接口时，返回该对象列表 </br>
 */

public class BluetoothDevice implements Parcelable {

    public String name = "";
    public String addr = "";

    protected BluetoothDevice(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(addr);
    }

    public void readFromParcel(Parcel source) {
        if (null != source) {
            name = source.readString();
            addr = source.readString();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BluetoothDevice> CREATOR = new Creator<BluetoothDevice>() {
        @Override
        public BluetoothDevice createFromParcel(Parcel in) {
            return new BluetoothDevice(in);
        }

        @Override
        public BluetoothDevice[] newArray(int size) {
            return new BluetoothDevice[size];
        }
    };

    /**
     * 创建一个蓝牙设备
     * @param addr
     * @param name
     * @return
     */
    public static BluetoothDevice createDevice(String addr, String name) {
        BluetoothDevice device = CREATOR.createFromParcel(null);
        device.name = name;
        device.addr = addr;
        return device;
    }

    @Override
    public boolean equals(Object o) {
        if (null == o || !(o instanceof BluetoothDevice)) {
            return false;
        }
        BluetoothDevice bluetoothDevice = (BluetoothDevice) o;
        if (bluetoothDevice == this || TextUtils.equals(bluetoothDevice.addr, addr)) {
            return true;
        }
        return false;
    }
}
