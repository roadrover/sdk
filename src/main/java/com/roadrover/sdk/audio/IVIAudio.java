package com.roadrover.sdk.audio;

import com.roadrover.sdk.utils.LogNameUtil;

import java.util.ArrayList;

public class IVIAudio {
    // 该定义必须和services-jni里面的AudioDevice.h里面的一致
    public static class ChipId {
        public static final int AK7601 = 1;
        public static final int BD37534 = 2;
    }

    /**
     * 车载喇叭的定义，必须和services-jni里面的AudioDevice.h里面的一致
     */
    public static class Speaker {
        public static final int FL = 0;
        public static final int FR = 1;
        public static final int RL = 2;
        public static final int RR = 3;
        public static final int SWL = 4;
        public static final int SWR = 5;
    }

    /**
     * 通道切换类的定义
     */
    public static class Channel {
        public static final int NONE                    = 0;
        public static final int PC                      = 1;    //系统
        public static final int PC_MUSIC                = 3;    //系统音乐
        public static final int PC_VIDEO                = 4;    //系统视频
        public static final int PC_VOICE                = 5;    //声控与TTS
        public static final int PC_SECONDARY            = 6;    //大巴乘客区（第二路PC通道）

        public static final int RADIO                   = 11;   //收音机
        public static final int RADIO_TA                = 12;   //收音机交通广播提醒

        public static final int TV                      = 21;   //TV
        public static final int TV2                     = 22;   //TV2
        public static final int AUX                     = 23;   //AUX
        public static final int AUX2                    = 24;   //AUX2
        public static final int AV                      = 25;   //AV
        public static final int AV2                     = 26;   //AV2
        public static final int AV3                     = 27;   //AV3
        public static final int AV4                     = 28;   //AV4
        public static final int ANNOUNCE_MIC            = 29;   //麦克风

        public static final int BLUETOOTH_TEL           = 31;   //蓝牙电话
        public static final int BLUETOOTH_RING          = 32;   //蓝牙来电铃声
        public static final int BLUETOOTH_A2DP          = 33;   //蓝牙音乐

        public static final int THIRDPARTY_CAR_TEL      = 41;   //安吉星等车载电话通道
        public static final int THIRDPARTY_DDH          = 42;   //嘀嘀虎

        public static final int APPLE_CARPLAY_MEDIA     = 51;   //CarPlay媒体
        public static final int APPLE_CARPLAY_ALARM     = 52;   //CarPlay闹钟
        public static final int APPLE_CARPLAY_SIRI      = 53;   //CarPlay语音
        public static final int APPLE_CARPLAY_TEL       = 64;   //CarPlay电话
        public static final int APPLE_USB_IPOD          = 59;   //CarPlay USB IPOD

        public static final int ANDROID_AUTO_MEDIA      = 61;   //AndroidAuto媒体
        public static final int ANDROID_AUTO_ALARM      = 62;   //AndroidAuto闹钟
        public static final int ANDROID_AUTO_VOICE      = 63;   //AndroidAuto语音
        public static final int ANDROID_AUTO_TEL        = 64;   //AndroidAuto电话

        public static final int MONO                    = 70;   //混音通道

        public int mId;
        public float mValue;
        public float mMin;
        public float mMax;
        public float mDefault;

        public static String getName(int channel) {
            return LogNameUtil.getName(channel, Channel.class, "Unknown audio channel: " + channel);
        }

        public static ArrayList<Integer> getChannels() {
            return LogNameUtil.getFields(Channel.class);
        }

        public static int getChannel(String name) {
            return LogNameUtil.getValue(Channel.class, name, NONE);
        }

        public Channel(int id, float min, float max, float defaultValue) {
            mId = id;
            mMin = min;
            mMax = max;
            mDefault = defaultValue;
            mValue = defaultValue;
        }

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

        public boolean isReadOnly() {
            return (mMax == mMin);
        }
    }

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

    // 音量条消息
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
