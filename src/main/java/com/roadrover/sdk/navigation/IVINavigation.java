package com.roadrover.sdk.navigation;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * 导航类型的定义类
 */

public class IVINavigation {

    /**
     * 导航引导类型定义
     */
    public static class TwelveClockType {
        /** 普通引导类型 */
        public static final int NORMAL = 0;
        /** 12点钟方式引导类型 */
        public static final int TWELVE_CLOCK = 1;

        public static String getName(int type) {
            return LogNameUtil.getName(type, TwelveClockType.class);
        }
    }

    /**
     * 导航普通引导id定义 </br>
     * TwelveClockType定义为NORMAL时使用
     */
    public static class NormalTurnId {
        /** 直行 */
        public static final int STRAIGHT_LINE           = 0;
        /** 左前方 */
        public static final int LEFT_ANTERIOR           = 1;
        /** 右前方 */
        public static final int RIGHT_ANTERIOR          = 2;
        /** 左转 */
        public static final int LEFT_TURN               = 3;
        /** 右转 */
        public static final int RIGHT_TURN              = 4;
        /** 左后方 */
        public static final int LEFT_REAR               = 5;
        /** 右后方 */
        public static final int RIGHT_REAR              = 6;
        /** 左调头 */
        public static final int LEFT_TUNER              = 7;
        /** 右调头 */
        public static final int RIGHT_TUNER             = 8;
        /** 右转专用道 */
        public static final int RIGHT_TURN_ROAD         = 9;
        /** 左转专用道 */
        public static final int LEFT_TURN_ROAD          = 10;
        /** 主路进辅路 */
        public static final int MAIN_TO_SIDE_ROAD       = 11;
        /** 辅路进主路 */
        public static final int SIDE_TO_MAIN_ROAD       = 12;
        /** 环岛入口 */
        public static final int ROTARY_ISLAND_ENTRANCE  = 23;
        /** 环岛出口 */
        public static final int ROTARY_ISLAND_EXIT      = 24;
        /** 隧道入口 */
        public static final int TUNNEL_ENTRANCE         = 25;
        /** 目的地 */
        public static final int DESTINATION             = 26;
        /** 服务区 */
        public static final int SERVICE_AREA            = 27;
        /** 高速路口停车区 */
        public static final int HIGH_SPEED_PARKING_AREA = 28;
        /** 轮渡码头 */
        public static final int FERRY_TERMINAL          = 29;
        /** 途径地1 */
        public static final int PATH_ONE                = 30;
        /** 途径地2 */
        public static final int PATH_TWO                = 31;
        /** 途径地3 */
        public static final int PATH_THREE              = 32;
        /** 途径地4 */
        public static final int PATH_FOUR               = 33;

        public static String getName(int id) {
            return LogNameUtil.getName(id, NormalTurnId.class);
        }
    }

    /**
     * 十二点钟方向的id </br>
     * TwelveClockType定义为TWELVE_CLOCK时使用
     */
    public static class TwelveClockTurnId {
        /** 1点钟方向 */
        public static final int ONE_CLOCK_DIRECTION    = 1;
        /** 2点钟方向 */
        public static final int TWO_CLOCK_DIRECTION    = 2;
        /** 3点钟方向 */
        public static final int THREE_CLOCK_DIRECTION  = 3;
        /** 4点钟方向 */
        public static final int FOUR_CLOCK_DIRECTION   = 4;
        /** 5点钟方向 */
        public static final int FIVE_CLOCK_DIRECTION   = 5;
        /** 6点钟方向 */
        public static final int SIX_CLOCK_DIRECTION    = 6;
        /** 7点钟方向 */
        public static final int SEVEN_CLOCK_DIRECTION  = 7;
        /** 8点钟方向 */
        public static final int EIGHT_CLOCK_DIRECTION  = 8;
        /** 9点钟方向 */
        public static final int NINE_CLOCK_DIRECTION   = 9;
        /** 10点钟方向 */
        public static final int TEN_CLOCK_DIRECTION    = 10;
        /** 11点钟方向 */
        public static final int ELEVEN_CLOCK_DIRECTION = 11;
        /** 12点钟方向 */
        public static final int TWELVE_CLOCK_DIRECTION = 12;

        public static String getName(int id) {
            return LogNameUtil.getName(id, TwelveClockTurnId.class);
        }
    }

    /**
     * 引导点属性
     */
    public static class GuideType {
        /** 高速入口 */
        public static final int HIGH_SPEED_ENTRANCE = 0;
        /** 高速出口 */
        public static final int HIGH_SPEED_EXIT     = 1;
        /** 城快入口 */
        public static final int CITY_FAST_ENTRANCE  = 2;
        /** 城快出口 */
        public static final int CITY_FAST_EXIT      = 3;
        /** 上高架 */
        public static final int UPPER_VIADUCT       = 4;
        /** 上桥 */
        public static final int ON_THE_BRIDGE       = 6;

        public static String getName(int type) {
            return LogNameUtil.getName(type, GuideType.class);
        }
    }

    /**
     * 电子眼的类型
     */
    public static class ElectronicEyeType {
        /** 闯红灯拍照 */
        public static final int TRAFFIC_LIGHT               = 1;
        /** 固定测速 */
        public static final int FIXED_SPEED                 = 2;
        /** 流动测速 */
        public static final int FLOW_SPEED                  = 3;
        /** 区间测速 */
        public static final int SECTION_SPEED               = 4;
        /** 压线拍照 */
        public static final int PRESS_LINE_PHOTOGRAPH       = 6;
        /** 监控拍照 */
        public static final int MONITOR_PHOTOGRAPH          = 7;
        /** 违章拍照 */
        public static final int ILLEGAL_PHOTOGRAPH          = 8;
        /** 公交车道拍照 */
        public static final int BUS_LANE_PHOTOGRAPH         = 9;
        /** 专用摄像头拍照 */
        public static final int SPECIAL_PHOTOGRAPH          = 10;
        /** 禁止停车 */
        public static final int NO_STOPPING                 = 11;
        /** 隧道拍照 */
        public static final int TUNNEL_PHOTOGRAPH           = 12;
        /** 高架桥出口拍照 */
        public static final int VIADUCT_EXIT_PHOTOGRAPH     = 13;
        /** 高架桥入口拍照 */
        public static final int VIADUCT_ENTRANCE_PHOTOGRAPH = 14;
        /** 高速出口拍照 */
        public static final int HIGH_SPEED_EXIT_PHOTOGRAPH  = 15;
        /** 交通遵守路段 */
        public static final int TRAFFIC_COMPLIANCE          = 16;
        /** 警车常出没路段 */
        public static final int POLICE_CAR_ROAD             = 17;
        /** 雷达测速 */
        public static final int RADAR_VELOCITY_MEASUREMENT  = 18;
        /** 其他类型电子眼 */
        public static final int OTHER_TYPE_PHOTOGRAPH       = 19;
        /** 铁道路口 */
        public static final int RAILWAY_INTERSECTION        = 100;
        /** 事故多发路段 */
        public static final int ACCIDENT_WAY                = 101;
        /** 急下坡路段 */
        public static final int RAPID_DOWN_WAY              = 102;
        /** 学校路段 */
        public static final int SCHOOL_ROAD                 = 103;
        /** 急转弯 */
        public static final int RAPID_TURN                  = 104;
        /** 山区路段 */
        public static final int HILL_WAY                    = 105;
        /** 其他类型警示点 */
        public static final int WARNING_OTHER               = 106;

        public static String getName(int type) {
            return LogNameUtil.getName(type, ElectronicEyeType.class);
        }
    }

    /**
     * 导航引导类型的Event工具类
     */
    public static class EventNavigationType {
        /** 导航引导类型定义{@link TwelveClockType} */
        public int mTwelveClock;
        /**
         * 引导类型id mTwelveClock=TwelveClockType.NORMAL {@link NormalTurnId} </br>
         * mTwelveClock=TwelveClockType.TwelveClockType {@link TwelveClockTurnId}
         */
        public int mTurnID;
        /** 引导类型，背景图片 {@link TwelveClockTurnId} */
        public int[] mArrayTurn;
        /** 引导点属性 {@link IVINavigation.GuideType} */
        public int mGuideType;
        /** 到下一个引导点的距离，单位 m */
        public int mDistance;
        /** 到目的地的距离，单位 m */
        public int mDestDistance;
        /** 到目的地所需要的时间，单位 s */
        public int mDestTime;
        /** 道路名称 */
        public String mRoadName;
        /** 下一个道路的名称 */
        public String mNextRoadName;
        /** 目标地名称 */
        public String mDestName; // 目标地名称

        public EventNavigationType(int twelveClock, int turnID, int[] arrayTurn,
                                   int guideType, int distance, int destDistance,
                                   int destTime, String roadName, String nextRoadName, String destName) {
            mTwelveClock = twelveClock;
            mTurnID = turnID;
            mArrayTurn = arrayTurn;
            mGuideType = guideType;
            mDistance = distance;
            mDestDistance = destDistance;
            mDestTime = destTime;
            mRoadName = roadName;
            mNextRoadName = nextRoadName;
            mDestName = destName;
        }

        public String toString() {
            return LogNameUtil.toString(this);
        }
    }

    /**
     * 导航区域发生改变，event通知消息类
     */
    public static class EventNavigationAddress {
        /** 省 */
        public String mProvince;
        /** 市 */
        public String mCity;
        /** 区域 */
        public String mCounty;

        public EventNavigationAddress(String province, String city, String county) {
            mProvince = province;
            mCity = city;
            mCounty = county;
        }
    }

    /**
     * 导航指南针指向改变，event通知消息类
     */
    public static class EventNavigationGuide {
        /** 指南针方向 */
        public int mDirection;

        public EventNavigationGuide(int direction) {
            mDirection = direction;
        }
    }

    /**
     * 电子眼信息提示，event通知消息类
     */
    public static class EventNavigationEyeInfo {
        /** 电子眼类型，{@link ElectronicEyeType} */
        public int mType;
        /** 下一个电子眼的距离，单位 m */
        public int mDistance;
        /** 限速多少 */
        public int mSpeedLimit;

        public EventNavigationEyeInfo(int type, int distance, int speedLimit) {
            mType = type;
            mDistance = distance;
            mSpeedLimit = speedLimit;
        }
    }
}
