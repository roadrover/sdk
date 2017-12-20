package com.roadrover.sdk.bluetooth;

import com.roadrover.btservice.bluetooth.BluetoothVCardBook;
import com.roadrover.sdk.utils.LogNameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙定义类
 * 包括命令执行的错误码，模块ID等等
 */

public class IVIBluetooth {
    /**
     * 蓝牙模块id定义
     */
    public static class ID {
        public static final int LR181 = 0; // 文强的LR181蓝牙模块
        public static final int BC5 = 1; // BC5蓝牙模块
        public static final int BA450 = 2; // 文强的BA450蓝牙模块
    }

    /**
     * 蓝牙命令的执行错误码
     */
    public static class BluetoothExecErrorMsg {

        /**
         * 未连接，其值为 {@value}
         */
        public static final int ERROR_DISCONNECT      = 1;

        /**
         * 正在初始化，其值为 {@value}
         */
        public static final int ERROR_INITING         = 2;

        /**
         * 参数错误，指令错误，其值为 {@value}
         */
        public static final int ERROR_PARAM           = 3;

        /**
         * 该电话本不支持下载，下载电话本时才有该错误，指令错误，其值为 {@value}
         */
        public static final int ERROR_PHONE_BOOK      = 4;

        /**
         * 电话本正在下载，指令错误，其值为 {@value}
         */
        public static final int ERROR_DOWNLOADING     = 5;

        /**
         * 串口错误，串口未打开成功，其值为 {@value}
         */
        public static final int ERROR_SERIAL_PORT     = 6;

        /**
         * 单条任务执行出错，其值为 {@value}
         */
        public static final int ERROR_TASK            = 7;

        /**
         * 已经打开，其值为 {@value}
         */
        public static final int ERROR_OPENED          = 8;

        /**
         * 查询时返回的数据错误，其值为 {@value}
         */
        public static final int ERROR_DATA            = 9;

        /**
         * 非通话状态，该错误主要是非通话状态做 DTMF的操作，其值为 {@value}
         */
        public static final int ERROR_NOT_INCALL      = 10;

        /**
         * 未打开，其值为 {@value}
         */
        public static final int ERROR_NOT_OPEN        = 11;

        /**
         * 模块不支持该命令，其值为 {@value}
         */
        public static final int ERROR_NOT_SUPPORT     = 12;

        /**
         * 获取数据超时，其值为 {@value}
         */
        public static final int ERROR_TIMER_OUT       = 13;

        /**
         * 连接其他设备时，当前如果是连接状态，不允许连接其他设备，其值为 {@value}
         */
        public static final int ERROR_CONNECTED       = 14;

        /**
         * 通话状态，不允许操作A2DP，其值为 {@value}
         */
        public static final int ERROR_TAKING          = 15;

        /**
         * 内部参数错误，其值为 {@value}
         */
        public static final int ERROR_INNER_PARAM     = 16;

        /**
         * 当前不允许连接蓝牙，一般可能为CarPlay状态，其值为 {@value}
         */
        public static final int ERROR_NO_ALLOW_LINK   = 17;

        /**
         * 不允许断开蓝牙，一般可能为AndroidAuto状态，其值为 {@value}
         */
        public static final int ERROR_NO_ALLOW_UNLINK = 18;

        /**
         * 蓝牙音乐未连接，在蓝牙音乐未连接时，直接调用控制蓝牙音乐的接口，其值为 {@value}
         */
        public static final int ERROR_BLUETOOTH_MUSIC_DISCONNECT = 19;

        /**
         * 非通话状态，操作接听，挂断等接口，其值为 {@value}
         */
        public static final int ERROR_NOT_TALKING     = 20;

        /**
         * 未知错误，其值为 {@value}
         */
        public static final int ERROR_UNKNOWN         = 99;// 未知错误

        /**
         * 通过type获取名字
         * @param type 例，传18，返回 "ERROR_NO_ALLOW_UNLINK"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, BluetoothExecErrorMsg.class);
        }
    }

    /**
     * 蓝牙连接状态
     */
    public static class BluetoothConnectStatus {

        /**
         * 已连接，其值为 {@value}
         */
        public static final int CONNECTED    = 1;

        /**
         * 未连接，其值为 {@value}
         */
        public static final int DISCONNECTED = 0;

        /**
         * 正在连接，其值为 {@value}
         */
        public static final int CONNECTING   = 2;

        /**
         * 连接失败，其值为 {@value}
         */
        public static final int CONNECTFAIL  = 3;

        public int mStatus; // 当前连接状态
        public String mAddr; // 地址
        public String mName; // 名字

        /**
         * 构造函数
         * @param status 当前连接状态
         */
        public BluetoothConnectStatus(int status) {
            mStatus = status;
        }

        /**
         * 构造函数
         * @param status 当前连接状态 {@link BluetoothConnectStatus}
         * @param addr 当前连接的设备地址
         * @param name 当前连接的设备名字
         */
        public BluetoothConnectStatus(int status, String addr, String name) {
            this(status);
            mAddr = addr;
            mName = name;
        }

        /**
         * 获取当前状态的名字
         * @return 例： "CONNECTED"
         */
        public String getName() {
            return getName(mStatus);
        }

        /**
         * 蓝牙是否是连接状态
         * @return 连接状态返回true，其他返回false
         */
        public boolean isConnected() {
            return mStatus == CONNECTED;
        }

        /**
         * 返回指定状态的名字，一般用于打印
         * @param status 例：1 返回 "CONNECTED"
         * @return
         */
        public static String getName(int status) {
            return LogNameUtil.getName(status, BluetoothConnectStatus.class);
        }

        @Override
        public String toString() {
            return getName() + " " + LogNameUtil.toString(this);
        }
    }

    /**
     * 蓝牙模组状态
     */
    public static class BluetoothModuleStatus {
        /**
         * hfp和a2dp都无效，其值为 {@value}
         */
        public static final int NONE_ACTIVE = 0;

        /**
         * hfp 有效，其值为 {@value}
         */
        public static final int HFP_ACTIVE  = 1;

        /**
         * a2dp 有效，其值为 {@value}
         */
        public static final int A2DP_ACTIVE = 2;

        /**
         * 通过类型返回名字
         * @param type 例：1 返回 "HFP_ACTIVE"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, BluetoothModuleStatus.class);
        }
    }

    /**
     * a2dp 的状态
     */
    public static class BluetoothA2DPStatus {
        /**
         * 正在初始化，其值为 {@value}
         */
        public static final int INITIALISING = 0;

        /**
         * 准备好了，其值为 {@value}
         */
        public static final int READY        = 1;

        /**
         * 启动，其值为 {@value}
         */
        public static final int INITIATING   = 2;

        /**
         * 已连接，其值为 {@value}
         */
        public static final int CONNECTED    = 3;

        /**
         * 正在播放，其值为 {@value}
         */
        public static final int STREAMING    = 4;

        /**
         * 通过类型返回名字
         * @param type 例： 1 返回 "READY"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, BluetoothA2DPStatus.class);
        }
    }

    /**
     * avrcp 的状态
     */
    public static class BluetoothAVRCPStatus {
        /**
         * 未连接，其值为 {@value}
         */
        public static final int DISCONNECTED = 0;

        /**
         * 连接中，其值为 {@value}
         */
        public static final int CONNECTING   = 1;

        /**
         * 已连接，其值为 {@value}
         */
        public static final int CONNECTED    = 2;

        /**
         * 通过类型返回名字，主要用于打印
         * @param type 例：1 返回 "CONNECTING"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, BluetoothAVRCPStatus.class);
        }
    }

    /**
     * hid 的状态
     */
    public static class BluetoothHIDStatus {

        /**
         * 未连接，其值为 {@value}
         */
        public static final int DISCONNECTED = 0;

        /**
         * 连接中，其值为 {@value}
         */
        public static final int CONNECTING   = 1;

        /**
         * 已连接，其值为 {@value}
         */
        public static final int CONNECTED    = 2;

        /**
         * 通过类型返回名字，主要用于打印
         * @param type 例：1 返回 "CONNECTING"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, BluetoothHIDStatus.class);
        }
    }

    /**
     * dtmf 指令集合 '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '0', '#'
     */
    public static class BluetoothDTMFCode {

        /**
         * DTMF 指令集合，值为 {@value}
         */
        public static final String[] DTMF_CODE = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"};
    }

    /**
     * 按键位置定义，{@link IVIBluetooth.BluetoothDTMFCode}
     */
    public static class KeyPositionDefined {

        /**
         * 1 的位置，值为 {@value}
         */
        public static final int ONE_POSITION      = 0;

        /**
         * 2 的位置，值为 {@value}
         */
        public static final int TWO_POSITION      = 1;

        /**
         * 3 的位置，值为 {@value}
         */
        public static final int THREE_POSITION    = 2;

        /**
         * 4 的位置，值为 {@value}
         */
        public static final int FOUR_POSITION     = 3;

        /**
         * 5 的位置，值为 {@value}
         */
        public static final int FIVE_POSITION     = 4;

        /**
         * 6 的位置，值为 {@value}
         */
        public static final int SIX_POSITION      = 5;

        /**
         * 7 的位置，值为 {@value}
         */
        public static final int SEVEN_POSITION    = 6;

        /**
         * 8 的位置，值为 {@value}
         */
        public static final int EIGHT_POSITION    = 7;

        /**
         * 9 的位置，值为 {@value}
         */
        public static final int NINE_POSITION     = 8;

        /**
         * "*" 的位置，值为 {@value}
         */
        public static final int ASTERISK_POSITION = 9;

        /**
         * 0 的位置，值为 {@value}
         */
        public static final int ZERO_POSITION     = 10;

        /**
         * # 的位置，值为 {@value}
         */
        public static final int POUND_POSITION    = 11;

        /**
         * 通过key值获取定义的名字，一般用于打印log用
         * @param key 例： 10 返回 "ZERO_POSITION"
         * @return
         */
        public static String getName(int key) {
            return LogNameUtil.getName(key, KeyPositionDefined.class);
        }
    }

    /**
     * 声音源状态
     */
    public static class BluetoothAudioTransferStatus {
        /**
         * 声音在手机端，其值为 {@value}
         */
        public static final int PHONE_STATUS = 0;
        /**
         * 声音在车机端，其值为 {@value}
         */
        public static final int HF_STATUS    = 1;

        /**
         * 通过status获取定义的名字，一般用于打印log
         * @param status 例： 1 返回 "HF_STATUS"
         * @return
         */
        public static String getName(int status) {
            return LogNameUtil.getName(status, BluetoothAudioTransferStatus.class);
        }
    }

    /**
     * 通话记录状态
     */
    public static class BluetoothCallHistoryStatus {

        /**
         * 未接，其值为 {@value}
         */
        public static final int MISS_STATUS    = 0;

        /**
         * 已拨，其值为 {@value}
         */
        public static final int CALLED_STATUS  = 1;

        /**
         * 已接，其值为 {@value}
         */
        public static final int LISTEN_STATUS  = 2;

        /**
         * 未知类型，其值为 {@value}
         */
        public static final int UNKNOWN_STATUS = 3;

        /**
         * 通过定义的类型获取名字，一般用于打印log
         * @param status 例： 2 返回 "LISTEN_STATUS"
         * @return
         */
        public static final String getName(int status) {
            return LogNameUtil.getName(status, BluetoothCallHistoryStatus.class);
        }
    }

    /**
     * 删除设备的Event类
     */
    public static class EventDeleteDevice {

        /**
         * 是否成功
         */
        public boolean isSuccess;

        /**
         * 错误码
         */
        public int errorCode;

        /**
         * 消息体
         */
        public String msg;

        /**
         * 删除设备构造函数
         * @param isSuccess 是否成功
         * @param errorCode 错误码，{@link BluetoothExecErrorMsg}
         * @param msg 消息体
         */
        public EventDeleteDevice(boolean isSuccess, int errorCode, String msg) {
            this.isSuccess = isSuccess;
            this.errorCode = errorCode;
            this.msg = msg;
        }
    }

    /**
     * 来电状态切换定义类
     */
    public static class EventVoiceChange {
        public int type;

        /**
         * 构造函数
         * @param type {@link BluetoothAudioTransferStatus}
         */
        public EventVoiceChange(int type) {
            this.type = type;
        }
    }

    /**
     * 通话状态定义类
     */
    public static class CallStatus {

        /**
         * 正常状态，其值为 {@value}
         */
        public static final int NORMAL         = 0;

        /**
         * 来电状态，其值为 {@value}
         */
        public static final int INCOMING       = 1;

        /**
         * 去电状态，其值为 {@value}
         */
        public static final int OUTGOING       = 2;

        /**
         * 挂断电话状态，其值为 {@value}
         */
        public static final int HANGUP         = 3;

        /**
         * 通话中状态，其值为 {@value}
         */
        public static final int TALKING        = 4;

        /**
         * 第三方通话来电状态，其值为 {@value}
         */
        public static final int THREE_INCOMING = 5;

        /**
         * 保留通话状态，其值为 {@value}
         */
        public static final int RETAIN         = 6;

        /**
         * 第三方通话去电状态，其值为 {@value}
         */
        public static final int THREE_OUTGOING = 7;

        /**
         * 第三方通话通话中状态，其值为 {@value}
         */
        public static final int THREE_TALKING  = 8;

        /**
         * 第三方通话挂断状态，其值为 {@value}
         */
        public static final int THREE_HANGUP = 9;

        /**
         * 蓝牙是否正在使用，非常态和非挂断状态都认为蓝牙正在使用，需要发出声音
         * @param status {@link CallStatus}
         * @return 是返回true
         */
        public static boolean isBluetoothSound(int status) {
            return status != NORMAL && status != HANGUP;
        }

        /**
         * 是否为三方通话状态
         * @param status {@link CallStatus}
         * @return 是返回true
         */
        public static boolean isThreeTalking(int status) {
            return (THREE_INCOMING == status || THREE_OUTGOING == status || THREE_TALKING == status);
        }

        /**
         * 通过状态获取名字，一般用于打印
         * @param status 例：5 返回 "THREE_INCOMING"
         * @return
         */
        public static String getName(int status) {
            return LogNameUtil.getName(status, CallStatus.class, "CallStatus " + status);
        }

        /**
         * 电话号码
         */
        public String mPhoneNumber;

        /**
         * 联系人名字
         */
        public String mContactName;

        /**
         * 当前通话状态
         */
        public int mStatus;

        /**
         * 该条记录是否插入数据库
         */
        public boolean mInsertSql;

        /**
         * 构造函数，使用该构造函数，默认会将这条数据插入数据库中
         * @param status 通话状态
         * @param phoneNumber 电话号码
         */
        public CallStatus(int status, String phoneNumber) {
            this(status, phoneNumber, true);
        }

        /**
         * 构造函数，使用该构造函数，默认会将这条数据插入数据库中
         * @param status 通话状态
         * @param phoneNumber 电话号码
         * @param contactName 联系人名字
         */
        public CallStatus(int status, String phoneNumber, String contactName) {
            this(status, phoneNumber);
            mContactName = contactName;
        }

        /**
         * 构造函数
         * @param status 通话状态
         * @param phoneNumber 电话号码
         * @param insertSql 是否插入数据库
         */
        public CallStatus(int status, String phoneNumber, boolean insertSql) {
            mStatus = status;
            mPhoneNumber = phoneNumber;
            mInsertSql = insertSql;
        }

        /**
         * 构造函数
         * @param status 通话状态
         * @param phoneNumber 电话号码
         * @param insertSql 是否插入数据库
         * @param contactName 联系人名字
         */
        public CallStatus(int status, String phoneNumber, boolean insertSql, String contactName) {
            this(status, phoneNumber, insertSql);
            mContactName = contactName;
        }

        /**
         * 判断当前状态，蓝牙是否在使用
         * @return
         */
        public boolean isBluetoothSound() {
            return CallStatus.isBluetoothSound(mStatus);
        }

        /**
         * 判断当前是否是通话状态
         * <b>注：三方通话也会认为是通话状态</br>
         * @return
         */
        public boolean isTalking() {
            return mStatus == TALKING || mStatus == OUTGOING ||
                    mStatus == THREE_TALKING || mStatus == THREE_OUTGOING ||
                    mStatus == THREE_INCOMING || mStatus == RETAIN; // 去电状态也认为是通话状态，第三方通话所有状态都认为是通话状态
        }

        /**
         * 是否是响铃状态
         * <b>注：只有单方来电才算响铃状态，三方状态的来电状态，不算做响铃状态，而算做通话状态</br>
         * @return
         */
        public boolean isRinging() {
            return mStatus == INCOMING;
        }

        /**
         * 获取当前状态的名字，一般用作打印
         * @return
         */
        public String getName() {
            return CallStatus.getName(mStatus);
        }

        @Override
        public String toString() {
            return getName() + " " + LogNameUtil.toString(this);
        }
    }

    /**
     * 蓝牙模块状态，包括连接状态， hf状态 以及 a2dp状态
     */
    public static class EventModuleConnectStatus {
        public int a2dpStatus;
        public boolean isStopped;

        /**
         * 蓝牙模块连接状态的构造函数
         * @param a2dpStatus {@link BluetoothA2DPStatus}
         * @param isStoped 是否停止
         */
        public EventModuleConnectStatus(int a2dpStatus, boolean isStoped) {
            this.a2dpStatus = a2dpStatus;
            this.isStopped = isStoped;
        }
    }

    /**
     * 连接设备的Event对象
     */
    public static class EventLinkDevice {
        public int status;
        public String addr;
        public String name;

        /**
         * 构造函数
         * @param status 状态 {@link BluetoothConnectStatus}
         * @param addr 连接的地址
         * @param name 连接的名字
         */
        public EventLinkDevice(int status, String addr, String name) {
            this.status = status;
            this.addr = addr;
            this.name = name;
        }

        /**
         * 当前是否是连接状态
         * @return
         */
        public boolean isConnected() {
            return status == BluetoothConnectStatus.CONNECTED;
        }

        @Override
        public String toString() {
            return BluetoothConnectStatus.getName(status) + " " + LogNameUtil.toString(this);
        }
    }

    /**
     * 同步蓝牙联系人 Event 类
     * 同时包括，通话记录也是通过该类
     * <b>发送步骤，成功，ON_PROGRESS -> ON_SUCCESS</br>
     * <b>失败，ON_PROGRESS -> ON_FAILUE</br>
     */
    public static class EventVCard {

        /**
         * 进度，值为{@value}
         */
        public static final int ON_PROGRESS = 0;

        /**
         * 获取失败，值为{@value}
         */
        public static final int ON_FAILURE  = 1;

        /**
         * 获取成功，值为{@value}
         */
        public static final int ON_SUCCESS  = 2;

        /**
         * 进度，失败，成功
         * 值为：ON_PROGRESS,ON_FAILURE,ON_SUCCESS
         */
        public int type;

        /**
         * VCard列表
         */
        public List<BluetoothVCardBook> books = new ArrayList<>();

        /**
         * 错误码 {@link BluetoothExecErrorMsg}
         */
        public int errorCode;

        /**
         * 消息体
         */
        public String msg;

        /**
         * 构造函数
         * @param type 类型，{@link EventVCard}
         * @param errorCode 错误码，{@link BluetoothExecErrorMsg}
         * @param msg 消息体
         * @param books 电话本或者通话记录列表
         */
        public EventVCard(int type, int errorCode, String msg, List<BluetoothVCardBook> books) {
            if (null != books) {
                this.books.addAll(books);
            }
            this.type = type;
            this.errorCode = errorCode;
            this.msg = msg;
        }
    }

    /**
     * 蓝牙音乐 id3 信息
     */
    public static class EventMp3Id3Info {

        /**
         * 歌曲名
         */
        public String name;

        /**
         * 歌手名
         */
        public String artist;

        /**
         * 专辑名
         */
        public String album;

        /**
         * 总时长
         */
        public long duration;

        /**
         * 构造函数
         * @param name 歌曲名
         * @param artist 歌手名
         * @param album 专辑名
         * @param duration 总时长
         */
        public EventMp3Id3Info(String name, String artist, String album, long duration) {
            this.name = name;
            this.artist = artist;
            this.album = album;
            this.duration = duration;
        }

        public String toString() {
            return LogNameUtil.toString(this);
        }
    }

    /**
     * 三方通话控制定义
     */
    public static class ThreePartyCallCtrl {

        /**
         * 挂断第三方，其值为 {@value}
         * <b>注：如果是三方来电状态，调用挂断第三方，则挂断来电的这个电话，因为来电的这个为第三方</br>
         * <b>如果是三方通话状态，挂断第三方，则挂断的是当前非接听（保持）的这个电话</b>
         */
        public static final int ACTION_HANGUP_THREE = 0;

        /**
         * 挂断当前通话，其值为 {@value}
         * <b>注：如果是三方来电状态，调用挂断当前通话，挂断的是后台接听的电话，而非来电的这个，并且，此时会接听第三方来电</br>
         * <b>如果当前是三方通话状态，调用挂断当前通话，则挂断的是当前通话这个电话</b>
         */
        public static final int ACTION_HANGUP_CUR   = 1;

        /**
         * 接听第三方，保留当前的通话，其值为 {@value}
         * <b>试用于第三方来电时</br>
         */
        public static final int ACTION_ANSWER_THREE = 2;

        /**
         * 合并通话，在接听三方通话之后，将三方的通话合并成一个通话，其值为 {@value}
         * <b>必须手机卡要支持合并通话才可以使用合并通话功能</br>
         */
        public static final int ACTION_MERGE_CALL   = 3;

        /**
         * 切换通话，接听第三方通话好，在保留的电话和当前通话的电话之间进行切换，其值为 {@value}
         */
        public static final int ACTION_SWITCH_CALL  = 4;

        /**
         * 获取指定类型的名字，一般用于打印log
         * @param type 例： 2 返回 "ACTION_ANSWER_THREE"
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, ThreePartyCallCtrl.class);
        }
    }

    /**
     * 手机电量的Event类
     */
    public static class EventBatteryValue {
        public int value;

        /**
         * 构造函数
         * @param value 电量 0-100
         */
        public EventBatteryValue(int value) {
            this.value = value;
        }
    }

    /**
     * 手机信号的Event类
     */
    public static class EventSignalValue {
        public int value;

        /**
         * 构造
         * @param value 0-5
         */
        public EventSignalValue(int value) {
            this.value = value;
        }
    }

    /**
     * 自动连接的Event类
     */
    public static class EventAutoLink {
        public boolean value;

        /**
         * 构造
         * @param value 0-5
         */
        public EventAutoLink(boolean value) {
            this.value = value;
        }
    }

    /**
     * 蓝牙开关的Event类
     */
    public static class EventPowerState {
        public boolean value;

        /**
         * 构造
         * @param value
         */
        public EventPowerState(boolean value) {
            this.value = value;
        }
    }
}
