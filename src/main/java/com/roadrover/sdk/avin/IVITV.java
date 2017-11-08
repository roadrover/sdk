package com.roadrover.sdk.avin;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * TV定义.
 */

public class IVITV {

    /**
     * TV模块
     */
    public static class Module {
        /**
         * 无, 其值 {@value}
         */
        public static final int NONE = 0;
        /**
         * 瑞信达, 其值 {@value}
         */
        public static final int RISHTA = 1;

        /**
         * 获取名字
         * @param module 模块，见{@link Module}
         * @return
         */
        public static String getName(int module) {
            return LogNameUtil.getName(module, Module.class);
        }
    }

    /**
     * TV控制码
     */
    public static class Control {
        /**
         * 开/关机, 其值 {@value}
         */
        public static final int POWER = 1;
        /**
         * 切入TV, 其值 {@value}
         */
        public static final int TV = 2;
        /**
         * 收音机, 其值 {@value}
         */
        public static final int RADIO = 3;
        /**
         * 搜索, 其值 {@value}
         */
        public static final int SEARCH = 4;
        /**
         * 静音, 其值 {@value}
         */
        public static final int MUTE = 5;
        /**
         * 数字1, 其值 {@value}
         */
        public static final int NUM1 = 6;
        /**
         * 数字2, 其值 {@value}
         */
        public static final int NUM2 = 7;
        /**
         * 数字3, 其值 {@value}
         */
        public static final int NUM3 = 8;
        /**
         * 数字4, 其值 {@value}
         */
        public static final int NUM4 = 9;
        /**
         * 数字5, 其值 {@value}
         */
        public static final int NUM5 = 10;
        /**
         * 数字6, 其值 {@value}
         */
        public static final int NUM6 = 11;
        /**
         * 数字7, 其值 {@value}
         */
        public static final int NUM7 = 12;
        /**
         * 数字8, 其值 {@value}
         */
        public static final int NUM8 = 13;
        /**
         * 数字9, 其值 {@value}
         */
        public static final int NUM9 = 14;
        /**
         * 数字0, 其值 {@value}
         */
        public static final int NUM0 = 15;
        /**
         * 重拨, 其值 {@value}
         */
        public static final int RECALL = 16;
        /**
         * TTX, 其值 {@value}
         */
        public static final int TTX = 17;
        /**
         * FAV, 其值 {@value}
         */
        public static final int FAV = 18;
        /**
         * SUB, 其值 {@value}
         */
        public static final int SUB = 19;
        /**
         * 上一台, 其值 {@value}
         */
        public static final int CH_ADD = 20;
        /**
         * EPG, 其值 {@value}
         */
        public static final int EPG = 21;
        /**
         * 信息, 其值 {@value}
         */
        public static final int INFO = 22;
        /**
         * 降低音量, 其值 {@value}
         */
        public static final int VOL_SUB = 23;
        /**
         * 确认, 其值 {@value}
         */
        public static final int ENTER = 24;
        /**
         * 增大音量, 其值 {@value}
         */
        public static final int VOL_ADD = 25;
        /**
         * 音效, 其值 {@value}
         */
        public static final int AUDIO = 26;
        /**
         * 菜单, 其值 {@value}
         */
        public static final int MENU = 27;
        /**
         * 下一台, 其值 {@value}
         */
        public static final int CH_SUB = 28;
        /**
         * 退出, 其值 {@value}
         */
        public static final int EXIT = 29;
        /**
         * 红, 其值 {@value}
         */
        public static final int RED = 30;
        /**
         * 绿, 其值 {@value}
         */
        public static final int GREEN = 31;
        /**
         * 黄, 其值 {@value}
         */
        public static final int YELLOW = 32;
        /**
         * 蓝, 其值 {@value}
         */
        public static final int BLUE = 33;

        /**
         * 获取名字
         * @param control 控制命令，见{@link Control}
         * @return
         */
        public static String getName(int control) {
            return LogNameUtil.getName(control, Control.class);
        }
    }
}
