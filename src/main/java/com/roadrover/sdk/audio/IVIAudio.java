package com.roadrover.sdk.audio;

import com.roadrover.sdk.utils.LogNameUtil;

import java.util.ArrayList;

/**
 * 音频相关定义
 */
public class IVIAudio {
    /**
     * 该定义必须和services-jni里面的AudioDevice.h里面的一致
     */
    public static class ChipId {
        /**
         * AK7601，其值为 {@value}
         */
        public static final int AK7601 = 1;
        /**
         * BD37534，其值为 {@value}
         */
        public static final int BD37534 = 2;
    }

    /**
     * 车载喇叭的定义，必须和services-jni里面的AudioDevice.h里面的一致
     */
    public static class Speaker {
        /**
         * 前左，其值为 {@value}
         */
        public static final int FL = 0;
        /**
         * 前右，其值为 {@value}
         */
        public static final int FR = 1;
        /**
         * 后左，其值为 {@value}
         */
        public static final int RL = 2;
        /**
         * 后右，其值为 {@value}
         */
        public static final int RR = 3;
        /**
         * TODO: 待补充，其值为 {@value}
         */
        public static final int SWL = 4;
        /**
         * TODO: 待补充，其值为 {@value}
         */
        public static final int SWR = 5;
    }

    /**
     * 通道切换类的定义
     */
    public static class Channel {
        /**无，其值为 {@value}*/
        public static final int NONE                    = 0;
        /**系统，其值为 {@value}*/
        public static final int PC                      = 1;
        /**系统音乐，其值为 {@value}*/
        public static final int PC_MUSIC                = 3;
        /**系统视频，其值为 {@value}*/
        public static final int PC_VIDEO                = 4;
        /**声控与TTS，其值为 {@value}*/
        public static final int PC_VOICE                = 5;
        /**大巴乘客区（第二路PC通道），其值为 {@value}*/
        public static final int PC_SECONDARY            = 6;

        /**收音机，其值为 {@value}*/
        public static final int RADIO                   = 11;
        /**收音机交通广播提醒，其值为 {@value}*/
        public static final int RADIO_TA                = 12;

        /**TV，其值为 {@value}*/
        public static final int TV                      = 21;
        /**TV2，其值为 {@value}*/
        public static final int TV2                     = 22;
        /**AUX，其值为 {@value}*/
        public static final int AUX                     = 23;
        /**AUX2，其值为 {@value}*/
        public static final int AUX2                    = 24;
        /**AV，其值为 {@value}*/
        public static final int AV                      = 25;
        /**AV2，其值为 {@value}*/
        public static final int AV2                     = 26;
        /**AV3，其值为 {@value}*/
        public static final int AV3                     = 27;
        /**AV4，其值为 {@value}*/
        public static final int AV4                     = 28;
        /**麦克风，其值为 {@value}*/
        public static final int ANNOUNCE_MIC            = 29;

        /**蓝牙电话，其值为 {@value}*/
        public static final int BLUETOOTH_TEL           = 31;
        /**蓝牙来电铃声，其值为 {@value}*/
        public static final int BLUETOOTH_RING          = 32;
        /**蓝牙音乐，其值为 {@value}*/
        public static final int BLUETOOTH_A2DP          = 33;

        /**安吉星等车载电话通道，其值为 {@value}*/
        public static final int THIRDPARTY_CAR_TEL      = 41;
        /**嘀嘀虎，其值为 {@value}*/
        public static final int THIRDPARTY_DDH          = 42;

        /**CarPlay媒体，其值为 {@value}*/
        public static final int APPLE_CARPLAY_MEDIA     = 51;
        /**CarPlay闹钟，其值为 {@value}*/
        public static final int APPLE_CARPLAY_ALARM     = 52;
        /**CarPlay语音，其值为 {@value}*/
        public static final int APPLE_CARPLAY_SIRI      = 53;
        /**CarPlay电话，其值为 {@value}*/
        public static final int APPLE_CARPLAY_TEL       = 64;
        /**CarPlay USB iPod，其值为 {@value}*/
        public static final int APPLE_USB_IPOD          = 59;

        /**AndroidAuto媒体，其值为 {@value}*/
        public static final int ANDROID_AUTO_MEDIA      = 61;
        /**AndroidAuto闹钟，其值为 {@value}*/
        public static final int ANDROID_AUTO_ALARM      = 62;
        /**AndroidAuto语音，其值为 {@value}*/
        public static final int ANDROID_AUTO_VOICE      = 63;
        /**AndroidAuto电话，其值为 {@value}*/
        public static final int ANDROID_AUTO_TEL        = 64;

        /**混音通道，其值为 {@value}*/
        public static final int MONO                    = 70;

        /**{@link IVIAudio.Channel}*/
        public int mId;
        /**{@link IVIAudio.Channel}的音量值*/
        public float mValue;
        /**{@link IVIAudio.Channel}的音量最小值*/
        public float mMin;
        /**{@link IVIAudio.Channel}的音量最大值*/
        public float mMax;
        /**{@link IVIAudio.Channel}的音量默认值*/
        public float mDefault;

        /**
         * 获取声音通道的描述名称
         * @param channel {@link IVIAudio.Channel}
         * @return
         */
        public static String getName(int channel) {
            return LogNameUtil.getName(channel, Channel.class, "Unknown audio channel: " + channel);
        }

        /**
         * 获取所有的声音通道
         * @return
         */
        public static ArrayList<Integer> getChannels() {
            return LogNameUtil.getFields(Channel.class);
        }

        /**
         * 根据通道描述名称获取声音通道的值
         * @param name 声音通道描述名称
         * @return
         */
        public static int getChannel(String name) {
            return LogNameUtil.getValue(Channel.class, name, NONE);
        }

        /**
         * 构造函数
         * @param id {@link #mId}
         * @param min {@link #mMin}
         * @param max {@link #mMax}
         * @param defaultValue {@link #mDefault}
         */
        public Channel(int id, float min, float max, float defaultValue) {
            mId = id;
            mMin = min;
            mMax = max;
            mDefault = defaultValue;
            mValue = defaultValue;
        }

        /**
         * 设置通道的声音值
         * @param value
         * @return
         */
        public boolean set(float value) {
            float nextValue;
            if (value < mMin) {
                nextValue = mMin;
            } else if(value > mMax) {
                nextValue = mMax;
            } else {
                nextValue = value;
            }

            if (mValue != nextValue) {
                mValue = nextValue;
                return true;
            } else {
                return false;
            }
        }

        /**
         * 是否只读
         * @return
         */
        public boolean isReadOnly() {
            return (mMax == mMin);
        }
    }

    /**
     * 音量发生变化
     */
    public static class EventVolumeChanged {
        public int mId;
        public int mValue;
        public int mMax;
        public EventVolumeChanged(int id, int value) {
            mId = id;
            mValue = value;
        }
        public EventVolumeChanged(int id, int value, int max) {
            this(id, value);
            mMax = max;
        }
    }

    /**
     * 静音状态发生变化
     */
    public static class EventMuteChanged {
        public boolean mMute;
        public int mSource;
        public EventMuteChanged(boolean mute, int source) {
            mMute = mute;
            mSource = source;
        }
    }

    public static class EventSecondaryMuteChanged {
        public boolean mMute;
        public EventSecondaryMuteChanged(boolean mute) {
            mMute = mute;
        }
    }

    /**
     * 音量条消息
     */
    public static class EventVolumeBar {
        public int mId;
        public int mValue;
        public int mMaxValue;
        public EventVolumeBar(int id, int value, int maxValue) {
            mId = id;
            mValue = value;
            mMaxValue = maxValue;
        }

        public EventVolumeBar(int id) {
            mId = AudioParam.Id.NONE;
        }
    }
}
