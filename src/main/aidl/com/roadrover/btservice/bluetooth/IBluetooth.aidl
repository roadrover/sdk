// IBluetooth.aidl
package com.roadrover.btservice.bluetooth;

import com.roadrover.btservice.bluetooth.IBluetoothCallback;
import com.roadrover.btservice.bluetooth.IBluetoothExecCallback;
import com.roadrover.btservice.bluetooth.IBluetoothLinkDeviceCallback;
import com.roadrover.btservice.bluetooth.IBluetoothStatusCallback;
import com.roadrover.btservice.bluetooth.IBluetoothVCardCallback;
import com.roadrover.btservice.bluetooth.IDeviceCallback;
import com.roadrover.btservice.bluetooth.ISearchDeviceCallback;

// 蓝牙模块的接口类，所有调用回调，全部通过 IBluetoothCallback 回调通知，不直接返回
// 由于执行指令全部在线程中执行，不直接返回执行结果

interface IBluetooth {

    /**
     * 获取蓝牙的版本
     */
    void getBluetoothVer(IBluetoothExecCallback callback);

    /**
     * 打开蓝牙模块
     */
    int openBluetoothModule(IBluetoothExecCallback callback);

    /**
     * 重置蓝牙模块
     */
    void resetBluetoothModule(IBluetoothExecCallback callback);

    /**
     * 关闭蓝牙模块
     * @param moduleid 蓝牙模块id
     */
    void closeBluetoothModule(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙模块的状态
     */
    void getBluetoothModuleStatus(IBluetoothStatusCallback callback);

    /**
     * 修改模块名字
     * @param moduleid 模块id
     * @param newName 新名字
     */
    void modifyModuleName(String newName, IBluetoothExecCallback callback);

    /**
     * 修改模块 pin 码
     * @param moduleid 模块id
     * @param newPin 新 pin 码
     */
    void modifyModulePIN(String newPin, IBluetoothExecCallback callback);

    // AVRCP控制指令 boolean sendAVRCPCommand(int moduleid, int command);
    /**
     * 下一曲
     * @param moduleid 模块id
     */
    void nextBtMusic(IBluetoothExecCallback callback);

    /**
     * 上一曲
     * @param moduleid 模块id
     */
    void prevBtMusic(IBluetoothExecCallback callback);

    /**
     * 播放，暂停
     * @param moduleid 模块id
     */
    void playAndPause(IBluetoothExecCallback callback);

    /**
     * 播放蓝牙音乐
     */
    void playBtMusic(IBluetoothExecCallback callback);

    /**
     * 暂停蓝牙音乐
     */
    void pauseBtMusic(IBluetoothExecCallback callback);

    /**
     * 停止音乐
     */
    void stopBtMusic(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙音乐 id3 信息，数据直接通过 onBtMusicId3Info 回调返回
     */
    void getBtMusicId3Info(IBluetoothExecCallback callback);

    /**
     * 设置蓝牙音量的百分比
     * @param volume 音量百分比
     */
    void setBtMusicVolumePercent(float volume, IBluetoothExecCallback callback);
    // AVRCP控制指令结束

    /**
     * 连接指定设备
     * @param moduleid 模块id
     * @param addr 目标设备地址
     */
    void linkDevice(String addr, IBluetoothExecCallback callback);

    /**
     * 断开设备，断开HFP，同时会断开A2DP
     * @param moduleid 模块id
     */
    void unlinkDevice(IBluetoothExecCallback callback);

    /**
     * 删除指定设备
     * @param moduleid 模块id
     * @param addr 设备地址
     */
    void deleteDevice(String addr, IBluetoothExecCallback callback);

    /**
     * 从蓝牙列表中删除所有设备
     */
    void deleteAllDevice(IBluetoothExecCallback callback);

    /**
     * 改变音量，增加音量
     * @param moduleid
     */
    void addVolume(IBluetoothExecCallback callback);

    /**
     * 降低音量
     * @param moduleid
     */
    void delVolume(IBluetoothExecCallback callback);

    /**
     * 静音蓝牙模块
     * @param isMute 是否静音
     */
    void muteBluetoothModule(boolean isMute, IBluetoothExecCallback callback);

    /**
     * 拨打电话
     * @param moduleid
     * @param phnum 电话号码
     */
    void callPhone(String phnum, IBluetoothExecCallback callback);

    /**
     * 重拨指令
     */
    void recallPhone(IBluetoothExecCallback callback);

    /**
     * 挂断电话
     */
    void hangPhone(IBluetoothExecCallback callback);

    /**
     * 来电拒接
     */
    void rejectPhone(IBluetoothExecCallback callback);

    /**
     * 来电接听
     */
    void listenPhone(IBluetoothExecCallback callback);

    /**
     * 语音拨号指令
     */
    void voiceDial(IBluetoothExecCallback callback);

    /**
     * 语音切换，切换手机接听还是车机接听
     */
    void transferCall(IBluetoothExecCallback callback);

    /**
     * 等待接听，该接口暂时无用
     */
    void waitCall(int type, IBluetoothExecCallback callback);

    /**
     * 发送按键指令
     * @param moduleid
     * @param code 按键值，对应IVIBluetooth.BluetoothDTMFCode 中的定义
     */
    void requestDTMF(int code, IBluetoothExecCallback callback);

    /**
     * 静掉mic,连续调用会在静音和解静音之间进行操作
     */
    void muteMic(boolean isMute, IBluetoothExecCallback callback);

    /**
     * 获取设备电量
     */
    void getDeviceBattery(IBluetoothExecCallback callback);

    /**
     * 获取信号强度
     * @param moduleid
     */
    void getDeviceSignalStrength(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙状态，状态会通过回调通知
     */
    void getBluetoothState(IBluetoothLinkDeviceCallback callback);

    /**
     * 连接A2DP
     * @param isConnected 是否连接
     */
    void requestA2DPConnect(boolean isConnected, IBluetoothExecCallback callback);

    /**
     * 连接上一个设备, 连接HFP协议
     * @param isConnected 是否连接
     */
    void requestHFPConnect(boolean isConnected, IBluetoothExecCallback callback);

    /**
     * 设置蓝牙的监听回调
     */
    void requestBluetoothListener(in IBluetoothCallback listener);

    /**
     * 注销蓝牙的监听回调
     */
    void unrequestBluetoothListener(in IBluetoothCallback listener);

    /**
     * 获取蓝牙电话本，结果通过callback返回
     * @param moduleid 模块id
     */
    void getPhoneContacts(IBluetoothVCardCallback callback);

    /**
     * 获取已接通话记录，结果通过callback回调返回
     * @param moduleid 模块id
     * @param callback 回调
     */
    void getReceivedCallRecord(IBluetoothVCardCallback callback);

    /**
     * 下载已播的通话记录，结果通过callback回调返回
     */
    void getDialedCallRecord(IBluetoothVCardCallback callback);

    /**
     * 获取未接的通话记录
     */
    void getMissedCallRecord(IBluetoothVCardCallback callback);

    /**
     * 获取所有的通话记录
     */
    void getAllCallRecord(IBluetoothVCardCallback callback);

    /**
     * 获取查找的设备的总数
     * @param moduleid 模块id
     */
    void getSearchedDeviceNum(IBluetoothExecCallback callback);

    /**
     * 获取查找的设备的名字
     * @param moduleid 模块id
     * @param sequence 设备序列号
     */
    void getSearchedDeviceName(int sequence, IBluetoothExecCallback callback);

    /**
     * 获取查找的设备地址
     * @param moduleid 模块id
     * @param sequence 设备序列号
     */
    void getSearchedDeviceAddr(int sequence, IBluetoothExecCallback callback);

    /**
     * 搜索设备
     * @param moduleid 蓝牙模块id
     * @param devicetype 设备类型
     * @param callback 通过该接口获取数据
     */
    void searchNewDevice(int devicetype, ISearchDeviceCallback callback);

    /**
     * 获取已经配置的设备列表
     * @param moduleid 蓝牙模块id
     * @param devicetype 设备类型
     * @param callback 通过该接口获取数据
     */
    void getPairedDevice(int devicetype, IDeviceCallback callback);

    /**
     * 获取当前设备的设备名
     * @param moduleid 模块id
     */
    void getCurrentDeviceName(IBluetoothExecCallback callback);

    /**
     * 获取当前设备的设备地址
     * @param moduleid 模块id
     */
    void getCurrentDeviceAddr(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙的名字，异步获取，结果通过 callback 回调返回
     * @param callback
     */
    void getBluetoothName(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙名字，同 getBluetoothName 方法，同步获取
     */
    String getBluetoothDeviceName();

    /**
     * 获取蓝牙的pin码，异步获取，结果通过 callback 回调返回
     * @param callback
     */
    void getBluetoothPin(IBluetoothExecCallback callback);

    /**
     * 获取蓝牙pin码，同 getBluetoothPin 方法
     */
    String getBluetoothDevicePin();

    /**
     * 停止当前命令
     */
    void stopCurrentCommand();

    /**
     * 第三方通话控制
     * @param action {@link com.roadrover.sdk.bluetooth.IVIBluetooth.ThreePartyCallCtrl}
     * @param callback
     */
    void threePartyCallCtrl(int action, IBluetoothExecCallback callback);

    /**
    * 打开蓝牙
    */
    void powerOn();

    /**
    * 关闭蓝牙
    */
    void powerOff();

    /**
     * 设置蓝牙铃声音量
     * @param percent 0.0f 最小，1.0f最大
     */
    void setRingVolume(float percent);

    /**
    * 设置蓝牙音乐静音
    */
    void muteBtMusic(boolean isMute, IBluetoothExecCallback callback);

    /**
     * 是否已经配对
     * @param address 地址：
     */
    boolean isAlreadyPaired(String address);

    /**
     * 配对请求 </br>
     * 判断地址，如果和已经连接的手机地址相同直接返回，否则要断开蓝牙连接，等待配对</br>
     */
    boolean pairingRequest(String address);

    /**
     * 打开录音 </br>
     * 调用该接口后，会将通话声音录下来 </br>
     * <b>lr181蓝牙模块才支持该接口，录音到的文件保存在 /data/goc/下面 </br>
     * mic原始音频 : audio_prim.pcm </br>
     * 处理后音频 : audio_proc.pcm </b></br>
     */
    void openRecording(IBluetoothExecCallback callback);

    /**
     * 关闭录音
     */
    void closeRecording(IBluetoothExecCallback callback);

    /**
    * 设置自动连接
    * @param autoLink
    */
    void setAutoLink(boolean autoLink, IBluetoothExecCallback callback);

    /**
    * 判断自动连接是否开启
    */
    boolean isAutoLinkOn();

    /**
    * 判断蓝牙开关是否开启
    */
    boolean isPowerOn();
}
