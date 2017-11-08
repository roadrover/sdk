package com.roadrover.btservice.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 蓝牙电话本，通话记录, 传递数据类
 */

public class BluetoothVCardBook implements Parcelable {

    /**
     * 编码类型
     */
    public String codingType;

    /**
     * 名字
     */
    public String name;

    /**
     * 类型，已拨或者未接等
     */
    public String type;

    /**
     * 电话号码
     */
    public String phoneNumber;

    /**
     * 通话记录，通话时间，时间为 20170109T211652  2017/01/09 21:16:52
     */
    public String callTime;

    protected BluetoothVCardBook(Parcel in) {
        readFromParcel(in);
    }

    /**
     * 创建电话本工具类
     * @param codingType 编码类型
     * @param name 名字
     * @param type 类型 已拨或者未接等
     * @param phoneNumber 电话号码
     * @param callTime 通话记录，通话时间
     * @return
     */
    public static BluetoothVCardBook createVCardBook(String codingType, String name, String type, String phoneNumber, String callTime) {
        BluetoothVCardBook book = CREATOR.createFromParcel(null);
        book.codingType = codingType;
        book.name = name;
        book.type = type;
        book.phoneNumber = phoneNumber;
        book.callTime = callTime;
        return book;
    }

    public static final Creator<BluetoothVCardBook> CREATOR = new Creator<BluetoothVCardBook>() {
        @Override
        public BluetoothVCardBook createFromParcel(Parcel in) {
            return new BluetoothVCardBook(in);
        }

        @Override
        public BluetoothVCardBook[] newArray(int size) {
            return new BluetoothVCardBook[size];
        }
    };

    @Override
    public String toString() {
        return "coding:" + codingType + " name:" + name +
                " type:" + type + " number:" + phoneNumber +
                " time:" + callTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(codingType);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(phoneNumber);
        parcel.writeString(callTime);
    }

    public void readFromParcel(Parcel source) {
        if (null != source) {
            codingType = source.readString();
            name = source.readString();
            type = source.readString();
            phoneNumber = source.readString();
            callTime = source.readString();
        }
    }

    /**
     * 判断两条记录是否是同一条记录
     * @param o
     * @return 名字一样，并且电话号码一样，并且通话时间一样，认为是同一条，同一条返回 true
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o instanceof BluetoothVCardBook) {
            BluetoothVCardBook other = (BluetoothVCardBook) o;
            if (TextUtils.equals(name, other.name) &&
                    TextUtils.equals(phoneNumber, other.phoneNumber)
                    && TextUtils.equals(callTime, other.callTime)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (name != null) {
            return name.hashCode();
        }
        return 0;
    }
}
