package com.roadrover.sdk.cluster;

/**
 * 仪表通信的定义数据.
 */

public class IVICluster {

    /**
     * 和仪表通信的ID
     */
    public static class ID {
        /** 未定义仪表通信功能 */
        public static final int UNKNOWN = -1;
        /** 通过MCU和仪表进行通信 */
        public static final int MCU = 0;
        /** 通过I2C和仪表进行通信 */
        public static final int I2C = 1;
    }
}
