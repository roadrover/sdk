package com.roadrover.sdk.system;

/**
 * 按键可配置的项的定义类
 */

public class IVIKey {

    /**
     * 长按定义 </br>
     * 例：增加音量按键需要配成长按，可以 add_volume,long_press
     */
    public static class LongKeyDef {
        /** 长按 */
        public static final String LONG_KEY_PRESS = "long_press";
        /** 长按分割符 */
        public static final String LONG_KEY_SPLIT = ",";
    }

    /**
     * mode按键的特殊操作定义类</br>
     * 如果需要mode按键的特殊配置，可以参考该位置 </br>
     * 例：mode按键操作，蓝牙断开，不允许进入蓝牙音乐，可以配置为： </br>
     * [ModeControl] </br>
     * APP0=com.roadrover.music/com.roadrover.music.activity.BTMusicActivity:Bluetooth
     */
    public static class ModeKeySpecialOperation {
        /** 必须存在盘符，才能在mode键打开该app */
        public static final String MUST_HAVE_DISK = ":Disk";
        /** 必须存在U盘，才能在mode键打开该app */
        public static final String MUST_HAVE_UDISK = ":UDisk";
        /** 必须连接蓝牙，才能mode键打开该app */
        public static final String MUST_CONNECT_BLUETOOTH = ":Bluetooth";
    }

    /**
     * 打开app特殊操作定义 </br>
     * 例：如果不在收音机界面，执行打开收音机，如果在收音机界面，执行系统按键87，可配置为： </br>
     * com.roadrover.radio_v2;top:87 </br>
     * 或者不在收音机界面执行打开收音机，在收音机界面执行搜索电台，可配置为：</br>
     * com.roadrover.radio_v2;top:scan </br>
     */
    public static class AppKeyDef {
        /** 是否配置了APP在顶层时的功能 */
        public static final String TOP_KEY_PRESS = ";top:";
    }

    /**
     * power 按键动作定义 </br>
     * 例：短按关屏，暂停媒体，长按重启，可配置为： </br>
     * [Power]
     * LONG_ACTION=9                       #长按重启 9为长按重启，2 关背光，暂停媒体， 5 静音
     * SHORT_ACTION=2                      #关背光、暂停媒体
     */
    public static class PowerKeyAction {
        public static final int NONE = 0; // 不动作
        public static final int BKL_OFF = 1; // 关背光
        public static final int BKL_OFF_AND_PAUSE_MEDIA = 2; // 关背光、暂停媒体
        public static final int BKL_OFF_AND_QUIT_APP = 3; // 关背光、结束当前媒体应用,
        public static final int MUTE_AND_QUIT_APP_AND_CLOCK = 4; // 静音、结束应用、进入时钟界面
        public static final int MUTE = 5; // 静音
        public static final int REBOOT = 9; // 复位

        public static String getDesc(int action) {
            switch (action) {
                case NONE:                              return "NONE";
                case BKL_OFF:                           return "BackLight OFF";
                case BKL_OFF_AND_PAUSE_MEDIA:           return "BackLight OFF, Mute, Pause Media";
                case BKL_OFF_AND_QUIT_APP:              return "BackLight OFF, Mute, Quit App";
                case MUTE_AND_QUIT_APP_AND_CLOCK:       return "Mute, Quit App, Enter Clock UI";
                case MUTE:                              return "Mute";
                case REBOOT:                            return "Reboot";
                default:
                    return "Unknown action " + action;
            }
        }
    }
}
