package com.roadrover.sdk.radio;

import com.roadrover.sdk.utils.LogNameUtil;
import com.roadrover.sdk.utils.Logcat;

public class IVIRadio {
    public static final String FREQ = "freq";
    public static final String BAND = "band";
    public static final String LOCATION = "location";
    public static final String AF = "af";
    public static final String TA = "ta";
    public static final String PTY = "pty";
    public static final int FM_MIN_KHZ = 80000;

    /**
     * 该定义必须和services-jni里面的RadioDevice.h里面的一致
     */
    public static class Id {
        /** 自动检测收音机模块 */
        public static final int AUTO_DETECT   = 0;
        /** TEF6638模块 */
        public static final int TEF6638       = 1;
        /** 12MHz，支持RDS */
        public static final int TEF6686_12MHZ = 2;
        /** 4MHz，不支持RDS */
        public static final int TEF6686_4MHZ  = 3;
        /** 配合TEF7092,做双天线方案 */
        public static final int TEF6692       = 4;
        /** SAA7706 */
        public static final int SAA7706       = 10;
    }

    public static class ScanAction {
        public static final int NONE = 0;
        public static final int ALL = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;

        public static final boolean isAll(int action) {
            return (ALL == action);
        }

        public static final String getName(int action) {
            return LogNameUtil.getName(action, ScanAction.class, "Unknown action: " + action);
        }
    }

    public static class Band {
        public static final int NONE = -1;
        public static final int AM = 0;
        public static final int FM = 1;

        public static String getName(int band) {
            return LogNameUtil.getName(band, Band.class);
        }
    }

    public static class Location {
        public static final int ASIA = 0;
        public static final int EUROPE = 1;
        public static final int US = 2;
        public static final int LATIN = 3;
    }

    public static class Direction {
        public static final int DOWN = -1;
        public static final int UP = 1;
    }

    public static String getIdName(int id) {
        return LogNameUtil.getName(id, Id.class, "Unknown radio id: " + id);
    }

    public static String getFreqString(int freq, int band, int location) {
        String result = "";
        if (band == IVIRadio.Band.FM) {
            Float ret = freq / 1000.0f;
            result = String.valueOf(ret);
            int len = result.length() - result.lastIndexOf(".") - 1;
            if (location == Location.EUROPE && len < 2) {
                result = result + "0";
            }
        } else {
            result = String.valueOf(freq);
        }

        return result;
    }

    public static String getBandString(int band) {
        if (band == Band.AM) {
            return "AM";
        } else if (band == Band.FM) {
            return "FM";
        } else {
            Logcat.d("Unknown band: " + band);
            return "";
        }
    }

    public static String getLocationString(int location) {
        return LogNameUtil.getName(location, Location.class, "Unknown location " + location);
    }

    public static String getFreqUnitString(int band) {
        if (band == Band.AM) {
            return "KHz";
        } else if (band == Band.FM) {
            return "MHz";
        } else {
            Logcat.d("Unknown band: " + band);
            return "";
        }
    }

    public static int getFreqStart(int band, int location) {
        if (band == Band.FM) {
            switch (location) {
                case Location.ASIA:     return 87500;
                case Location.EUROPE:   return 87500;
                case Location.US:       return 87500;
                case Location.LATIN:    return 87500;
                default:                return 87500;
            }
        } else {
            switch (location) {
                case Location.ASIA:     return 531;
                case Location.EUROPE:   return 522;
                case Location.US:       return 520;
                case Location.LATIN:    return 520;
                default:                return 520;
            }
        }
    }

    public static int getFreqEnd(int band, int location) {
        if (band == Band.FM) {
            switch (location) {
                case Location.ASIA:     return 108000;
                case Location.EUROPE:   return 108000;
                case Location.US:       return 107900;
                case Location.LATIN:    return 107900;
                default:                return 108000;
            }
        } else {
            switch (location) {
                case Location.ASIA:     return 1602;
                case Location.EUROPE:   return 1620;
                case Location.US:       return 1710;
                case Location.LATIN:    return 1710;
                default:                return 1710;
            }
        }
    }

    public static int getFreqStep(int band, int location) {
        if (band == Band.FM) {
            switch (location) {
                case Location.ASIA:     return 100;
                case Location.EUROPE:   return 50;
                case Location.US:       return 200;
                case Location.LATIN:    return 100;
                default:                return 50;
            }
        } else {
            switch (location) {
                case Location.ASIA:     return 9;
                case Location.EUROPE:   return 9;
                case Location.US:       return 10;
                case Location.LATIN:    return 10;
                default:                return 9;
            }
        }
    }

    /**
     * 判断收音频率是否有效
     * @param freq
     * @param band
     * @param location
     * @return 收音机频率有效返回true
     */
    public static boolean isFreqValid(int freq, int band, int location) {
        boolean ret = false;
        final int start = getFreqStart(band, location);
        final int end = getFreqEnd(band, location);
        if (freq >= start && freq <= end) {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断收音频率是否有效
     * @param freq
     * @param location
     * @return 有效返回true
     */
    public static boolean isFreqValid(int freq, int location) {
        return (isFreqValid(freq, Band.FM, location) || isFreqValid(freq, Band.AM, location));
    }

    /**
     * 获取收音频率对应的波段
     * @param freq 收音机频率
     * @param location 地区
     * @return {@link Band}
     */
    public static int getFreqBand(int freq, int location) {
        int ret = Band.NONE;
        if (isFreqValid(freq, Band.FM, location)) {
            ret = Band.FM;
        } else if (isFreqValid(freq, Band.AM, location)) {
            ret = Band.AM;
        }
        return ret;
    }

    public static class EventFreqChanged {
        public int mFreq;
        EventFreqChanged(int freq) {
            mFreq = freq;
        }
    }

    public static class EventScanResult {
        public int mFreq;
        public int mSignalStrength;
        EventScanResult(int freq, int signalStrength) {
            mFreq = freq;
            mSignalStrength = signalStrength;
        }
    }

    public static class EventScanStart {
        public boolean mScanAll;
        EventScanStart(boolean isScanAll) {
            mScanAll = isScanAll;
        }
    }

    public static class EventScanEnd {
        public boolean mScanAll;
        EventScanEnd(boolean isScanAll) {
            mScanAll = isScanAll;
        }
    }

    public static class EventScanAbort {
        public boolean mScanAll;
        EventScanAbort(boolean isScanAll) {
            mScanAll = isScanAll;
        }
    }

    public static class EventSignalUpdate {
        public int mFreq;
        public int mSignalStrength;
        EventSignalUpdate(int freq, int signalStrength) {
            mFreq = freq;
            mSignalStrength = signalStrength;
        }
    }

    public static class EventControl {
        public static class Action {
            /** 上一个电台 */
            public static final int PREV        = 1;
            /** 下一个电台 */
            public static final int NEXT        = 2;
            /** 外部控制，第几个电台，例：声控 */
            public static final int SELECT      = 3;
            /** 外部控制，收藏电台，例：声控 */
            public static final int SET_FAVOUR  = 4;
            /** 外部控制，退出应用，例：声控 */
            public static final int QUIT_APP    = 5;
            /** 外部控制，停止播放，例：声控 */
            public static final int STOP        = 6;
            /** TUNE旋钮旋转消息，mValue = 1 顺时针 */
            public static final int TUNE_ROTATE = 7;
            /** 暂停 */
            public static final int SUSPEND     = 8;
            /** 恢复播放 */
            public static final int RESUME      = 9;
            /** 暂停播放 */
            public static final int PAUSE       = 10;
            /** 播放 */
            public static final int PLAY        = 11;
            /** 播放&暂停 */
            public static final int PLAY_PAUSE  = 12;
            /** 上搜索 */
            public static final int SCAN_UP     = 13;
            /** 下搜索 */
            public static final int SCAN_DOWN   = 14;
            /** 搜索全部 */
            public static final int SCAN_ALL    = 15;
        }
        public int mAction;
        public int mValue;
        public EventControl(int action) {
            mAction = action;
        }

        public EventControl(int action, int value) {
            mAction = action;
            mValue = value;
        }

        public static String getName(int action) {
            return LogNameUtil.getName(action, Action.class);
        }
    }

    public static class EventRdsPs {
        public int mPI;         // 电台唯一编码
        public int mFreq;       // 电台频率
        public String mText;    // 电台名称
        public EventRdsPs(int pi, int freq, String ps) {
            mPI = pi;
            mFreq = freq;
            mText = ps;
        }
    }

    public static class EventRdsRt {
        public int mPI;         // 电台唯一编码
        public int mFreq;       // 电台频率
        public String mText;    // 电台文本信息
        public EventRdsRt(int pi, int freq, String rt) {
            mPI = pi;
            mFreq = freq;
            mText = rt;
        }
    }

    public static class EventRdsMask {
        public int mPI;         // 电台唯一编码
        public int mFreq;       // 电台频率
        public int mPTY;        // 节目类型
        public int mTP;         // 当前节目是否可以播放交通广播（当前不一定播）
        public int mTA;         // 当前节目是否正在播放交通广播
        public EventRdsMask(int pi, int freq, int pty, int tp, int ta) {
            mPI = pi;
            mFreq = freq;
            mPTY = pty;
            mTP = tp;
            mTA = ta;
        }
    }

    public static class RDS {
        public static final int PI_UNKNOWN  = 0;
        /** pty 对话框 */
        public static final int PTY_ALARM   = 31;
        /** pty 未定义 */
        public static final int PTY_UNKNOWN = 0xFF;
        /** TA 关 */
        public static final int TA_OFF      = 0;
        /** TA 开 */
        public static final int TA_ON       = 1;
        /** TA 未定义 */
        public static final int TA_UNKNOWN  = 0xFF;
        /** TP 关 */
        public static final int TP_OFF      = 0;
        /** TP 开 */
        public static final int TP_ON       = 1;
        /** TP 未定义 */
        public static final int TP_UNKNOWN  = 0xFF;
    }
}
