package com.roadrover.sdk.car;

public class Trip {
    /**
     * 里程ID
     */
    public static class Id {
        public static final int TOTAL = 0;          //总里程
        public static final int START = 1;          //从车辆启动
        public static final int A = 2;              //里程A
        public static final int B = 3;              //里程B
        public static final int C = 4;              //里程C
    }

    /**
     * 参数值在float[]中的索引
     */
    public static class Index {
        public static final int DISTANCE = 0;               //距离km
        public static final int TIME_MINUTES = 1;           //时间分钟
        public static final int AVG_FUEL_CONSUMPTION = 2;   //平均油耗
        public static final int AVG_SPEED = 3;              //平均速度
        public static final int DIST_DISTANCE = 4;          //距离0.1km
    }

    public int mId;
    public int mIndex;
    public float mValue;    //具体值

    public Trip(int id, int index, float value) {
        mId = id;
        mIndex = index;
        mValue = value;
    }

    public static int getKey(int id, int index) {
        return (id << 8 | index);
    }

    public static float getUnknownValue(int id) {
        return -1.0f;
    }
}