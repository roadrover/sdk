package com.roadrover.sdk.car;

import com.roadrover.sdk.utils.Logcat;

/**
 * 协议外部功放音效类
 */

public class AmpAudio {
    public static final int INVALID = 0xff;
    public boolean mIsHasAmpAudio = false;
    public byte[] mData;
    public int mMaxVolume;
    public int mMaxBass;
    public int mMaxMiddle;
    public int mMaxTreble;
    public int mMaxBalance;
    public int mMaxFade;
    public int mDefaultPreVolume;

    public static class Id {
        /**音量最大值*/
        public static final int VOLUME = 0;
        /**低音最大值*/
        public static final int BASS = 1;
        /**中音最大值*/
        public static final int MIDDLE = 2;
        /**高音最大值*/
        public static final int TREBLE = 3;
        /**平衡音最大值*/
        public static final int BALANCE = 4;
        /**衰减音最大值*/
        public static final int FADE = 5;
    }

    public AmpAudio() {
        mMaxVolume = INVALID;
        mMaxBass = INVALID;
        mMaxMiddle = INVALID;
        mMaxTreble = INVALID;
        mMaxBalance = INVALID;
        mMaxFade = INVALID;
        mIsHasAmpAudio = false;
    }

    public AmpAudio(byte[] buff) {
        mData = buff;
        parseAudioMaxData(buff);
    }

    /**
     * 设置数据值
     * @param buff
     */
    public void setAmpMaxData(byte[] buff) {
        mData = buff;
        parseAudioMaxData(buff);
    }

    /**
     * 解析串口数据
     * @param buff
     */
    private void parseAudioMaxData(byte[] buff) {
        if (buff != null && buff.length > 8) {
            mMaxVolume = buff[0] & 0xff;
            mMaxBass = buff[1] & 0xff;
            mMaxMiddle = buff[2] & 0xff;
            mMaxTreble = buff[3] & 0xff;
            mMaxBalance = buff[4] & 0xff;
            mMaxFade = buff[5] & 0xff;
            mDefaultPreVolume = buff[7] & 0xff;
            mIsHasAmpAudio = true;
        } else {
            Logcat.w("Invalid buff value !");
            mMaxVolume = INVALID;
            mMaxBass = INVALID;
            mMaxMiddle = INVALID;
            mMaxTreble = INVALID;
            mMaxBalance = INVALID;
            mMaxFade = INVALID;
            mDefaultPreVolume = INVALID;
            mIsHasAmpAudio = false;
        }
    }

    /**
     * 是否存在外部功放的音效调节功能
     * @return
     */
    public boolean isAmpAudioExist() {
        return mIsHasAmpAudio;
    }

    /**
     * 获取默认的前置音量
     * @return
     */
    public int getDefaultPreVolume() {
        return mDefaultPreVolume;
    }

    /**
     * 获取外部功放各个项可调节的最大值
     * @param id
     * @return
     */
    public int getMaxAmpValue(int id) {
        switch (id) {
            case Id.VOLUME:
                return mMaxVolume;
            case Id.BASS:
                return mMaxBass;
            case Id.MIDDLE:
                return mMaxMiddle;
            case Id.TREBLE:
                return mMaxTreble;
            case Id.BALANCE:
                return mMaxBalance;
            case Id.FADE:
                return mMaxFade;
            default:
                break;
        }
        return INVALID;
    }

    /**
     * 获取外部功放各个项可调节的最小值
     * @param id
     * @return
     */
    public int getMinAmpValue(int id) {
        return 0;
    }

    /**
     * 获取外部功放各个项可调节的默认值
     * @param id
     * @return
     */
    public int getDefaultAmpValue(int id) {
        final int max = getMaxAmpValue(id);
        if (max == INVALID) {
            return 0;
        } else {
            return (max >> 1);
        }
    }
}
