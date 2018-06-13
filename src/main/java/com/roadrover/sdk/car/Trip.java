package com.roadrover.sdk.car;

import com.roadrover.sdk.utils.LogNameUtil;

public class Trip {
    /**
     * 里程ID
     */
    public static class Id {
        /** 总里程 */
        public static final int TOTAL = 0;
        /** 从车辆启动 */
        public static final int START = 1;
        /** 里程A */
        public static final int A = 2;
        /** 里程B */
        public static final int B = 3;
        /** 里程C */
        public static final int C = 4;
    }

    /**
     * 参数值在float[]中的索引
     */
    public static class Index {
        /** 距离km */
        public static final int DISTANCE             = 0;
        /** 时间分钟 */
        public static final int TIME_MINUTES         = 1;
        /** 平均油耗 */
        public static final int AVG_FUEL_CONSUMPTION = 2;
        /** 平均速度 */
        public static final int AVG_SPEED            = 3;
        /** 距离0.1km */
        public static final int DIST_DISTANCE        = 4;
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

    /**
     * 将类打印成String
     * @return
     */
    @Override
    public String toString() {
        return "Id:" + LogNameUtil.getName(mId, Id.class) +
                " index:" + LogNameUtil.getName(mIndex, Index.class) +
                " value:" + mValue;
    }
}