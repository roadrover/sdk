package com.roadrover.sdk.audio;

import com.roadrover.sdk.Param;
import com.roadrover.sdk.utils.LogNameUtil;

public class AudioParam extends Param {
    private static final int BALANCE_FADE_BASE = 100;

    /**
     * 音频参数ID定义，必须与jni里面的AudioDevice.h里面的定义一致，
     * 如果在这里修改了值的定义，一定要同步修改AudioDevice.h
     */
    public static class Id {
        public static final int NONE                    = 0;

        /**
         * mute 系统，其值为 {@value}
         */
        public static final int MUTE                    = 1;

        /**
         * mute 次通道静音：后排或者乘客区，其值为 {@value}
         */
        public static final int MUTE_SECONDARY          = 2;

        /**
         * 主音量：DSP或者功放的音量，其值为 {@value}
         */
        public static final int VOLUME_MASTER           = 10;

        /**
         * 安卓系统的媒体音量，相当于 STREAM_MUSIC，其值为 {@value}
         */
        public static final int VOLUME_MEDIA            = 11;

        /**
         * 主音量：DSP或者功放的音量(蓝牙通话的时候)，其值为 {@value}
         */
        public static final int VOLUME_BLUETOOTH        = 12;

        /**
         * 主音量：DSP或者功放的音量(蓝牙铃声的时候)，其值为 {@value}
         */
        public static final int VOLUME_BLUETOOTH_RING   = 13;

        /**
         * 收音机、AV、AUX等媒体下的提示音（包括导航）合成音量，百分比0-100%，其值为 {@value}
         */
        public static final int VOLUME_ALARM            = 14;

        /**
         * 倒车时的媒体音量，百分比0-100%，其值为 {@value}
         */
        public static final int VOLUME_MEDIA_CCD        = 15;

        /**
         * 安卓系统的导航音量：系统定制音频流，其值为 {@value}
         */
        public static final int VOLUME_NAVIGATION       = 16;

        /**
         * 安卓系统的TTS音量，其值为 {@value}
         */
        public static final int VOLUME_TTS              = 17;

        /**
         * 次音量：后排区域，其值为 {@value}
         */
        public static final int VOLUME_SECONDARY        = 18;

        /**
         * 低音，其值为 {@value}
         */
        public static final int BASS                    = 21;

        /**
         * 中音，其值为 {@value}
         */
        public static final int MIDDLE                  = 22;

        /**
         * 高音，其值为 {@value}
         */
        public static final int TREBLE                  = 23;

        /**
         * 高16位是Balance，低16位是Fade，且在其本来的值的基础上加BALANCE_FADE_BASE来消除负数
         * 其值为 {@value}
         */
        public static final int BALANCE_FADE            = 24;

        /**
         * 最佳位置，其值为 {@value}
         */
        public static final int POSITION                = 25;

        /**
         * 专家音效，其值为 {@value}
         */
        public static final int EXPERT_AUDIO_EFFECT     = 26;

        public static final int EQ_COUNT                = 31;

        /**
         * EQ模式，包括  Custom, Rock, Dance, Classic Etc 等，其值为 {@value}
         */
        public static final int EQ_MODE                 = 32;

        public static final int CENTER_SPEAKER          = 41;

        /**
         * 低音炮，其值为 {@value}
         */
        public static final int SUBWOOFER_SPEAKER       = 42;

        /**
         * 环绕音 Surround LS/RS ，其值为 {@value}
         */
        public static final int SURROUND                = 43;

        /**
         * 车速音量补偿，其值为 {@value}
         */
        public static final int ASL                     = 51;

        /**
         * 原车功放音量
         */
        public static final int CAR_AMP_VOLUME          = 101;
        public static final int CAR_AMP_BASS            = 102;
        public static final int CAR_AMP_MIDDLE          = 103;
        public static final int CAR_AMP_TREBLE          = 104;
        public static final int CAR_AMP_ASL             = 105;
        public static final int CAR_AMP_ASL_TYPE        = 106;

        public static final int EQ_BAND0_GAIN           = 1000;
        public static final int EQ_BAND9_GAIN           = 1009;
        public static final int EQ_BAND0_Q              = 1020;
        public static final int EQ_BAND9_Q              = 1029;
        public static final int EQ_BAND0_CENTER_FREQ    = 1040;
        public static final int EQ_BAND9_CENTER_FREQ    = 1049;

        /**
         * 获取十段EQ增益的 id
         * @param band 0-9
         * @return
         */
        public static int getEqGainId(int band) {
            return EQ_BAND0_GAIN + band;
        }

        /**
         * 获取中心频点的id
         * @param band 0-9
         * @return
         */
        public static int getEqCenterFreqId(int band) {
            return EQ_BAND0_CENTER_FREQ + band;
        }

        /**
         * 获取十段EQ的Q值的id
         * @param band 0-9
         * @return
         */
        public static int getEqQId(int band) {
            return EQ_BAND0_Q + band;
        }

        /**
         * 打印获取名字，通过id将名字打印出来
         * @param id
         * @return
         */
        public static String getName(int id) {
            return LogNameUtil.getName(id, Id.class, "Audio Param id " + String.valueOf(id));
        }

        /**
         * 判断该id是否在10段EQ增益的 id 里面
         * @param id
         * @return
         */
        public static boolean isEqGainId(int id) {
            return (id >= EQ_BAND0_GAIN && id <= EQ_BAND9_GAIN);
        }

        /**
         * 判断该id是否是调节音量条的id
         * @param id
         * @return
         */
        public static boolean isVolumeId(int id) {
            return (id == VOLUME_MASTER
                    || id == VOLUME_MEDIA
                    || id == VOLUME_BLUETOOTH
                    || id == VOLUME_BLUETOOTH_RING
                    || id == VOLUME_ALARM);
        }
    }

    /**
     * EQ模式
     */
    public static class EqMode {
        public static final int START = 1;

        /**
         * 用户自定义模式，其值为{@value}
         */
        public static final int USER = 1;

        /**
         * 用户模式模式，其值为{@value}
         */
        public static final int DEFAULT = 2;

        /**
         * 摇滚模式，其值为{@value}
         */
        public static final int ROCK = 3;

        /**
         * 经典模式，其值为{@value}
         */
        public static final int CLASSICAL = 4;

        /**
         * 流行模式，其值为{@value}
         */
        public static final int POP = 5;

        /**
         * 现场模式，其值为{@value}
         */
        public static final int LIVE = 6;

        /**
         * 舞曲模式，其值为{@value}
         */
        public static final int DANCE = 7;

        /**
         * 柔和模式，其值为{@value}
         */
        public static final int SOFT = 8;
        public static final int END = 8;

        /**
         * 通过eqMode获取他的名字，一般用于打印log
         * @param eqMode 8 则返回 SOFT
         * @return
         */
        public static String getName(int eqMode) {
            return LogNameUtil.getName(eqMode, EqMode.class, "EQ mode " + eqMode);
        }
    }

    /**
     * 车速音量补偿级别定义类
     */
    public static class AslLevel {

        /**
         * 关闭车速音量补偿，其值为{@value}
         */
        public static final int OFF = 0;

        /**
         * 低，其值为{@value}
         */
        public static final int LOW = 1;

        /**
         * 中，其值为{@value}
         */
        public static final int MIDDLE = 2;

        /**
         * 高，其值为{@value}
         */
        public static final int HIGH = 3;

        /**
         * 最小，其值为{@value}
         */
        public static final int MIN = OFF;

        /**
         * 最大，其值为{@value}
         */
        public static final int MAX = HIGH;
    }

    /**
     * 最佳位置定义
     */
    public static class Position {
        public static final int FL = 0;
        public static final int FR = 1;
        public static final int RL = 2;
        public static final int RR = 3;
        public static final int CENTER = 4;
        public static final int UNKNOWN = -1;   //硬件不支持最佳位置

        public static final int MIN = FL;
        public static final int MAX = CENTER;
        public static String getName(int position) {
            switch (position) {
                case FL:        return "FL";
                case FR:        return "FL";
                case RL:        return "FL";
                case RR:        return "FL";
                case CENTER:    return "CENTER";
                default:        return "Unknown position " + position;
            }
        }
    }

    /**
     * 专家音效的场景定义
     */
    public static class ExpertAudioEffect {

        /**
         * 关闭专家音效，值为{@value}
         */
        public static final int OFF = -1;

        /**
         * 主驾，值为{@value}
         */
        public static final int DRIVER = 0;

        /**
         * 副驾，值为{@value}
         */
        public static final int PASSENGER = 1;

        /**
         * 全车，值为{@value}
         */
        public static final int WHOLE_CAR = 2;

        /**
         * RCA模式，值为{@value}
         */
        public static final int AUDITION_ROOM = 3;

        /**
         * 同轴光纤模式，值为{@value}
         */
        public static final int SPDIF = 4;

        /**
         * 试音房模式，值为{@value}
         */
        public static final int RCA = 5;

        /**
         * 自定义，值为{@value}
         */
        public static final int CUSTOM = 6;
        public static final int MIN = OFF;
        public static final int MAX = CUSTOM;

        /**
         * 通过effect获取他的名字，一般用于打印log
         * @param effect 6 则返回 CUSTOM
         * @return
         */
        public static String getName(int effect) {
            return LogNameUtil.getName(effect, ExpertAudioEffect.class, "Unknown expert audio effect " + effect);
        }
    }

    /**
     * 构造函数
     * @param id {@link AudioParam.Id}
     * @param min 当前id的值的最小值，比如音量，最小值0
     * @param max 当前id的值的最大值，比如主音量，最大值30
     * @param defaultValue 默认的音量值
     */
    public AudioParam(int id, int min, int max, int defaultValue) {
        super(id, min, max, defaultValue);
    }

    /**
     * 构造函数，布尔型的id，最大值为1，最小值为0
     * @param id {@link AudioParam.Id}
     * @param defaultValue 默认的值
     */
    public AudioParam(int id, boolean defaultValue) {
        super(id, defaultValue);
    }

    /**
     * 构造函数，只读的参数类型
     * @param id  {@link AudioParam.Id}
     * @param value 默认的值
     */
    public AudioParam(int id, int value) {
        super(id, value);
    }

    /**
     * 返回当前参数id的名字
     * @return
     */
    public String getName() {
        return Id.getName(mId);
    }

    /**
     * 通过值获取频率点，会自动转换成 Hz和KHz
     * @return
     */
    public String getFreqText() {
        if (mValue < 1000) {
            return mValue + " Hz";
        } else {
            return mValue/1000 + " KHz";
        }
    }

    public static int getBalance(int value) {
        return ((value & 0xFFFF0000) >> 16) - BALANCE_FADE_BASE;
    }

    public static int getFade(int value) {
        return (value & 0xFFFF) - BALANCE_FADE_BASE;
    }

    public static int getBalanceFadeCombine(int balance, int fade) {
        return ((balance + BALANCE_FADE_BASE) << 16) | (fade + BALANCE_FADE_BASE);
    }

    /**
     * 设置参数值
     * @param value
     * @return 设置成功返回true，失败返回false
     */
    @Override
    public boolean set(int value) {
        if (mId == Id.BALANCE_FADE) {
            int balance = getBalance(value);
            int fade = getFade(value);

            if (balance < mMin) {
                balance = mMin;
            } else if(balance > mMax) {
                balance = mMax;
            }

            if (fade < mMin) {
                fade = mMin;
            } else if(fade > mMax) {
                fade = mMax;
            }

            int nextValue = getBalanceFadeCombine(balance, fade);
            if (mValue != nextValue) {
                mValue = nextValue;
                return true;
            } else {
                return false;
            }
        } else {
            return super.set(value);
        }
    }

    public static String getValueString(int id, int value) {
        if (id == Id.BALANCE_FADE) {
            int balance = getBalance(value);
            int fade = getFade(value);
            return "balance " + balance + ", fade " + fade;
        } else {
            return Integer.toString(value);
        }
    }
}
