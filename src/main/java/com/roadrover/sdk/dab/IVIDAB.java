package com.roadrover.sdk.dab;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * 数字音频广播定义.
 */

public class IVIDAB {

    /**
     * DAB 芯片
     */
    public static class DABChip {
        public static final int NONE = 0;
        public static final int NXP_SAF3602 = 1;

        public static String getName(int chip) {
            switch (chip) {
                case NONE: return "None";
                case NXP_SAF3602: return "NXP SAF3602";
                default:
                    return "Unknown chip: " + chip;
            }
        }
    }

    /**
     * DAB运行状态
     */
    public static class SystemState {
        public static final int STOPPED = 0x0;
        public static final int RUNNING = 0x1;
        public static final int INIT_SUCCESSFUL = 0x2;
        public static final int DOWNLOAD_SDRAM = 0x3;
        public static final int DOWNLOAD_FLASH = 0x4;
        public static final int DOWNLOAD_SUCCESSFUL = 0x5;
        public static final int TUNE_IN_PROGRESS = 0x6;
        public static final int BACKGROUND_SCAN_STARTED = 0x7;
        public static final int BACKGROUND_SCAN_FINISHED = 0x8;

        public static String getName(int state) {
            switch (state) {
                case STOPPED:
                    return "Stopped";
                case RUNNING:
                    return "Running";
                case INIT_SUCCESSFUL:
                    return "Init successful";
                case DOWNLOAD_SDRAM:
                    return "Download SDRam";
                case DOWNLOAD_FLASH:
                    return "Download flash";
                case DOWNLOAD_SUCCESSFUL:
                    return "Download successful";
                case TUNE_IN_PROGRESS:
                    return "Tune in progress";
                case BACKGROUND_SCAN_STARTED:
                    return "Background scan started";
                case BACKGROUND_SCAN_FINISHED:
                    return "Background scan finished";
                default:
                    return "Unknown state: " + state;
            }
        }
    }

    /**
     * 服务链接状态
     */
    public static class ServiceLinkingStatus {
        /** friendly name: 'DAB original', ordinal=0 */
        public static final int DAB_ORIGINAL = 0x0;
        /** friendly name: 'FM Backup', ordinal=1 */
        public static final int FM_BACKUP    = 0x1;
        /** friendly name: 'DAB alternative', ordinal=2 */
        public static final int DAB_ALTERNATIVE = 0x2;

        public static String getName(int status) {
            return LogNameUtil.getName(status, ServiceLinkingStatus.class);
        }
    }

    /**
     * 服务链接模式
     */
    public static class ServiceLinkingMode {
        public static final int NONE     = 0x00;
        /** dab-fm自动无缝连接（转播） */
        public static final int DABFM    = 0x01;
        /** dab-dab自动无缝连接（可选服务）(only for dual tuner systems) */
        public static final int DABDAB   = 0x02;
        /** (only for dual tuner systems) */
        public static final int DABFMDAB = 0x03;

        public static String getName(int mode) {
            return LogNameUtil.getName(mode, ServiceLinkingStatus.class);
        }
    }

    /**
     * 烧录软件来源
     */
    public static class FirmwareDestination {
        public static final int SDRAM = 0;
        public static final int FLASH = 1;

        public static String getName(int destination) {
            return LogNameUtil.getName(destination, FirmwareDestination.class);
        }
    }

    /**
     * 软件烧录结果
     */
    public static class DownloadFirmwareResult {
        public static final int SUCCESSFUL = 0;
        public static final int ERROR_NO_MEMORY = 1;
        public static final int ERROR_BOOTLOADER_NOT_FOUND = 2;
        public static final int ERROR_SPI_CONFIG_FAILURE = 3;
        public static final int ERROR_SECONDARY_BOOTLOAD_WRITE = 4;
        public static final int ERROR_IMAGE_WRITE = 5;
        public static final int ERROR_MIDDLEWARE_RUNNING = 6;
        public static final int ERROR_OTHER = 99;

        public static String getName(int result) {
            return LogNameUtil.getName(result, DownloadFirmwareResult.class);
        }
    }

    public static class EventSystemStateChanged {
        public int mSystemState;

        EventSystemStateChanged(int systemState) {
            mSystemState = systemState;
        }
    }
}
