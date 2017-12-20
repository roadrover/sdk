// IBluetoothCallback.aidl
package com.roadrover.btservice.bluetooth;

// 蓝牙数据接口回调

interface IBluetoothCallback {

    /**
     * 在连接过程中，断开，连接等都会调用该状态
     */
    void onConnectStatus(int nStatus, String addr, String name);

    /**
     * 电话状态回调
     * @param nStatus IVIBluetooth.BluetoothCallStatus
     * @param phoneNumber 只有来电的状态才会有phoneNumber
     * @param contactName 联系人名字，有可能没有
     */
    void onCallStatus(int nStatus, String phoneNumber, String contactName);

    /**
     * 语音切换
     * @param type 语音切换在哪一端 IVIBluetooth.BluetoothAudioTransferStatus
     */
    void onVoiceChange(int type);

    /**
     * 状态查询命令执行成功
     * @param avstatus IVIBluetooth.BluetoothA2DPStatus
     * @param isStoped 是否停止
     */
    void onA2DPConnectStatus(int avstatus, boolean isStoped);

    /**
     * 蓝牙音乐 id3 信息
     * @param name     名字
     * @param artist   歌手
     * @param album    专辑
     * @param duration 总时长
     */
    void onBtMusicId3Info(String name, String artist, String album, long duration);

    /**
    * 手机电量
    * @param value     电量值 0-100
    */
    void onBtBatteryValue(int value);

    /**
    * 手机信号
    * @param value     信号值 0-5
    */
    void onBtSignalValue(int value);

    /**
    * 蓝牙开关状态
    * @param value
    */
    void onPowerStatus(boolean value);
}
