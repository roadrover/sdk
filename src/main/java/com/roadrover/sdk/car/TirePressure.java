package com.roadrover.sdk.car;


import com.roadrover.sdk.utils.LogNameUtil;
import com.roadrover.sdk.utils.Logcat;

public class TirePressure {

    public String TAG = "TirePressure";
    public static final int TIRE_PRESSURE_VULUE_UNKNOWN = -1;

    public int mId;         // ID 定义
    public byte[] mData;
    public int rawValue;    // 数据
    public int extraValue;  // 附加状态数据
    public int dotType;     // 温度小数点标志  0整数显示(默认） 1带一个小数点，后面为.0  2带一个小数点，后面为.5

    public static class ID {
        /** 左前轮温度值 */
        public static final int LEFT_FRONT_TEMP  = 1;
        /** 右前轮温度值 */
        public static final int RIGHT_FRONT_TEMP = 2;
        /** 左后轮温度值 */
        public static final int LEFT_REAR_TEMP   = 3;
        /** 右后轮温度值 */
        public static final int RIGHT_REAR_TEMP  = 4;

        /** 左前轮温度警报 */
        public static final int LEFT_FRONT_TEMP_ALARM  = 5;
        /** 右前轮温度警报 */
        public static final int RIGHT_FRONT_TEMP_ALARM = 6;
        /** 左后轮温度警报 */
        public static final int LEFT_REAR_TEMP_ALARM   = 7;
        /** 右后轮温度警报 */
        public static final int RIGHT_REAR_TEMP_ALARM  = 8;

        /** 左前轮胎压警报 */
        public static final int LEFT_FRONT_TYRE_ALARM  = 9;
        /** 右前轮胎压警报 */
        public static final int RIGHT_FRONT_TYRE_ALARM = 10;
        /** 左后轮胎压警报 */
        public static final int LEFT_REAR_TYRE_ALARM   = 11;
        /** 右后轮胎压警报 */
        public static final int RIGHT_REAR_TYRE_ALARM  = 12;

        /** 左前轮图标闪烁 */
        public static final int LEFT_FRONT_ICON_TWINKLE  = 13;
        /** 右前轮图标闪烁 */
        public static final int RIGHT_FRONT_ICON_TWINKLE = 14;
        /** 左后轮图标闪烁 */
        public static final int LEFT_REAR_ICON_TWINKLE   = 15;
        /** 右后轮图标闪烁 */
        public static final int RIGHT_REAR_ICON_TWINKLE  = 16;

        /** 左前轮图标颜色 */
        public static final int LEFT_FRONT_ICON_COLOR  = 17;
        /** 右前轮图标颜色 */
        public static final int RIGHT_FRONT_ICON_COLOR = 18;
        /** 左后轮图标颜色 */
        public static final int LEFT_REAR_ICON_COLOR   = 19;
        /** 右后轮图标颜色 */
        public static final int RIGHT_REAR_ICON_COLOR  = 20;

        /** 左前轮胎压值 */
        public static final int LEFT_FRONT_TYRE  = 21;
        /** 右前轮胎压值 */
        public static final int RIGHT_FRONT_TYRE = 22;
        /** 左后轮胎压值 */
        public static final int LEFT_REAR_TYRE   = 23;
        /** 右后轮胎压值 */
        public static final int RIGHT_REAR_TYRE  = 24;

        /** 左前轮传感器故障 */
        public static final int LEFT_FRONT_SENSOR  = 25;
        /** 右前轮传感器故障 */
        public static final int RIGHT_FRONT_SENSOR = 26;
        /** 左后轮传感器故障 */
        public static final int LEFT_REAR_SENSOR   = 27;
        /** 右后轮传感器故障 */
        public static final int RIGHT_REAR_SENSOR  = 28;

        /** 系统状态 */
        public static final int SYSTEM_STATUS = 29;

        /**
         * 获取对应{@link TirePressure.ID}名称
         * @param id 见{@link TirePressure.ID}
         * @return
         */
        public static String getName(int id) {
            return LogNameUtil.getName(id, TirePressure.class, "Unknown id: " + id);
        }
    }

    public static class TEMPERATURE {
        /** 无效 */
        public static final int UNKNOWN = -1;
        /** 摄氏度 */
        public static final int C = 0x00;
        /** 华氏度 */
        public static final int F = 0x01;
    }

    public static class TEMPERATURE_ALARM {
        /** 温度正常 */
        public static final int TEMPERATURE_NOMAL = 0x00;
        /** 低温警报 */
        public static final int TEMPERATURE_LOW   = 0x01;
        /** 高温警报 */
        public static final int TEMPERATURE_HIGH  = 0x02;
    }

    public static class TIRE_PRESSURE_ALARM {
        /** 压力正常 */
        public static final int TIRE_PRESSURE_NOMAL = 0x00;
        /** 低压警报 */
        public static final int TIRE_PRESSURE_LOW   = 0x01;
        /** 高压警报 */
        public static final int TIRE_PRESSURE_HIGH  = 0x02;
        /** 漏气报警 */
        public static final int LEAKAGE             = 0x03;
    }

    public static class TIRE_ICON_COLOR {
        /** 灰色 */
        public static final int GRAY   = 0x00;
        /** 绿色(正常 */
        public static final int GREEN  = 0x01;
        /** 黄色(警告) */
        public static final int ORANGE = 0X02;
        /** 红色(严重警告) */
        public static final int RED    = 0X03;
    }

    public static class TIRE_ICON_TWINKLE {
        /** 不闪 */
        public static final int NO   = 0x00;
        /** 慢闪 */
        public static final int SLOW = 0x01;
        /** 快闪 */
        public static final int FAST = 0x02;
    }

    public static class SENSOR_STATE {
        /** 传感器正常 */
        public static final int NORMAL      = 0x00;
        /** 传感器电压过低 */
        public static final int LOW_VOLTAGE = 0x01;
        /** 传感器丢失 */
        public static final int LOST        = 0x02;
        /** 传感器失效 */
        public static final int INVALID     = 0x03;
    }

    public static class SYSTEM_STATUS {
        /** 系统正常 */
        public static final int NORMAL      = 0x00;
        /** 系统故障 */
        public static final int LOW_VOLTAGE = 0x01;
        /** 压力异常 */
        public static final int LOST        = 0x02;
        /** 胎压学习未完成 */
        public static final int INVALID     = 0x03;
    }

    public TirePressure(int id, int rawValue, int extraValue, int dotType) {
        this.mId = id;
        this.rawValue = rawValue;
        this.extraValue = extraValue;
        this.dotType = dotType;
    }

    public TirePressure(int id, byte[] data) {
        this.mId = id;
        this.mData = data;
        parseData(mId, mData);
    }

    /**
     * 解析数据
     *
     * @param id
     * @param data
     */
    public void parseData(int id, byte[] data) {
        switch (id) {
            case ID.LEFT_FRONT_TEMP:
            case ID.RIGHT_FRONT_TEMP:
            case ID.LEFT_REAR_TEMP:
            case ID.RIGHT_REAR_TEMP:
                parseTemperature(data);    // 解析温度 1- 4
                break;
            case ID.LEFT_FRONT_TEMP_ALARM:
            case ID.RIGHT_FRONT_TEMP_ALARM:
            case ID.LEFT_REAR_TEMP_ALARM:
            case ID.RIGHT_REAR_TEMP_ALARM:
                parseOneByteAlarm(data);   // 解析温度警报 5- 8
                break;
            case ID.LEFT_FRONT_TYRE_ALARM:
            case ID.RIGHT_FRONT_TYRE_ALARM:
            case ID.LEFT_REAR_TYRE_ALARM:
            case ID.RIGHT_REAR_TYRE_ALARM:
                parseOneByteAlarm(data);   // 胎压警报9 -  12
                break;
            case ID.LEFT_FRONT_ICON_TWINKLE:
            case ID.RIGHT_FRONT_ICON_TWINKLE:
            case ID.LEFT_REAR_ICON_TWINKLE:
            case ID.RIGHT_REAR_ICON_TWINKLE:
                parseOneByteAlarm(data);   // 前、后轮图标闪烁13 - 16
                break;
            case ID.LEFT_FRONT_ICON_COLOR:
            case ID.RIGHT_FRONT_ICON_COLOR:
            case ID.LEFT_REAR_ICON_COLOR:
            case ID.RIGHT_REAR_ICON_COLOR:
                parseOneByteAlarm(data);   // 前、后轮图标颜色  17 - 20
                break;
            case ID.LEFT_FRONT_TYRE:
            case ID.RIGHT_FRONT_TYRE:
            case ID.LEFT_REAR_TYRE:
            case ID.RIGHT_REAR_TYRE:
                parseTirePressure(data);   // 解析胎压压力值 21- 24
                break;
            case ID.LEFT_FRONT_SENSOR:
            case ID.RIGHT_FRONT_SENSOR:
            case ID.LEFT_REAR_SENSOR:
            case ID.RIGHT_REAR_SENSOR:
                parseTwoByteAlarm(data);   // 传感器故障25-28
                break;
            case ID.SYSTEM_STATUS:         // 系统状态
                parseOneByteAlarm(data);
                break;
        }
    }

    /**
     * 解析温度
     * @param data
     */
    private void parseTemperature(byte[] data) {
        if (data != null && data.length > 0) {
            int temp = data[0] & 0xff;
            if (data[0] == (byte) 0xff) {
                rawValue = TEMPERATURE.UNKNOWN;
            } else {
                if (data.length < 2) { // 没有byte[3]直接显示整数、摄氏度
                    rawValue = temp - 100;
                    extraValue = TEMPERATURE.C;
                } else {
                    int unit = data[1] & 0x01;// unit 0 摄氏度 - 1 华氏度
                    dotType = (data[1] >> 6) & 0x03;    //  0整数 , 1后面加.0 ，2后面加.5
                    if (unit == 0) {
                        rawValue = temp - 100; // 摄氏度表达：（C+100）
                        extraValue = TEMPERATURE.C;
                    } else {
                        rawValue = temp - 60; //  华氏度表达：(F+60)
                        extraValue = TEMPERATURE.F;
                    }
                }
            }
        }
    }

    /**
     * 解析带有一个Byte的数据
     * @param data
     */
    private void parseOneByteAlarm(byte[] data) {
        if (data != null && data.length > 0) {
            rawValue = data[0];
        }
    }

    /**
     * 解析带有2个Byte的数据
     * @param data
     */
    private void parseTwoByteAlarm(byte[] data) {
        if (data != null && data.length > 0) {
            rawValue = data[0];
            if (data.length > 1) {
                extraValue = data[1]; 
            }
        }
    }

    /**
     * 解析胎压压力值
     * @param data
     */
    private void parseTirePressure(byte[] data) {
        if (data != null && data.length > 0) {
            if (data.length > 1) {
                if ((data[0] == (byte) 0xff) && (data[1] == (byte) 0xff)) {
                    rawValue = TIRE_PRESSURE_VULUE_UNKNOWN;
                } else {
                    rawValue = ((data[0] & 0xff) << 8) | (data[1] & 0xff);
                }
            } else {
                rawValue = TIRE_PRESSURE_VULUE_UNKNOWN;
            }
        } else {
            rawValue = TIRE_PRESSURE_VULUE_UNKNOWN;
        }
    }

    public static float getTemperature(int rawValue, int dotType) {
        return rawValue + getOffset(rawValue, dotType);
    }

    public static String getString(int tempUnit) {
        return (tempUnit == TEMPERATURE.C) ? "℃" : "℉";
    }

    public static float getOffset(float temp, int dotType) {
        if (dotType == 2) {
            return (temp < 0) ? -0.5f : 0.5f;
        } else {
            return 0.0f;
        }
    }
}
