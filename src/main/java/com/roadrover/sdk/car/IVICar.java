package com.roadrover.sdk.car;

import android.text.TextUtils;

import com.roadrover.sdk.utils.LogNameUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IVICar {
    public static final int RPM_UNKNOWN = -1;
    public static final float DISTANCE_UNKNOWN = -1.0f;
    public static final float SPEED_UNKNOWN = -1.0f;
    public static final float TIME_UNKNOWN = -1.0f;
    public static final float FUEL_CONSUMPTION_UNKNOWN = -1.0f;
    public static final float BATTERY_VOLTAGE_UNKNOWN = -1.0f;
    public static final int OUTSIDE_TEMP_UNKNOWN = 0xFF;
    public static final int INSIDE_TEMP_UNKNOWN = 0xFF;
    public static final float FUEL_L_LOW = -2.0f;
    public static final float FUEL_L_UNKNOWN = -1.0f;
    public static final float MAX_WHEEL_ANGLE = 38.0f;
    public static final float MAX_STEERING_WHEEL_ANGLE = 200.0f;
    public static final float CLEAN_WATER_OK = 0.0f;
    public static final float CLEAN_WATER_LOW = 1.0f;
    public static final float SEAT_BELT_OK = 0.0f;
    public static final float SEAT_BELT_RELEASE = 1.0f;

    /**
     * MCU协议卡的版本号
     */
    public static class EventMcuVersion {
        public String mVersion;

        public EventMcuVersion(String version) {
            mVersion = version;
        }
    }

    /**
     * 温度单位
     */
    public static class TemperatureUnit {
        public static final int C = 0;
        public static final int F = 1;

        public static float F2C(float tempF) {
            return (tempF - 32.0f) / 1.8f;
        }

        public static float C2F(float tempC) {
            return 1.8f * tempC + 32.0f;
        }

        public static String getString(int tempUnit) {
            return (tempUnit == C) ? "℃" : "℉";
        }
    }

    /**
     * 油耗信息
     */
    public static class FuelConsumptionUnit {
        public static final int LP100KM = 0;      // L/100km
        public static final int LPH = 1;          // L/H
        public static final int MPG = 2;          // Mile per Gallon
    }

    /**
     * 高频实时车辆信息，使用者只有主动注册需要该信息，则在该信息变化的时候才能获取回调
     */
    public static class RealTimeInfo {
        public static class Id {
            public static final int SPEED = 1;                    // 实时速度
            public static final int FUEL_CONSUMPTION = 2;         // 瞬时油耗
            public static final int ENGINE_RPM = 3;               // 发动机转速
            public static final int WHEEL_ANGLE = 4;              // 转向车轮转角
            public static final int RADAR = 5;                    // 雷达，注意雷达数据通过Radar传送
            public static final int SPEED_GPS = 6;                // 实时速度，通过GPS来获取的
            public static final int FUEL_CONSUMPTION_MINUTE = 7;  // 分钟油耗，一分钟发一次
        }

        public int mId;
        public float mValue;

        public RealTimeInfo(int id, float value) {
            mId = id;
            mValue = value;
        }
    }

    /**
     * 车辆其他参数
     */
    public static class ExtraState {
        public static class Id {
            public static final int BATTERY_VOLTAGE = 0;        // 电池电压
            public static final int REMAIN_FUEL = 1;            // 剩余油量L,
            public static final int REMAIN_FUEL_DISTANCE = 2;   // 剩余油量里程km
            public static final int GEAR = 3;                   // 档位信息，参考Gear里面的定义
            public static final int CLEAN_WATER = 4;            // 玻璃水
            public static final int SEAT_BELT = 5;              // 安全带
        }

        public int mId;
        public float mValue;

        public ExtraState(int id, float value) {
            mId = id;
            mValue = value;
        }

        public static float getUnknownValue(int id) {
            return -1.0f;
        }
    }

    /**
     * 车辆档位
     */
    public static class Gear {
        public static final int UNKNOWN = -1;
        public static final int P = 0;
        public static final int R = 1;
        public static final int N = 2;
        public static final int D = 3;
        public static final int S = 4;
        public static final int D1 = 5;
        public static final int D2 = 6;
        public static final int D3 = 7;
        public static final int D4 = 8;
        public static final int D5 = 9;
        public static final int D6 = 10;
        public static final int L = 11;
        public static final int M1 = 16;
        public static final int M2 = 17;
        public static final int M3 = 18;
        public static final int M4 = 19;
        public static final int M5 = 20;
        public static final int M6 = 21;

        public static String getName(int id) {
            return LogNameUtil.getName(id, Gear.class);
        }
    }

    /**
     * 车门
     */
    public static class Door {
        public static class Id {
            public static final int FRONT_LEFT          = 0;    // 前左
            public static final int FRONT_RIGHT         = 1;    // 前右
            public static final int REAR_LEFT           = 2;    // 后左
            public static final int REAR_RIGHT          = 3;    // 后右
            public static final int HOOT                = 4;    // 发动机盖
            public static final int FUEL_CAP            = 5;    // 油箱盖
            public static final int TRUNK               = 6;    // 后尾箱盖
            public static final int REAR_WIND_SHIELD    = 7;    // 后风窗
            public static final int BUS_LUGGAGE         = 9;    // 大巴行李箱门
            public static final int BUS_MID             = 10;   // 大巴中门
            public static final int COUNT               = 11;   // 总车门数
        }

        public int mChangeMask;
        public int mStatusMask;

        public Door(int changeMask, int statusMask) {
            mChangeMask = changeMask;
            mStatusMask = statusMask;
        }

        public boolean isChanged(int id) {
            return ((1 << id) & mChangeMask) != 0;
        }

        public boolean isOpened(int id) {
            return ((1 << id) & mStatusMask) != 0;
        }

        public boolean isHaveOpened() {
            return mStatusMask != 0;
        }

        public boolean isOpening(int id) {
            return isChanged(id) && isOpened(id);
        }

        public boolean isClosing(int id) {
            return isChanged(id) && !isOpened(id);
        }
    }

    /**
     * 车灯信息
     */
    public static class Light {
        public static class Id {
            public static final int TURN_LEFT = 0;  // 左转向灯
            public static final int TURN_RIGHT = 1; // 右转向灯
            public static final int MARKER = 2;     // 示廓灯
            public static final int EMERGENCY = 3;  // 紧急灯（双闪）
            public static final int FRONT_FOG = 4;  // 前雾灯
            public static final int REAR_FOG = 5;   // 后雾灯
            public static final int HIGH_BEAM = 6;  // 远光灯
            public static final int LOW_BEAM = 7;   // 近光灯
            public static final int DAY = 8;        // 日间行车灯
            public static final int BRAKE = 9;      // 刹车灯
            public static final int RESERVE = 10;   // 倒车灯
            public static final int ILL = 11;       // ILL灯线，只代表了车灯亮
            public static final int COUNT = 12;
        }

        public int mChangeMask;
        public int mStatusMask;

        public Light(int changeMask, int statusMask) {
            mChangeMask = changeMask;
            mStatusMask = statusMask;
        }

        public boolean isOn(int id) {
            return (mStatusMask & (1 << id)) != 0;
        }

        public boolean isOn() {
            return isOn(Id.MARKER) || isOn(Id.LOW_BEAM) || isOn(Id.ILL) || isOn(Id.HIGH_BEAM);
        }
    }

    /**
     * 大灯信息，与 Light 不一样，只控制背光的亮，暗
     */
    public static class HeadLight {
        public boolean mIsOn;

        public HeadLight(boolean isOn) {
            mIsOn = isOn;
        }

        public boolean isOn() {
            return mIsOn;
        }
    }

    /**
     * 快速倒车信息
     */
    public static class FastReverse {
        public boolean mIsOn;

        public FastReverse(boolean isOn) {
            mIsOn = isOn;
        }

        public boolean isOn() {
            return mIsOn;
        }
    }

    /**
     * ACC 状态
     */
    public static class Acc {
        public boolean mOn;

        public Acc(boolean on) {
            mOn = on;
        }
    }

    /**
     * 倒车状态
     */
    public static class Ccd {
        public static class Status {
            public static final int UNKNOWN = -1;
            public static final int OFF = 0;        // CCD OFF
            public static final int ON = 1;         // CCD ON
            public static final int ALREADY_ON = 2; // CCD 已经在系统启动阶段就已经被打开 （快速倒车）

            public static String getName(int status) {
                return LogNameUtil.getName(status, Status.class, "Unknown ccd status: " + status);
            }
        }

        public int mStatus;

        public Ccd(int status) {
            mStatus = status;
        }

        public boolean isOn() {
            return mStatus == Status.ON || mStatus == Status.ALREADY_ON;
        }
    }

    /**
     * 手刹状态
     */
    public static class Handbrake {
        public static class Status {
            public static final int UNKNOWN = -1;
            public static final int HOLD = 1;       // 手刹拉起，可以观看视频
            public static final int RELEASE = 0;    // 手刹放下，不可以观看视频

            public static String getName(int status) {
                return LogNameUtil.getName(status, Handbrake.Status.class);
            }
        }

        public int mStatus;

        public Handbrake(int status) {
            mStatus = status;
        }

        public Handbrake(boolean hold) {
            mStatus = hold ? Status.HOLD : Status.RELEASE;
        }

        public String getName() {
            return Status.getName(mStatus);
        }
    }

    /**
     * 车外温度
     */
    public static class OutsideTemp {
        public int mRawValue;

        public OutsideTemp(int rawValue) {
            mRawValue = rawValue;
        }

        public float getTemp(int tempUnit) {
            return getTemp(mRawValue, tempUnit);
        }

        private static float getOffset(float temp, int dotType) {
            if (dotType == 2) {
                return (temp < 0) ? -0.5f : 0.5f;
            } else {
                return 0.0f;
            }
        }

        public static boolean isShowDot(int rawValue) {
            int b1 = (rawValue & 0xFF);
            int dotType = (b1 & 0xC0) >> 6;
            return (dotType != 0);
        }

        public static float getTemp(int rawValue, int tempUnit) {
            int b0 = (rawValue & 0xFF00) >> 8;  //BYTE[0]
            int b1 = (rawValue & 0xFF);         //BYTE[1]
            int dotType = (b1 & 0xC0) >> 6;

            if ((b1 & 0x01) == 0) {
                float tempC = b0 - 100.0f;
                tempC += getOffset(tempC, dotType);

                if (tempUnit == TemperatureUnit.C) {
                    return tempC;
                } else {
                    return TemperatureUnit.C2F(tempC);
                }
            } else {
                float tempF = b0 - 60.0f;
                tempF += getOffset(tempF, dotType);

                if (tempUnit == TemperatureUnit.F) {
                    return tempF;
                } else {
                    return TemperatureUnit.F2C(tempF);
                }
            }
        }
    }

    /**
     * 车内温度
     */
    public static class InsideTemp {
        public int mRawValue;

        public InsideTemp(int rawValue) {
            if (rawValue == Climate.CLIMATE_VALUE_UNKNOWN) {
                mRawValue = IVICar.INSIDE_TEMP_UNKNOWN;
            } else {
                mRawValue = rawValue;
            }
        }

        public float getTemp(int tempUnit) {
            return getTemp(mRawValue, tempUnit);
        }

        public static boolean isShowDot(int rawValue) {
            return OutsideTemp.isShowDot(rawValue);
        }

        public static float getTemp(int rawValue, int tempUnit) {
            return OutsideTemp.getTemp(rawValue, tempUnit);
        }
    }

    /**
     * 雷达
     * 数据格式
     * 宝马雷达：
     * 距离，范围0-255。0表示无穷远
     * 其他：
     * BIT7-4: 数据范围（最大有多少格1-15）
     * BIT3-0: 实际数据（格数0-15)
     * 如果该字节为0x1F表示该探头位置异常，则在对应的位置显示异常符号
     * <p>
     * 注：
     * 1：格数越多表示距离越远，0表示距离无穷远.
     * 2：非宝马格式时，如果上位机收到非法数据（实际格数大于最大格数 数据上看就是低4位大于高4位），
     * 那么要按照有效格数=0处理。另外，协议端也要过滤这种情况
     */
    public static class Radar {
        public static class Type {
            public static final int NONE = 0x00;
            public static final int R3 = 0x01;
            public static final int F4R4 = 0x02;    // 车头4个，车尾4个
            public static final int F6R4 = 0x03;    // 车头6个，车尾4个
            public static final int F6R6 = 0x05;    // 车头6个，车尾6个
            public static final int BWM = 0x0A;     // 宝马雷达（布局同类型2，车头4个，车尾4个，数据格式不同）
        }

        public int mType;
        public byte[] mData;

        public Radar(int type, byte[] data) {
            mType = type;
            mData = data;
        }

        public static String getName(int type) {
            return LogNameUtil.getName(type, Radar.Type.class);
        }

        @Override
        public String toString() {
            return "Type: " + getName(mType) + " Data: " + mData;
        }
    };

    /**
     * 按键消息
     */
    public static class Key {
        public static class Id {
            public static final int NONE = 0;
            public static final int VOLUME_UP = 1;
            public static final int VOLUME_DOWN = 2;
            public static final int MODE = 6;
            public static final int ANSWER = 7; // 接听按键
            public static final int HOME = 8;
            public static final int ODD = 9;
            public static final int RADIO = 10;
            public static final int PLAY_PAUSE = 11;
            public static final int MP3 = 13;
            public static final int NAVI = 14;
            public static final int DVD = 15;
            public static final int EJECT = 16;
            public static final int CCD = 17;
            public static final int MP4 = 18;
            public static final int TV = 19;
            public static final int OSD = 20;
            public static final int DISP = 22;
            public static final int IPOD = 23;
            public static final int CAMERA = 24;
            public static final int OK = 25;
            public static final int AUX = 28;
            public static final int CLIMATE = 29;
            public static final int FM = 30;
            public static final int AM = 31;
            public static final int NEXT = 32;
            public static final int PREV = 33;
            public static final int HANG_UP = 34; // 挂断
            public static final int SCAN_UP = 42;
            public static final int SCAN_DOWN = 43;
            public static final int ALL_SCAN = 47;
            public static final int CLOSE_MEDIA = 50;
            public static final int UP = 58;
            public static final int DOWN = 59;
            public static final int LEFT = 60;
            public static final int RIGHT = 61;
            public static final int ROTATE_CW = 62;
            public static final int ROTATE_CCW = 63;
            public static final int FF = 64;
            public static final int FB = 65;
            public static final int BACK = 66;
            public static final int SELECT = 67;
            public static final int MEDIA_REPEAT = 70;
            public static final int MEDIA_SHUFFLE = 71;
            public static final int CAR_SET = 81;
            public static final int FUEL_INFO = 83;
            public static final int STANDBY_CLOCK = 84;
            public static final int STANDBY_BK_OFF = 85;
            public static final int STANDBY = 86;
            public static final int BT = 89;
            public static final int KEY_0 = 90;
            public static final int KEY_1 = 91;
            public static final int KEY_2 = 92;
            public static final int KEY_3 = 93;
            public static final int KEY_4 = 94;
            public static final int KEY_5 = 95;
            public static final int KEY_6 = 96;
            public static final int KEY_7 = 97;
            public static final int KEY_8 = 98;
            public static final int KEY_9 = 99;
            public static final int MUTE = 105;
            public static final int VOICE = 106;
            public static final int MULTI_FUNC = 159;   // 多功能键
            public static final int TUNEIN_RADIO = 160;
            public static final int DRIVE_MODE = 161;
            public static final int APP_LIST = 162;
            public static final int POWER = 72;
            public static final int AM_FM = 171; // FM和AM之间切换
            public static final int ALL_APP = 172; // 跳转所有应用页面
            public static final int START_APP = 173; // 启动应用
            public static final int MENU = 176; // 菜单
            public static final int REAR_UP = 178;           // 后排：上
            public static final int REAR_DOWN = 179;         // 后排：下
            public static final int REAR_LEFT = 180;         // 后排：左
            public static final int REAR_RIGHT = 181;        // 后排：右
            public static final int REAR_OK = 182;           // 后排：确定
            public static final int REAR_VOLUME_UP = 183;    // 后排：音量+
            public static final int REAR_VOLUME_DOWN = 184;  // 后排：音量-
            public static final int REAR_MENU = 185;         // 后排：MENU
            public static final int REAR_PLAY_PAUSE = 186;   // 后排：Play/Pause
            public static final int REAR_MUTE = 187;         // 后排：静音
            public static final int REAR_FF = 188;           // 后排：快进
            public static final int REAR_FB = 189;           // 后排：快退
            public static final int REAR_NEXT = 190;         // 后排：下一曲
            public static final int REAR_PREV = 191;         // 后排：上一曲
            public static final int VOLUME_SEL = 192;        // 选择调节哪个区的音量：前后排
            public static final int DUAL_ROTATE_CW = 193;    // 双区车型的旋钮，顺时针，由应用处理
            public static final int DUAL_ROTATE_CCW = 194;   // 双区车型的旋钮，逆时针，由应用处理
            public static final int ALL_MUTE = 195;          // 下旋钮短按双区静音，前后台同时MUTE,主要应用在双区媒体的按键控制上
            public static final int REBOOT = 1000;      // 重启
            public static final int PREV_ANSWER = 1001; // 正常状态是上一曲，来电状态是接听
            public static final int NEXT_HANGUP = 1002; // 正常状态是下一曲，来电或者通话状态是挂断
            public static final int EQ = 1003;          // EQ均衡器
            public static final int ANDROID_KEY = 1999; // 系统按键
        }

        public static class Name {
            public static final String NONE        = "none";       //无作用
            public static final String NEXT        = "next";       // 下一曲
            public static final String PREV        = "prev";       // 上一曲
            public static final String UP          = "up";         // 上
            public static final String DOWN        = "down";       // 下
            public static final String LEFT        = "left";       // 左
            public static final String RIGHT       = "right";      // 右
            public static final String ENTER       = "enter";      // 确定
            public static final String SCAN        = "scan";       // 收音机搜台
            public static final String SCAN_UP     = "scan_up";    // 收音机搜台
            public static final String SCAN_DOWN   = "scan_down";  // 收音机搜台
            public static final String AM_FM       = "am_fm";      // am 和 fm 切换
            public static final String AM          = "am";         // am
            public static final String FM          = "fm";         // fm
            public static final String MODE        = "mode";       // 模式切换
            public static final String SHUT_DOWN   = "shutdown";   // 关屏
            public static final String ADD_VOLUME  = "add_volume"; // 增加音量
            public static final String DEL_VOLUME  = "del_volume"; // 减少音量
            public static final String HOME        = "home";       // 主页
            public static final String BACK        = "back";       // 返回
            public static final String ALL_APP     = "all_app";    // 跳到 allApp页面
            public static final String NAVIGATION  = "navigation"; // 导航
            public static final String MUTE        = "mute";       // 禁音
            public static final String ANSWER      = "answer";     // 接听电话
            public static final String HANG_UP     = "hang_up";    // 挂断
            public static final String VOICE       = "voice";      // 语音
            public static final String PLAY_PAUSE  = "play_pause"; // 播放暂停
            public static final String MULTI_FUNC  = "multi_func";  //多功能按键
            public static final String POWER       = "power";      // power 按键
            public static final String MENU        = "menu";       // 菜单按键
            public static final String PREV_ANSWER = "prev_answer"; // 上一曲，来电状态是接听
            public static final String NEXT_HANGUP = "next_hangup"; // 下一曲，通话状态是挂断
            public static final String ROTATE_CW   = "rotate_cw";   // 旋钮顺时针转动
            public static final String ROTATE_CCW  = "rotate_ccw";   // 旋钮逆时针转动
            public static final String REAR_UP     = "rear_up";           // 后排：上
            public static final String REAR_DOWN   = "rear_down";         // 后排：下
            public static final String REAR_LEFT   = "rear_left";         // 后排：左
            public static final String REAR_RIGHT  = "rear_right";        // 后排：右
            public static final String REAR_OK     = "rear_ok";           // 后排：确定
            public static final String REAR_VOLUME_UP = "rear_volume_up";  // 后排：音量+
            public static final String REAR_VOLUME_DOWN = "rear_volume_down";  // 后排：音量-
            public static final String REAR_MENU   = "rear_menu";         // 后排：MENU
            public static final String REAR_PLAY_PAUSE = "rear_pause";    // 后排：Play/Pause
            public static final String REAR_MUTE   = "rear_mute";         // 后排：静音
            public static final String REAR_FF     = "rear_ff";           // 后排：快进
            public static final String REAR_FB     = "rear_fb";           // 后排：快退
            public static final String REAR_NEXT   = "rear_next";         // 后排：下一曲
            public static final String REAR_PREV   = "rear_prev";         // 后排：上一曲
            public static final String VOLUME_SEL  = "select_volume";     // 选择调节哪个区的音量：前后排
            public static final String DUAL_ROTATE_CW = "dual_rotate_cw"; // 双区车型的旋钮，顺时针，由应用处理
            public static final String DUAL_ROTATE_CCW = "dual_rotate_ccw";// 双区车型的旋钮，逆时针，由应用处理
            public static final String ALL_MUTE = "all_mute";              // 下旋钮短按双区静音，前后台同时MUTE,主要应用在双区媒体的按键控制上
            public static final String EQ = "eq";                          // EQ均衡器
            public static final String ANDROID_KEY = "android_key";        // 系统按键
        }

        public static class Type {
            public static final int DOWN = 1;   //按键下压
            public static final int UP = 0;     //按键抬起
            public static final int ONCE = 2;   //不分下压和抬起，只处理一次
        }

        public int mId;
        public int mType;
        public String mParams; // 有些按键需要带参数走，比如遥控器配置打开哪个应用，配置了包名，需要将包名带过去

        private static Map<String, Integer> mNameToIdMap;

        static {
            // 初始化Name的字符串到ID的映射
            mNameToIdMap = new HashMap<>();
            mNameToIdMap.put(Name.NEXT,         Id.NEXT);
            mNameToIdMap.put(Name.PREV,         Id.PREV);
            mNameToIdMap.put(Name.UP,           Id.UP);
            mNameToIdMap.put(Name.DOWN,         Id.DOWN);
            mNameToIdMap.put(Name.LEFT,         Id.LEFT);
            mNameToIdMap.put(Name.RIGHT,        Id.RIGHT);
            mNameToIdMap.put(Name.ENTER,        Id.OK);
            mNameToIdMap.put(Name.SCAN,         Id.ALL_SCAN);
            mNameToIdMap.put(Name.SCAN_UP,      Id.SCAN_UP);
            mNameToIdMap.put(Name.SCAN_DOWN,    Id.SCAN_DOWN);
            mNameToIdMap.put(Name.AM_FM,        Id.AM_FM);
            mNameToIdMap.put(Name.AM,           Id.AM);
            mNameToIdMap.put(Name.FM,           Id.FM);
            mNameToIdMap.put(Name.MODE,         Id.MODE);
            mNameToIdMap.put(Name.SHUT_DOWN,    Id.POWER);
            mNameToIdMap.put(Name.ADD_VOLUME,   Id.VOLUME_UP);
            mNameToIdMap.put(Name.DEL_VOLUME,   Id.VOLUME_DOWN);
            mNameToIdMap.put(Name.HOME,         Id.HOME);
            mNameToIdMap.put(Name.ALL_APP,      Id.ALL_APP);
            mNameToIdMap.put(Name.NAVIGATION,   Id.NAVI);
            mNameToIdMap.put(Name.BACK,         Id.BACK);
            mNameToIdMap.put(Name.MUTE,         Id.MUTE);
            mNameToIdMap.put(Name.DEL_VOLUME,   Id.VOLUME_DOWN);
            mNameToIdMap.put(Name.ANSWER,       Id.ANSWER);
            mNameToIdMap.put(Name.HANG_UP,      Id.HANG_UP);
            mNameToIdMap.put(Name.VOICE,        Id.VOICE);
            mNameToIdMap.put(Name.PLAY_PAUSE,   Id.PLAY_PAUSE);
            mNameToIdMap.put(Name.MULTI_FUNC,   Id.MULTI_FUNC);
            mNameToIdMap.put(Name.POWER,        Id.POWER);
            mNameToIdMap.put(Name.MENU,         Id.MENU);
            mNameToIdMap.put(Name.PREV_ANSWER,  Id.PREV_ANSWER);
            mNameToIdMap.put(Name.NEXT_HANGUP,  Id.NEXT_HANGUP);
            mNameToIdMap.put(Name.ROTATE_CW,    Id.ROTATE_CW);
            mNameToIdMap.put(Name.ROTATE_CCW,   Id.ROTATE_CCW);

            mNameToIdMap.put(Name.REAR_UP,      Id.REAR_UP);
            mNameToIdMap.put(Name.REAR_DOWN,    Id.REAR_DOWN);
            mNameToIdMap.put(Name.REAR_LEFT,    Id.REAR_LEFT);
            mNameToIdMap.put(Name.REAR_RIGHT,   Id.REAR_RIGHT);
            mNameToIdMap.put(Name.REAR_OK,      Id.REAR_OK);
            mNameToIdMap.put(Name.REAR_VOLUME_UP,    Id.REAR_VOLUME_UP);
            mNameToIdMap.put(Name.REAR_VOLUME_DOWN,  Id.REAR_VOLUME_DOWN);
            mNameToIdMap.put(Name.REAR_MENU,         Id.REAR_MENU);
            mNameToIdMap.put(Name.REAR_PLAY_PAUSE,   Id.REAR_PLAY_PAUSE);
            mNameToIdMap.put(Name.REAR_MUTE,    Id.REAR_MUTE);
            mNameToIdMap.put(Name.REAR_FF,      Id.REAR_FF);
            mNameToIdMap.put(Name.REAR_FB,      Id.REAR_FB);
            mNameToIdMap.put(Name.REAR_NEXT,    Id.REAR_NEXT);
            mNameToIdMap.put(Name.REAR_PREV,    Id.REAR_PREV);
            mNameToIdMap.put(Name.VOLUME_SEL,   Id.VOLUME_SEL);
            mNameToIdMap.put(Name.DUAL_ROTATE_CW,    Id.DUAL_ROTATE_CW);
            mNameToIdMap.put(Name.DUAL_ROTATE_CCW,   Id.DUAL_ROTATE_CCW);
            mNameToIdMap.put(Name.ALL_MUTE,   Id.ALL_MUTE);
            mNameToIdMap.put(Name.EQ,         Id.EQ);
            mNameToIdMap.put(Name.ANDROID_KEY, Id.ANDROID_KEY);
        }

        public Key(int id, int type) {
            mId = id;
            mType = type;
        }

        public Key(int id, boolean down) {
            mId = id;
            mType = down ? Type.DOWN : Type.UP;
        }

        public static String getName(int id) {
            String ret = "Key " + id;
            for (Map.Entry<String, Integer> entry : mNameToIdMap.entrySet()) {
                if (id == entry.getValue()) {
                    ret = entry.getKey();
                    break;
                }
            }
            return ret;
        }

        public static String getType(int type) {
            switch (type) {
                case Type.DOWN:
                    return "DOWN";
                case Type.UP:
                    return "UP";
                case Type.ONCE:
                    return "ONCE";
                default:
                    return "Type: " + type;
            }
        }

        public static int getId(String name) {
            Integer id = mNameToIdMap.get(name);
            if (id == null) {
                return Id.NONE;
            } else {
                return id;
            }
        }

        /**
         * 获取所有按键名称
         * @return 返回按键名字列表
         */
        public static List<String> getNames() {
            List<String> ret = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : mNameToIdMap.entrySet()) {
                ret.add(entry.getKey());
            }
            return ret;
        }

        /**
         * 获取所有按键的id值
         * @return 按键表
         */
        public static List<Integer> getIds() {
            List<Integer> ret = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : mNameToIdMap.entrySet()) {
                ret.add(entry.getValue());
            }
            return ret;
        }

        public String getName() {
            return getName(mId);
        }

        public Key(int id, int type, String params) {
            this(id, type);
            mParams = params;
        }
    }

    /**
     * 车辆故障和警告信息
     */
    public static class AlertMessage {
        int mMessageCode;

        public AlertMessage(int messageCode) {
            mMessageCode = messageCode;
        }
    }

    public static String getFuelConsumptionUnitString(int unit) {
        switch (unit) {
            case FuelConsumptionUnit.LP100KM:
                return "L/100km";
            case FuelConsumptionUnit.LPH:
                return "L/H";
            case FuelConsumptionUnit.MPG:
                return "MPG";
            default:
                return "";
        }
    }

    public static String getFuelConsumptionString(float consumption, int unit) {
        String value = "-.- ";
        if (consumption != FUEL_CONSUMPTION_UNKNOWN) {
            value = String.format(Locale.US, "%.1f ", consumption);
        }

        return value + getFuelConsumptionUnitString(unit);
    }

    public static String getTimeStringHourMinutes(int totalMinutes) {
        int hours = totalMinutes / 60;
        int leftMinutes = totalMinutes - hours * 60;
        return "" + hours + ":" + String.format(Locale.US, "%02d", leftMinutes);
    }

    // mcu 升级错误消息码定义
    public static class McuUpgradeErrorMsg {
        public static final int FILE_NOT_EXISTS     = 0; // 文件不存在
        public static final int FILE_NOT_READ       = 1; // 文件不能读
        public static final int FILE_FORMAT_ERROR   = 2; // 文件格式错误，后缀错误，暂时只支持.iap和.mcu格式升级
        public static final int FILE_CONTENT_ERROR  = 3; // 文件内容错误
        public static final int UPGRADE_FAILUE      = 4; // 下位机升级失败
        public static final int UPGRADE_TIME_OUT    = 5; // 升级超时

        public static String getName(int type) {
            return LogNameUtil.getName(type, McuUpgradeErrorMsg.class);
        }
    }

    /**
     * 车辆设置参数，从MCU到Android APK
     */
    public static class Setting {
        public int mCarId;
        public byte[] mData;

        public Setting(int carId, byte[] data) {
            mCarId = carId;
            mData = data;
        }
    }

    /**
     * 外部设备
     */
    public static class ExtraDevice {
        public static class CarId {
            public static final int GENERAL  = 0;        // 通用车型，不特指是哪个车型
            public static final int ZHONGTAI = 1;       // 众泰
            public static final int YUTONG   = 3;         // 宇通
        }

        public static class DeviceId {
            public static final int AVM360       = 1;         // 360全景
            public static final int AMBIENTLIGHT = 8;         // 氛围灯
            public static final int CARSET       = 10;        // 原车设置
        }


        public int mCarId;
        public int mDeviceId;
        public byte[] mData;

        public ExtraDevice(int carId, int deviceId, byte[] data) {
            mCarId = carId;
            mDeviceId = deviceId;
            mData = data;
        }

        public int getByte(int index) {
            if (index < 0 || mData == null || index >= mData.length) {
                return 0;
            }

            return mData[index] & 0xFF;
        }
    }

    /**
     * 配置参数，对应CIV_V2里面的CMD_PARAM
     */
    public static class CmdParam {
        public static class Id {
            public static final int EXTRA_DEVICE_CONFIG = 0x56;
        }

        public static class CarId {
            public static final int GENERAL = 0x01;     // 通用车型
        }

        public int mId;
        public byte[] mData;

        public CmdParam(int id, byte[] data) {
            mId = id;
            mData = data;
        }

        public int getByte(int index) {
            if (index < 0 || mData == null || index >= mData.length) {
                return 0;
            }

            return mData[index] & 0xFF;
        }
    }

    /**
     * 保养信息
     */
    public static class Maintenance {

        /**
         * 保养ID
         */
        public static class Id {
            public static final int INTERVAL = 0;          //保养周期
            public static final int INSPECTION = 1;        //保养检查
        }

        public int mId;         // 保养ID
        public int mMileage;    // 里程，单位为当前单位（100 km/100 mi), 0的时候显示----
        public int mDays;       // 时间（天数），0的时候显示---- 当里程或者时间2个参数有1个为0的时候提示保养

        public Maintenance(int id, int mileage, int days) {
            mId = id;
            mMileage = mileage;
            mDays = days;
        }
    }

    /**
     * 汽车VIN
     */
    public static class CarVIN {
        public String mVIN;     // 车辆识别码VIN，VIN码由17位字符组成 Vehicle Identification Number
        public int mKeyNumber;  // 匹配钥匙数目

        public CarVIN(String VIN, int number) {
            mVIN = VIN;
            mKeyNumber = number;
        }
    }

    /**
     * 故障报告，I9协议里的CMD_CAR_Report
     */
    public static class CarReport {

        public static class Car {
            public static final int GOLF = 7;        //Golf7
        }

        public static class Type {
            public static final int REPORT = 0;      //警告故障报告
            public static final int STARTSTOP = 1;   //自动启停报告
            public static final int AIRCON = 2;      //空调报告

            public static String getName(int type) {
                return LogNameUtil.getName(type, Type.class);
            }
        }

        public static class Control {
            public static final int HIDE = 0;       //隐藏
            public static final int SHOW = 1;       //显示
            public static final int REPAIR = 2;     //已修复
            public static final int CLEAR = 5;      //mcu开始上传故障码，此时需要清除故障列表
            public static final int ADD = 7;   //增加这条故障码
            public static final int DELETE = 9;     //删除这条故障码

            public static String getName(int type) {
                return LogNameUtil.getName(type, Control.class);
            }
        }

        public int mCarId;
        public int mConTrolId;
        public int mTypeId;
        public int mFaultCode;
        public int[] mCurrList;

        //解析
        public CarReport(int carid, int controlId, int type, int faultcode) {
            mCarId = carid;
            mConTrolId = controlId;
            mTypeId = type;
            mFaultCode = faultcode;
        }

        //刷新UI
        public CarReport(int carid, int type, int[] currlist) {
            mCarId = carid;
            mTypeId = type;
            mCurrList = currlist;
        }
    }

    /**
     * 自动泊车
     */
    public static class AutoPark {
        public int mStatus;

        public AutoPark(int status) {
            mStatus = status;
        }

        public static class Status {
            public static final int EXIT = 0x00;                                  //退出自动泊车系统
            public static final int ENTER = 0x01;                                 //寻找泊车位
            public static final int ENTER_RIGHT = 0x65;                           //寻找泊车位
            public static final int SLOW_DOWN = 0x02;                             //减速
            public static final int SLOW_DOWN_RIGHT = 100 + 0x02;                 //减速右边
            public static final int FOUND = 0x03;                                 //找到泊车位
            public static final int FOUND_RIGHT = 100 + 0x03;                     //找到泊车位
            public static final int DRIVE_FORWARD_OUTSIDE = 0x04;                 //向前开
            public static final int DRIVE_FORWARD_OUTSIDE_RIGHT = 100 + 0x04;     //向前开
            public static final int DRIVE_FORWARD_INSIDE = 0x05;                  //往前开
            public static final int DRIVE_FORWARD_INSIDE_RIGHT = 100 + 0x05;      //往前开
            public static final int RELEASE_STEER = 0x06;                         //双手离开方向盘，挂倒档
            public static final int RELEASE_STEER_RIGHT = 100 + 0x06;             //双手离开方向盘，挂倒档
            public static final int CHECK_SURROUND = 0x07;                        //检测周边，倒车
            public static final int CHECK_SURROUND_RIGHT = 100 + 0x07;            //检测周边，倒车
            public static final int STOP_DRIVE_BACKWARD = 0x08;                   //停止,向后
            public static final int STOP_DRIVE_BACKWARD_RIGHT = 100 + 0x08;       //停止,向后
            public static final int DRIVE_FORWARD = 0x09;                         //停止,向前
            public static final int DRIVE_FORWARD_RIGHT = 100 + 0x09;             //停止,向前
            public static final int DRIVE_BACKWARD = 0x0A;                        //往后开
            public static final int DRIVE_BACKWARD_RIGHT = 100 + 0x0A;            //往后开
            public static final int DRIVE_FORWARD_CONTINUE = 0x0B;                //继续向前
            public static final int DRIVE_FORWARD_CONTINUE_RIGHT = 100 + 0x0B;    //继续向前
            public static final int DRIVE_BACKWARD_CONTINUE = 0x0C;               //继续向后
            public static final int DRIVE_BACKWARD_CONTINUE_RIGHT = 100 + 0x0C;   //继续向后
            public static final int FINISH = 0x0D;                                //完成
            public static final int FINISH_RIGHT = 100 + 0x0D;                    //完成
            public static final int MALFUNCTION = 0xC9;                           //故障
            public static final int NOT_AVAILABLE = 0xCA;                         //不可用
            public static final int CANCELLED = 0xCB;                             //取消
            public static final int CANCELLED_BY_SPEEDING = 0xCC;
            public static final int CANCELLED_BY_STEERING = 0xCD;
            public static final int CANCELLED_BY_ESP_EVENT = 0xCE;
            public static final int CANCELLED_BY_ABS_EVENT = 0xCF;
            public static final int RESUME_AVAILABLE = 0xD0;
        }

        public static String getName(int code) {
            return LogNameUtil.getName(code, AutoPark.Status.class, "not any");
        }

        @Override
        public String toString() {
            return "Status: " + getName(mStatus);
        }
    }
	
	/*
     * 能量流动数据
     */
    public static class EnergyFlow {

        public int mBattery;                    // 电池电量,最大值由具体的车型确定
        public int mEngineToTyre;               // 发动机到驱动轮
        public int mEngineToMotor;              // 发动机到马达
        public int mMotorToTyre;                // 马达到驱动轮
        public int mMotorToBattery;              // 马达到电池

        public byte[] mEnergyData;              //总的buff，给app获取值用

        public EnergyFlow (int battery, int engineToTyre, int engineToMotor,
                           int motorToTyre, int motorToBatery, byte[] data) {
            mBattery = battery;
            mEngineToTyre = engineToTyre;
            mEngineToMotor = engineToMotor;
            mMotorToTyre = motorToTyre;
            mMotorToBattery = motorToBatery;

            mEnergyData = data;
        }

        public EnergyFlow (int battery, int engineToTyre, int engineToMotor, int motorToTyre, int motorToBatery) {
            mBattery = battery;
            mEngineToTyre = engineToTyre;
            mEngineToMotor = engineToMotor;
            mMotorToTyre = motorToTyre;
            mMotorToBattery = motorToBatery;
        }

        public static class Flow {
            public static final int NO_FLOW = 0x00;                       //无流动
            public static final int FROM_A_TO_B = 0x01;                   //从A到B，正向流动
            public static final int FROM_B_TO_A = 0x02;                   //从B到A，反向流动
            public static final int NO_CONNECTION = 0xFF;                 //两个部件直接没有联系
        }
    }

    /**
     * 下位机定义的语言
     */
    public static class Language {

        /**
         * English,值为 {@value}
         */
        public static final int LAN_ENGLISH = 0;

        /**
         * 中文,值为 {@value}
         */
        public static final int LAN_CHINESE = 1;

        /**
         * France,值为 {@value}
         */
        public static final int LAN_FRANCE = 2;

        /**
         * 繁体,值为 {@value}
         */
        public static final int LAN_TW = 3;

        /**
         * 西班牙,值为 {@value}
         */
        public static final int LAN_SPANISH = 4;

        /**
         * 意大利,值为 {@value}
         */
        public static final int LAN_ITALY = 5;

        /**
         * 德语,值为 {@value}
         */
        public static final int LAN_GERMAN = 6;

        /**
         * 荷兰语,值为 {@value}
         */
        public static final int LAN_NETHERLANDS = 7;

        /**
         * 俄语,值为 {@value}
         */
        public static final int LAN_RUSSIAN = 8;

        /**
         * 土耳其,值为 {@value}
         */
        public static final int LAN_TURKEY = 9;

        /**
         * 葡萄牙,值为 {@value}
         */
        public static final int LAN_PORTUGAL = 10;

        /**
         * 韩语,值为 {@value}
         */
        public static final int LAN_KOREAN = 11;

        /**
         * 捷克语,值为 {@value}
         */
        public static final int LAN_CZECH = 12;

        /**
         * 罗马尼亚语,值为 {@value}
         */
        public static final int LAN_ROMANIAN = 13;

        /**
         * 匈牙利语, 值为 {@value}
         */
        public static final int LAN_HUNGARIAN = 14;

        /**
         * 斯洛伐克语, 值为 {@value}
         */
        public static final int LAN_SLOVAKIA = 15;

        /**
         * 波兰语, 值为 {@value}
         */
        public static final int LAN_POLISH = 16;

        /**
         * 丹麦语, 值为 {@value}
         */
        public static final int LAN_DANISH = 17;

        /**
         * 乌克兰语, 值为 {@value}
         */
        public static final int LAN_UKRAINE = 18;

        /**
         * 芬兰语, 值为 {@value}
         */
        public static final int LAN_FINNISH = 19;

        /**
         * 瑞典语, 值为 {@value}
         */
        public static final int LAN_SWEDISH = 20;

        /**
         * 挪威语, 值为 {@value}
         */
        public static final int LAN_NORWEGIAN = 21;

        /**
         * 泰语, 值为 {@value}
         */
        public static final int LAN_THAI = 22;

        /**
         * 马来语, 值为 {@value}
         */
        public static final int LAN_MALAYSIAN = 23;

        /**
         * 印尼语, 值为 {@value}
         */
        public static final int LAN_INDONESIAN = 24;

        /**
         * 阿拉伯语, 值为 {@value}
         */
        public static final int LAN_ARABIC = 25;

        /**
         * 波斯语, 值为 {@value}
         */
        public static final int LAN_FARSI = 26;

        /**
         * 希伯来语, 值为 {@value}
         */
        public static final int LAN_HEBREW = 27;

        public static String getName(int type) {
            return LogNameUtil.getName(type, Language.class);
        }
    }

    /**
     * 协议方控学习按键类
     */
    public static class StudyKeyItem{
        public int mChannel;           //AD通道
        public int mMax;               //最大值
        public int mMin;               //最小值
        public int mOriMiddle;
        public boolean isNeedRepeat = false;   //是否需要按下连续执行短按功能，例如：音量加减
        public String mShortAction = null;    //短按功能
        public String mLongAction = null;     //长按功能

        public StudyKeyItem(){

        }

        public StudyKeyItem(int channel, int oriMiddle){
            mChannel = channel;
            mOriMiddle = oriMiddle;
        }

        public StudyKeyItem(int channel, int max, int min, boolean needRepeat, String shortaction, String longaction){
            mChannel = channel;
            mMax = max;
            mMin = min;
            isNeedRepeat = needRepeat;
            mShortAction = shortaction;
            mLongAction = longaction;

            mOriMiddle = (max + min)/2;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof StudyKeyItem) {
                if (TextUtils.equals(mShortAction, ((StudyKeyItem) o).mShortAction) &&
                        TextUtils.equals(mLongAction, ((StudyKeyItem) o).mLongAction) &&
                        isNeedRepeat == ((StudyKeyItem) o).isNeedRepeat) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 仪表发送的数据
     */
    public static class EventClusterMessage {
        public byte[] mDatas;
        public EventClusterMessage(byte[] datas) {
            mDatas = datas;
        }
    }
}
