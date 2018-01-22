package com.roadrover.sdk.dab;

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
        public static final int DAB_ORIGINAL = 0x0; //friendly name: 'DAB original', ordinal=0
        public static final int FM_BACKUP = 0x1; //friendly name: 'FM Backup', ordinal=1
        public static final int DAB_ALTERNATIVE = 0x2; //friendly name: 'DAB alternative', ordinal=2

        public static String getName(int status) {
            switch (status) {
                case DAB_ORIGINAL:
                    return "DAB original";
                case FM_BACKUP:
                    return "FM Backup";
                case DAB_ALTERNATIVE:
                    return "DAB alternative";
                default:
                    return "Unknown status: " + status;
            }
        }
    }

    /**
     * 服务链接模式
     */
    public static class ServiceLinkingMode {
        public static final int NONE = 0x00;
        public static final int DABFM = 0x01;     // dab-fm自动无缝连接（转播）
        public static final int DABDAB = 0x02;    // dab-dab自动无缝连接（可选服务）(only for dual tuner systems)
        public static final int DABFMDAB = 0x03;  // (only for dual tuner systems)

        public static String getName(int mode) {
            switch (mode) {
                case NONE:
                    return "None";
                case DABFM:
                    return "DAB-FM";
                case DABDAB:
                    return "DAB-DAB";
                case DABFMDAB:
                    return "DAB-FM-DAB";
                default:
                    return "Unknown mode: " + mode;
            }
        }
    }

    /**
     * 烧录软件来源
     */
    public static class FirmwareDestination {
        public static final int SDRAM = 0;
        public static final int FLASH = 1;

        public static String getName(int destination) {
            switch (destination) {
                case SDRAM:
                    return "SD-RAM";
                case FLASH:
                    return "Flash";
                default:
                    return "Unknown destination: " + destination;
            }
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
            switch (result) {
                case SUCCESSFUL:
                    return "Successful";
                case ERROR_NO_MEMORY:
                    return "Error no memory";
                case ERROR_BOOTLOADER_NOT_FOUND:
                    return "Error bootloader not found";
                case ERROR_SPI_CONFIG_FAILURE:
                    return "Error SPI config failure";
                case ERROR_SECONDARY_BOOTLOAD_WRITE:
                    return "Error secondary bootloader write";
                case ERROR_IMAGE_WRITE:
                    return "Error image write";
                case ERROR_MIDDLEWARE_RUNNING:
                    return "Error middleware running";
                case ERROR_OTHER:
                    return "Error other";
                default:
                    return "Unknown result: " + result;
            }
        }
    }

    public static class EventSystemStateChanged {
        public int mSystemState;
        EventSystemStateChanged(int systemState) {
            mSystemState = systemState;
        }
    }
}
