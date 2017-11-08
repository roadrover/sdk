package com.roadrover.sdk.voice;

import android.os.Message;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * 语音类型定义
 */

public class IVIVoice {

    /**
     * 播放模式的定义，用作语音控制当前媒体的播放模式
     */
    public static class MediaPlayMode {
        /** 顺序循环，值为{@value} */
        public static final int LIST_MODE   = 0;
        /** 单曲模式，值为{@value} */
        public static final int SINGLE_MODE = 1;
        /** 随机循环模式，值为{@value} */
        public static final int RAND_MODE   = 2;

        /** 最小值，用于做判断 */
        public static final int MIN = LIST_MODE;
        /** 最大值，用于做判断 */
        public static final int MAX = RAND_MODE;

        /**
         * 通过mode id获取定义的名字
         * @param mode {@link MediaPlayMode}
         * @return 例：1 返回 "SINGLE_MODE"
         */
        public static String getName(int mode) {
            return LogNameUtil.getName(mode, MediaPlayMode.class);
        }
    }

    /**
     * 语音控制媒体的 event 类
     */
    public static class EventVoiceMedia {

        /** 选择媒体，值为{@value} */
        public static final int SWITCH_MEDIA  = 0;
        /** 设置播放模式，值为{@value} */
        public static final int SET_PLAY_MODE = 1;
        /** 随机播放，值为{@value} */
        public static final int PLAY_RANDOM   = 2;
        /** 播放指定媒体，值为{@value} */
        public static final int PLAY_MEDIA    = 3;
        /** 收藏媒体，值为{@value} */
        public static final int FAVOUR_MEDIA  = 4;

        /** 当前id */
        public int mId = SWITCH_MEDIA;
        /** 媒体控制带的参数，例：第几首歌 */
        public int mArg1 = 0;
        /** 是否收藏带的参数 */
        public boolean mIsFavour = false;
        /** 播放指定媒体带的参数 */
        public String mMediaName, mSingerName;

        /**
         * 构造函数，使用该构造函数，只做控制，例：PLAY_RANDOM
         * @param id {@link EventVoiceMedia}
         */
        public EventVoiceMedia(int id) {
            mId = id;
        }

        /**
         * 带参数的构造函数，例：SWITCH_MEDIA 选择媒体
         * @param id {@link EventVoiceMedia}
         * @param arg1
         */
        public EventVoiceMedia(int id, int arg1) {
            mId = id;
            mArg1 = arg1;
        }

        /**
         * 收藏媒体的构造函数
         * @param id {@link EventVoiceMedia}
         * @param isFavour 是否收藏
         */
        public EventVoiceMedia(int id, boolean isFavour) {
            mId = id;
            mIsFavour = isFavour;
        }

        /**
         * 指定媒体播放的构造函数
         * @param id {@link EventVoiceMedia}
         * @param mediaName 媒体名字
         * @param singerName 歌手名字
         */
        public EventVoiceMedia(int id, String mediaName, String singerName) {
            mId = id;
            mMediaName = mediaName;
            mSingerName = singerName;
        }
    }

    /**
     * 语音控制收音机的Event定义类
     */
    public static class EventVoiceRadio {
        /** 设置频率，值为{@value} */
        public static final int SET_FREQ   = 0;
        /** 开始扫描，值为{@value} */
        public static final int START_SCAN = 1;
        /** 停止扫描，值为{@value} */
        public static final int STOP_SCAN  = 2;
        /** 设置am和fm，值为{@value} */
        public static final int SET_BAND   = 3;

        /** id */
        public int mId = SET_FREQ;
        /** 传递过程中带的参数，例：频率点 */
        public int mArg1 = 0;

        /**
         * 单纯控制命令的构造，例：STOP_SCAN
         * @param id {@link EventVoiceRadio}
         */
        public EventVoiceRadio(int id) {
            mId = id;
        }

        /**
         * 带参数的构造函数，例：SET_BAND
         * @param id {@link EventVoiceRadio}
         * @param arg1 参数，例：频率点
         */
        public EventVoiceRadio(int id, int arg1) {
            this(id);
            mArg1 = arg1;
        }
    }

    /**
     * 语音回调的event类
     */
    public static class EventVoiceCallback {
        /** 打开语音app，值为{@value} */
        public static final int OPEN_VOICE_APP = 0;

        /** 定义的类型 */
        public int mType;

        /**
         * 构造函数
         * @param type {@link EventVoiceCallback}
         */
        public EventVoiceCallback(int type) {
            mType = type;
        }
    }
}
