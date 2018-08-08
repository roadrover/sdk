package com.roadrover.sdk.cluster;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * 仪表通信的定义数据.
 */

public class IVICluster {

    /**
     * 和仪表通信的ID
     */
    public static class ID {
        /**
         * 未定义仪表通信功能
         */
        public static final int UNKNOWN = -1;
        /**
         * 通过MCU和仪表进行通信
         */
        public static final int MCU = 0;
        /**
         * 通过I2C和仪表进行通信
         */
        public static final int I2C = 1;
        /**
         * 通过SPI和仪表进行通信
         */
        public static final int SPI = 2;
        /**
         * 通过以太网和仪表进行通信
         */
        public static final int ETHERNET = 3;
    }


    /**
     * 互动APP类型
     */
    public static class AppType {

        /**
         * 音乐类型，其值为 {@value}
         */
        public static final int MUSIC = 0;

        /**
         * 收音机类型，其值为 {@value}
         */
        public static final int RADIO = 1;
        /**
         * 蓝牙类型，其值为 {@value}
         */
        public static final int BLUETOOTH = 2;

    }

    /**
     * 控制类型Event
     */
    public static class EventControl {
        public static class Action {
            public static final int UP = 0;
            public static final int DOWN = 1;
        }

        /**
         * app 类型 {@link AppType}
         */
        public int mAppType;
        public int mAction;

        public EventControl(int appType, int action) {
            mAppType = appType;
            mAction = action;
        }

        public static String getName(int action) {
            return LogNameUtil.getName(action, Action.class);
        }

    }
}
