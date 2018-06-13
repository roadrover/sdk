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
    public static final int OUTSIDE_TEMP_UNKNOWN = 0xFF;
    public static final int INSIDE_TEMP_UNKNOWN  = 0xFF;

    public static final float DISTANCE_UNKNOWN         = -1.0f;
    public static final float SPEED_UNKNOWN            = -1.0f;
    public static final float TIME_UNKNOWN             = -1.0f;
    public static final float FUEL_CONSUMPTION_UNKNOWN = -1.0f;
    public static final float BATTERY_VOLTAGE_UNKNOWN  = -1.0f;
    public static final float FUEL_L_LOW               = -2.0f;
    public static final float FUEL_L_UNKNOWN           = -1.0f;
    public static final float MAX_WHEEL_ANGLE          = 38.0f;
    public static final float MAX_STEERING_WHEEL_ANGLE = 200.0f;
    public static final float CLEAN_WATER_OK           = 0.0f;
    public static final float CLEAN_WATER_LOW          = 1.0f;
    public static final float SEAT_BELT_OK             = 0.0f;
    public static final float SEAT_BELT_RELEASE        = 1.0f;

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
        /** L/100km */
        public static final int LP100KM = 0;
        /** L/H */
        public static final int LPH     = 1;
        /** Mile per Gallon */
        public static final int MPG     = 2;
    }

    /**
     * 高频实时车辆信息，使用者只有主动注册需要该信息，则在该信息变化的时候才能获取回调
     */
    public static class RealTimeInfo {
        public static class Id {
            /** 实时速度 */
            public static final int SPEED                   = 1;
            /** 瞬时油耗 */
            public static final int FUEL_CONSUMPTION        = 2;
            /** 发动机转速 */
            public static final int ENGINE_RPM              = 3;
            /** 转向车轮转角 */
            public static final int WHEEL_ANGLE             = 4;
            /** 雷达，注意雷达数据通过Radar传送 */
            public static final int RADAR                   = 5;
            /** 实时速度，通过GPS来获取的 */
            public static final int SPEED_GPS               = 6;
            /** 分钟油耗，一分钟发一次 */
            public static final int FUEL_CONSUMPTION_MINUTE = 7;
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
            /** 电池电压 */
            public static final int BATTERY_VOLTAGE      = 0;
            /** 剩余油量L */
            public static final int REMAIN_FUEL          = 1;
            /** 剩余油量里程km */
            public static final int REMAIN_FUEL_DISTANCE = 2;
            /** 档位信息，参考Gear里面的定义 */
            public static final int GEAR                 = 3;
            /** 玻璃水 */
            public static final int CLEAN_WATER          = 4;
            /** 安全带 */
            public static final int SEAT_BELT            = 5;
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
            /** 前左 */
            public static final int FRONT_LEFT          = 0;
            /** 前右 */
            public static final int FRONT_RIGHT         = 1;
            /** 后左 */
            public static final int REAR_LEFT           = 2;
            /** 后右 */
            public static final int REAR_RIGHT          = 3;
            /** 发动机盖 */
            public static final int HOOT                = 4;
            /** 油箱盖 */
            public static final int FUEL_CAP            = 5;
            /** 后尾箱盖 */
            public static final int TRUNK               = 6;
            /** 后风窗 */
            public static final int REAR_WIND_SHIELD    = 7;
            /** 大巴行李箱门 */
            public static final int BUS_LUGGAGE         = 9;
            /** 大巴中门 */
            public static final int BUS_MID             = 10;
            /** 总车门数 */
            public static final int COUNT               = 11;
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
            /** 左转向灯 */
            public static final int TURN_LEFT  = 0;
            /** 右转向灯 */
            public static final int TURN_RIGHT = 1;
            /** 示廓灯 */
            public static final int MARKER     = 2;
            /** 紧急灯（双闪） */
            public static final int EMERGENCY  = 3;
            /** 前雾灯 */
            public static final int FRONT_FOG  = 4;
            /** 后雾灯 */
            public static final int REAR_FOG   = 5;
            /** 远光灯 */
            public static final int HIGH_BEAM  = 6;
            /** 近光灯 */
            public static final int LOW_BEAM   = 7;
            /** 日间行车灯 */
            public static final int DAY        = 8;
            /** 刹车灯 */
            public static final int BRAKE      = 9;
            /** 倒车灯 */
            public static final int RESERVE    = 10;
            /** ILL灯线，只代表了车灯亮 */
            public static final int ILL        = 11;
            /** id总数 */
            public static final int COUNT      = 12;
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
     * 车型英文名称
     */
    public static class CarEnName {
        /** 高尔夫7代 */
        public static final String GOLF_7  = "Golf 7";
    }

	/**
     * 车型中文名称
     */
    public static class CarCnName {
        /** 高尔夫7代 */
        public static final String GOLF_7  = "高尔夫7代";
    }

    /**
     * ACC 状态
     */
    public static class Acc {
        public boolean mOn;
        public boolean mIsAcc = false;

        public Acc(boolean on) {
            mOn = on;
        }

        public Acc(boolean on, boolean isAcc) {
            mOn = on;
            mIsAcc = isAcc;
        }
    }

    /**
     * 360全景状态
     */
    public static class Avm {
        public static class Status {
            public static final int UNKNOWN  = -1;
            public static final int OFF      = 0;
            public static final int ON       = 1;
        }
        public int mStatus;

        public Avm(int status) {
            mStatus = status;
        }
    }

    /**
     * 倒车状态
     */
    public static class Ccd {
        public static class Status {
            public static final int UNKNOWN    = -1;
            /** CCD OFF */
            public static final int OFF        = 0;
            /** CCD ON */
            public static final int ON         = 1;
            /** CCD 已经在系统启动阶段就已经被打开 （快速倒车） */
            public static final int ALREADY_ON = 2;

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
            /** 手刹拉起，可以观看视频 */
            public static final int HOLD    = 1;
            /** 手刹放下，不可以观看视频 */
            public static final int RELEASE = 0;

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
            public static final int R3   = 0x01;
            /** 车头4个，车尾4个 */
            public static final int F4R4 = 0x02;
            /** 车头6个，车尾4个 */
            public static final int F6R4 = 0x03;
            /** 车头6个，车尾6个 */
            public static final int F6R6 = 0x05;
            /** 宝马雷达（布局同类型2，车头4个，车尾4个，数据格式不同） */
            public static final int BWM  = 0x0A;
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
     * 车辆故障和警告信息
     */
    public static class AlertMessage {
        int mMessageCode;

        public AlertMessage(int messageCode) {
            mMessageCode = messageCode;
        }
    }

    public static String getFuelConsumptionUnitString(int unit) {
        return LogNameUtil.getName(unit, FuelConsumptionUnit.class);
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
        /** 文件不存在 */
        public static final int FILE_NOT_EXISTS     = 0;
        /** 文件不能读 */
        public static final int FILE_NOT_READ       = 1;
        /** 文件格式错误，后缀错误，暂时只支持.mcu格式升级 */
        public static final int FILE_FORMAT_ERROR   = 2;
        /** 文件内容错误 */
        public static final int FILE_CONTENT_ERROR  = 3;
        /** 下位机升级失败 */
        public static final int UPGRADE_FAILUE      = 4;
        /** 升级超时 */
        public static final int UPGRADE_TIME_OUT    = 5;

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
            /** 通用车型，不特指是哪个车型 */
            public static final int GENERAL  = 0;
            /** 众泰 */
            public static final int ZHONGTAI = 1;
            /** 宇通 */
            public static final int YUTONG   = 3;

            /**
             * 获取车型名字
             * @param id {@link CarId}
             * @return
             */
            public static String getCarName(int id) {
                return LogNameUtil.getName(id, ExtraDevice.CarId.class);
            }
        }

        public static class DeviceId {
            /** 360全景 */
            public static final int AVM360           = 0x01;
            /** 行车记录仪 */
            public static final int DRIVING_RECORDER = 0x02;
            /** PM2.5 */
            public static final int PM2_PONIT_5      = 0x04;
            /** PEPS */
            public static final int PEPS             = 0x07;
            /** 氛围灯 */
            public static final int AMBIENTLIGHT     = 0x08;
            /** 自动泊车 */
            public static final int AUTO_PARK        = 0x09;
            /** 原车设置 */
            public static final int CARSET           = 0x0A;
            /** 雷达报警音 */
            public static final int RADAR_WARING     = 0x0B;

            /**
             * 获取设备名字
             * @param id {@link DeviceId}
             * @return
             */
            public static String getDeviceName(int id) {
                return LogNameUtil.getName(id, ExtraDevice.DeviceId.class);
            }
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
     * 雷达报警类型
     */
    public static class RadarWaring {
        public static class Type {
            public static final int NONE = 0x00;
            public static final int HZ_1 = 0x01;
            public static final int HZ_2 = 0x02;
            public static final int HZ_4 = 0x03;
            public static final int HZ_8 = 0x04;
            public static final int WARING_ON = 0x05;
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
            /** 通用车型 */
            public static final int GENERAL = 0x01;
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
            /** 保养周期 */
            public static final int INTERVAL   = 0;
            /** 保养检查 */
            public static final int INSPECTION = 1;
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
     * 保养提醒
     */
    public static class MaintainWarning {
        public boolean mShow;

        public MaintainWarning(boolean show) {
            mShow = show;
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
            /** Golf7 */
            public static final int GOLF = 7;
        }

        public static class Type {
            /** 警告故障报告 */
            public static final int REPORT    = 0;
            /** 自动启停报告 */
            public static final int STARTSTOP = 1;
            /** 空调报告 */
            public static final int AIRCON    = 2;

            public static String getName(int type) {
                return LogNameUtil.getName(type, Type.class);
            }
        }

        public static class Control {
            /** 隐藏 */
            public static final int HIDE   = 0;
            /** 显示 */
            public static final int SHOW   = 1;
            /** 已修复 */
            public static final int REPAIR = 2;
            /** mcu开始上传故障码，此时需要清除故障列表 */
            public static final int CLEAR  = 5;
            /** 增加这条故障码 */
            public static final int ADD    = 7;
            /** 删除这条故障码 */
            public static final int DELETE = 9;

            public static String getName(int type) {
                return LogNameUtil.getName(type, Control.class);
            }
        }

        public int mCarId;
        public int mConTrolId;
        public int mTypeId;
        public int mFaultCode;
        public int[] mCurrList;

        /**
         * 解析
         */
        public CarReport(int carid, int controlId, int type, int faultcode) {
            mCarId = carid;
            mConTrolId = controlId;
            mTypeId = type;
            mFaultCode = faultcode;
        }

        /**
         * 刷新UI
         */
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
            /** 退出自动泊车系统 */
            public static final int EXIT                          = 0x00;
            /** 寻找泊车位 */
            public static final int ENTER                         = 0x01;
            /** 寻找泊车位 */
            public static final int ENTER_RIGHT                   = 0x65;
            /** 减速 */
            public static final int SLOW_DOWN                     = 0x02;
            /** 减速右边 */
            public static final int SLOW_DOWN_RIGHT               = 100 + 0x02;
            /** 找到泊车位 */
            public static final int FOUND                         = 0x03;
            /** 找到泊车位 */
            public static final int FOUND_RIGHT                   = 100 + 0x03;
            /** 向前开 */
            public static final int DRIVE_FORWARD_OUTSIDE         = 0x04;
            /** 向前开 */
            public static final int DRIVE_FORWARD_OUTSIDE_RIGHT   = 100 + 0x04;
            /** 往前开 */
            public static final int DRIVE_FORWARD_INSIDE          = 0x05;
            /** 往前开 */
            public static final int DRIVE_FORWARD_INSIDE_RIGHT    = 100 + 0x05;
            /** 双手离开方向盘，挂倒档 */
            public static final int RELEASE_STEER                 = 0x06;
            /** 双手离开方向盘，挂倒档 */
            public static final int RELEASE_STEER_RIGHT           = 100 + 0x06;
            /** 检测周边，倒车 */
            public static final int CHECK_SURROUND                = 0x07;
            /** 检测周边，倒车 */
            public static final int CHECK_SURROUND_RIGHT          = 100 + 0x07;
            /** 停止,向后 */
            public static final int STOP_DRIVE_BACKWARD           = 0x08;
            /** 停止,向后 */
            public static final int STOP_DRIVE_BACKWARD_RIGHT     = 100 + 0x08;
            /** 停止,向前 */
            public static final int DRIVE_FORWARD                 = 0x09;
            /** 停止,向前 */
            public static final int DRIVE_FORWARD_RIGHT           = 100 + 0x09;
            /** 往后开 */
            public static final int DRIVE_BACKWARD                = 0x0A;
            /** 往后开 */
            public static final int DRIVE_BACKWARD_RIGHT          = 100 + 0x0A;
            /** 继续向前 */
            public static final int DRIVE_FORWARD_CONTINUE        = 0x0B;
            /** 继续向前 */
            public static final int DRIVE_FORWARD_CONTINUE_RIGHT  = 100 + 0x0B;
            /** 继续向后 */
            public static final int DRIVE_BACKWARD_CONTINUE       = 0x0C;
            /** 继续向后 */
            public static final int DRIVE_BACKWARD_CONTINUE_RIGHT = 100 + 0x0C;
            /** 完成 */
            public static final int FINISH                        = 0x0D;
            /** 完成 */
            public static final int FINISH_RIGHT                  = 100 + 0x0D;
            /** 故障 */
            public static final int MALFUNCTION                   = 0xC9;
            /** 不可用 */
            public static final int NOT_AVAILABLE                 = 0xCA;
            /** 取消 */
            public static final int CANCELLED                     = 0xCB;
            public static final int CANCELLED_BY_SPEEDING         = 0xCC;
            public static final int CANCELLED_BY_STEERING         = 0xCD;
            public static final int CANCELLED_BY_ESP_EVENT        = 0xCE;
            public static final int CANCELLED_BY_ABS_EVENT        = 0xCF;
            public static final int RESUME_AVAILABLE              = 0xD0;
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
        public int mMotorToBattery;             // 马达到电池

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
            /** 无流动 */
            public static final int NO_FLOW       = 0x00;
            /** 从A到B，正向流动 */
            public static final int FROM_A_TO_B   = 0x01;
            /** 从B到A，反向流动 */
            public static final int FROM_B_TO_A   = 0x02;
            /** 两个部件直接没有联系 */
            public static final int NO_CONNECTION = 0xFF;

            /**
             * 打印标记，通过String打印
             * @param flow {@link Flow}
             * @return
             */
            public static String getName(int flow) {
                return LogNameUtil.getName(flow, Flow.class);
            }
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
        public int mChannel;           // AD通道
        public int mMax;               // 最大值
        public int mMin;               // 最小值
        public int mOriMiddle;
        public boolean isNeedRepeat = false;  // 是否需要按下连续执行短按功能，例如：音量加减
        public String mShortAction = null;    // 短按功能
        public String mLongAction = null;     // 长按功能

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

    /**
     * 发送触摸坐标给单片机
     */
    public static class TouchClickEvent {
        /** 弹起 */
        public static final int UP   = 1;
        /** 按下 */
        public static final int DOWN = 0;
    }

    /**
     * 硬件版本号数据
     */
    public static class EventHardwareMessage {
        public byte[] mDatas;
        public EventHardwareMessage(byte[] datas) {
            mDatas = datas;
        }
    }

    /**
     * 硬件版本号升级状态
     */
    public static class HardwareVersionStatus {
        /** 表示MCU写入成功 */
        public static final int SUCCESS = 0x05;
        /** 表示MCU写入失败（可能EEPROM故障等） */
        public static final int FAILURE = 0x04;
    }

    /**
     * 日期时间
     */
    public static class EventDateTime {
        public int mYear;
        public int mMonth;
        public int mDay;
        public int mHour;
        public int mMinute;
        public int mSecond;
        public int mWeek;

        public EventDateTime(int year, int month, int day, int hour, int minute, int second, int week) {
            mYear = year;
            mMonth = month;
            mDay = day;
            mHour = hour;
            mMinute = minute;
            mSecond = second;
            mWeek = week;
        }

        @Override
        public String toString() {
            return mYear + "/" + mMonth + "/" + mDay + " " + mHour + ":" + mMinute + " " + mSecond;
        }
    }

    /**
     * PEPS提示
     */
    public static class PEPS {

        public static class DisplayContent {
            /** 钥匙尾端按下启动按钮 */
            public static final int START_BUTTON_END_OF_KEY     = 0x01;
            /** 切换至P或者N档启动 */
            public static final int START_SWITCH_P_OR_N         = 0x02;
            /** 踩下离合按下启动按钮 */
            public static final int START_DEPRESSING_THE_CLUTCH = 0x03;
            /** 踩刹车按启动按钮 */
            public static final int START_BUTTON_BRAKES         = 0x04;
            /** 切换到P档按启动按钮 */
            public static final int START_SWITCH_P              = 0x05;
            /** 踩下刹车按下启动按钮切换到N档启动 */
            public static final int START_BRAKES_SWITCH_N       = 0x06;
            /** 踩下离合按下启动按钮启动 */
            public static final int START_DEPRESSING_CLUTCH     = 0x07;
            /** 切换至N档启动 */
            public static final int START_SWITCH_N              = 0x08;
            /** 未检测到钥匙 */
            public static final int NO_EEY_DETECTED             = 0x09;
            /** 钥匙电池电量低 */
            public static final int KEY_BATTERY_LOW             = 0x0A;

            /**
             * 调试打印接口
             * @param id {@ink DisplayContent}
             * @return
             */
            public String getName(int id) {
                return LogNameUtil.getName(id, DisplayContent.class);
            }
        }

        public static class DisplayTimes {
            /** 立即消失 */
            public static final int NOW_DISAPPEAR = 0x00;
            /** 常显 */
            public static final int ALWAYS        = 0xff;
        }

        public static class NumbeAlarms {
            /** 无报警声 */
            public static final int NO     = 0x00;
            /** 常响 */
            public static final int ALWAYS = 0xff;
        }
    }

}
