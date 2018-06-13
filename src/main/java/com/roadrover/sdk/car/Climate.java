package com.roadrover.sdk.car;

import java.text.DecimalFormat;

/**
 * 空调消息
 */
public class Climate {
    public static final int CLIMATE_VALUE_UNKNOWN = -1;
    public static class Id {
        public static final int UNKNOWN                = -1;
        public static final int FRONT_LEFT_TEMP        = 1;  // 前排左边温度
        public static final int FRONT_RIGHT_TEMP       = 2;  // 前排右边温度
        public static final int REAR_TEMP              = 3;  // 后排温度
        public static final int FRONT_LEFT_CHAIR_HEAT  = 4;  // 左边座椅加热
        public static final int FRONT_RIGHT_CHAIR_HEAT = 5;  // 右边座椅加热
        public static final int FRONT_LEFT_COOL        = 6;  // 左边座椅制冷
        public static final int FRONT_RIGHT_COOL       = 7;  // 右边座椅制冷
        public static final int MY_TEMP                = 8;  // 我的温度
        public static final int FRONT_AUTO             = 9;  // 前排空调AUTO
        public static final int AIR_DIR_TO_WINDOW      = 11; // 吹风向窗户
        public static final int AIR_DIR_TO_HEAD        = 12; // 吹风向头部
        public static final int AIR_DIR_TO_LEG         = 13; // 吹风向脚部
        public static final int AUTO_FAN_LEVEL         = 14; // 自动风速档次 (0: 关，1-3档次)
        public static final int AUTO_AIR_CIRCLE        = 15; // 自动空气循环 (0: 关，1: 开)
        public static final int FAN_LEVEL              = 16; // 风量大小调整
        public static final int AC_LEVEL               = 17; // 空调运行方式（1:弱,2:中,3:强）
        public static final int AIR_CIRCLE_MODE        = 18; // 循环方式 (1:内循环，2外循环)
        public static final int AC                     = 19; // AC
        public static final int AIR_DIR                = 20; // 前排空调吹风模式（吹风方向）
        public static final int LEFT_AC_LEVEL          = 21; // 左边AC模拟量(0最冷，100最热)
        public static final int RIGHT_AC_LEVEL         = 22; // 右边AC模拟量(0最冷，100最热)
        public static final int FRONT_DEFOG            = 23; // 前窗除雾器Front Dimister MAX 0关闭 1打开
        public static final int REAR_DEFOG             = 24; // 后窗吹风(0关闭，1打开)
        public static final int DUAL_ZONE              = 25; // DUAL双区 (0关闭，1打开)
        public static final int AC_ON_OFF              = 26; // 空调开关（0：关，1开）
        public static final int MAX_AC                 = 27; // Max Ac（0：关，1开）
        public static final int WHEEL_HEAT             = 28; // 方向盘加热（0：关，1开）
        public static final int FRONT_WINDOW_HEAT      = 29; // 挡风玻璃加热（0：关，1开）
        public static final int REAR_AC                = 30; // 后排空调(0: 关，1:开)
        public static final int REAR_AC_AUTO           = 31; // 后排空调AUTO(0: 关, 1: 开)
        public static final int REAR_AC_LOCK           = 32; // 后排空调Lock(0: 关, 1: 开)
        public static final int REAR_FAN_LEVEL         = 33; // 后排风量大小（含义同16）
        public static final int REAR_AIR_MODE          = 34; // 后排空调吹风模式选择
        public static final int MIRROR_HEAT            = 35; // 后视镜加热（0：关，1开）
        public static final int AUTO_WINDSCREEN_HEAT   = 36; // 动挡风玻璃加热（0：关，1开）
        public static final int FORCE_AC_SWITCH        = 37; // 强制AC开关（0：关，1开）
        public static final int AIR_SWAP_FAN_SWITCH    = 38; // 换气扇开关（0：关，1开）
        public static final int AUX_HEATER             = 40; // 辅助加热 0: 关Deactivate 1: 开Activate now
        public static final int MAX_DEMISTER_SWITCH    = 50; // max demister switch （0：关，1开）
        public static final int CONV_CONSUME_LEVEL     = 51; // Conv consume level
        public static final int POLLEN_FILTER_MODE     = 52; // Pollen filter mode 0：关闭，1：打开
        public static final int AUTO_BAK_HEATER        = 53; // 自动备用加热器(0: 关，1: 开)
        public static final int REAR_AIR_TO_TOP        = 54; // 后排吹风向顶部(0: 关，1: 开)
        public static final int REAR_AIR_TO_HEAD       = 55; // 后排吹风向头部(0: 关，1: 开)
        public static final int REAR_AIR_TO_LEG        = 56; // 后排吹风向脚部(0: 关，1: 开)
        public static final int REAR_LEFT_TEMP         = 57; // 后排左边温度
        public static final int REAR_RIGHT_TEMP        = 58; // 后排右边温度
        public static final int REAR_AUTO_FAN_LEVEL    = 59; // 后排自动风速档次(0: 关，1-3档次)
        public static final int MAX_HOT                = 60; // 打开最大制热
        public static final int UP_DOWN_TEMP           = 63; // 温度上下调节  1：上调，2: 下调
        public static final int NEGATIVE_ICONS         = 64; // 负离子
        public static final int SYNC_ZONE              = 66; // 多区(异步)
        public static final int AC_SWITCH              = 68; // AC请求开关
        public static final int SHOW_CLIMATE_UI        = 80; // 主空调页面打开和关闭 0：关闭   1：打开
        public static final int INSIDE_TEMP            = 81; // 车内温度，定义和车外温度是一样的
        public static final int ELECTRIC_TEMP          = 82; // 电动空调（低配）温度控制；只有1-15 15个档位，无具体温度值
        public static final int START_CLIMATE_DIALOG   = 83; // 显示空调界面（弹框）
        public static final int START_CLIMATE_COMMON   = 84; // 显示空调界面（适用于常显）
        public static final int UP_DOWN_LEFT_TEMP      = 85; // 左温度上下调节
        public static final int UP_DOWN_RIGHT_TEMP     = 86; // 右温度上下调节
        public static final int REAR_CLIMATE_SWITCH    = 91; // 后排空调开关
        public static final int FRONT_LEFT_TEMP_VOICE  = 129;// 前排左边温度,通过声控功能启动
        public static final int FRONT_RIGHT_TEMP_VOICE = 130;// 前排右边温度,通过声控功能启动
        public static final int FRONT_LEFT_CHAIR_HEAT_VOICE  = 132; // 左边座椅加热(语音)
        public static final int FRONT_RIGHT_CHAIR_HEAT_VOICE = 133; // 右边座椅加热(语音)
        public static final int FRONT_LEFT_COOL_VOICE        = 134; // 左边座椅制冷(语音)
        public static final int FRONT_RIGHT_COOL_VOICE       = 135; // 右边座椅制冷(语音)
        public static final int FAN_LEVEL_VOICE              = 144; // 风力设置(语音)
    }

    public class AIR_CIRCLE_MODE {
        public static final int NONE     = 0;
        public static final int INTERNAL = 1;
        public static final int EXTERNAL = 2;
        public static final int AUTO     = 3;
    }

    public class FAN_LEVEL {
        public static final int NOT_DISPLAY = 20;
        public static final int AUTO        = 100;
    }

    public class CHAIR_HEAT {
        public static final int OFF  = 100;
        public static final int LOW  = 101;
        public static final int MID  = 102;
        public static final int HIGH = 103;
        public static final int NONE = 0xFF;
    }

    public int mId;
    public int mRawValue;
    protected static Climate mUnknown = new Climate(Id.UNKNOWN, CLIMATE_VALUE_UNKNOWN);

    public Climate() {
        mId = 0;
        mRawValue = CLIMATE_VALUE_UNKNOWN;
    }

    public Climate(int id, int rawValue) {
        mId = id;
        mRawValue = rawValue;
    }

    public static Climate getUnknown() {
        return mUnknown;
    }

    public boolean isValid() {
        return mRawValue != CLIMATE_VALUE_UNKNOWN;
    }

    public String getTemp(int tempUnit) {
        if (mRawValue == CLIMATE_VALUE_UNKNOWN) {
            return "-.-";
        } else if (isTempHi()) {
            return "HI";
        } else if (isTempLo()) {
            return "LO";
        } else if (mRawValue == 0xFF) {
            return "";
        } else {
            float value = getTempValue(tempUnit);
            DecimalFormat df = new DecimalFormat("###.0");
            return df.format(value) + " " + IVICar.TemperatureUnit.getString(tempUnit);
        }
    }

    public float getTempValue(int tempUnit) {
        /**
         *  01 Lo，F0: High，其他为温度*2,
         *  20 – 80 : 表示摄氏度10-40 度,
         *  100 – 200: 表示华氏度 50 – 100 度
         */
        int value = mRawValue;
        if (value >= 20 && value <= 80) {
            float tempC = value/2.0f;
            return (tempUnit == IVICar.TemperatureUnit.C)? tempC : IVICar.TemperatureUnit.C2F(tempC);
        } else if (value >= 100 && value <= 200) {
            float tempF = value/2.0f;
            return (tempUnit == IVICar.TemperatureUnit.F)? tempF : IVICar.TemperatureUnit.F2C(tempF);
        } else {
            // 错误的温度值
            return 0.0f;
        }
    }

    public boolean isTempLo() {
        return (mRawValue == 0x01);
    }

    public boolean isTempHi() {
        return (mRawValue == 0xF0);
    }

    /**
     * 得到AC的状态字符串，ID=Climate.Id.AC
     */
    public String getAc() {
        switch (mRawValue) {
            case 0: return "";
            case 1: return "AC";
            case 2: return "AC OFF";
            case 3: return "A/C";
            case 4: return "A/C OFF";
            case 5: return "A/C ON";
            case 6: return "A/C AUTO";
            default:return "";
        }
    }

    public boolean isAcOn() {
        return (mRawValue == 1 || mRawValue == 3 || mRawValue == 5);
    }

    public boolean isAcAuto() {
        return mRawValue == 6;
    }
}