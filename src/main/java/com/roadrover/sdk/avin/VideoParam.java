package com.roadrover.sdk.avin;

import com.roadrover.sdk.Param;
import com.roadrover.sdk.system.IVIConfig;
import com.roadrover.sdk.utils.LogNameUtil;

import java.util.ArrayList;

/**
 * 视频参数，ID由两部分组成
 * Bit[16-8] IVIAVIn.Id 视频通道号
 * Bit[8-0] SubId 亮度、对比度
 */
public class VideoParam extends Param {
    /**
     * 亮度、对比度等视频相关参数
     */
    public static class SubId {
        /**
         * 无, 其值 {@value}
         */
        public static final int NONE = 0;
        /**
         * 亮度, 其值 {@value}
         */
        public static final int BRIGHTNESS = 1;
        /**
         * 对比度, 其值 {@value}
         */
        public static final int CONTRAST = 2;
        /**
         * 饱和度, 其值 {@value}
         */
        public static final int SATURATION = 3;
        /**
         * 色度, 其值 {@value}
         */
        public static final int HUE = 4;
        /**
         * CVBS 的制式, 其值 {@value}
         */
        public static final int CVBS_TYPE = 5;

        /**
         * 获取SubId的名字
         * @param id SubId，见{@link SubId}
         * @return
         */
        public static String getName(int id) {
            return LogNameUtil.getName(id, SubId.class, "Sub Id " + id);
        }

        /**
         * 获取SubId列表
         * @return
         */
        public static ArrayList<Integer> getSubIds() {
            return LogNameUtil.getFields(SubId.class);
        }
    }

    /**
     * CVBS类型
     */
    public static class CvbsType {
        /**
         * 自动, 其值 {@value}
         */
        public static final int AUTO = 0;
        /**
         * NTSC制式, 其值 {@value}
         */
        public static final int NTSC = 1;
        /**
         * PAL制式, 其值 {@value}
         */
        public static final int PAL = 2;
        /**
         * 最小值, 其值 {@value}
         */
        public static final int MIN = AUTO;
        /**
         * 最大值, 其值 {@value}
         */
        public static final int MAX = PAL;

        /**
         * 获取CVBS类型的名字
         * @param type CVBS类型，见{@link CvbsType}
         * @return
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, CvbsType.class, "Unknown CVBS type " + type);
        }

        /**
         * 获取视频高度
         * @param type CVBS类型，见{@link CvbsType}
         * @return
         */
        public static int getVideoHeight(int type) {
            if (AUTO == type) {
                type = IVIConfig.getAVInDefaultCVBSType();
            }
            return getCVBSVideoHeight(type);
        }

        /**
         * 获取视频宽度
         * @param type CVBS类型，见{@link CvbsType}
         * @return
         */
        public static int getVideoWidth(int type) {
            return getCVBSVideoWidth(type);
        }

        /**
         * 获取指定CVBS格式视频高度
         * @param type CVBS类型，见{@link CvbsType}
         * @return
         */
        private static int getCVBSVideoHeight(int type) {
            return (type == PAL) ? 576 : 480;
        }

        /**
         * 获取指定CVBS格式视频宽度
         * @param type CVBS类型，见{@link CvbsType}
         * @return
         */
        private static int getCVBSVideoWidth(int type) {
            return 720;
        }
    }

    /**
     * 构造函数
     * @param avId AVIN ID，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @param subId Sub ID，见{@link VideoParam.SubId}
     * @param min 最小值
     * @param max 最大值
     * @param defaultValue 默认值
     */
    public VideoParam(int avId, int subId, int min, int max, int defaultValue) {
        super(makeId(avId, subId), min, max, defaultValue);
    }

    /**
     * 构造函数
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @param min 最小值
     * @param max 最大值
     * @param defaultValue 默认值
     */
    public VideoParam(int id, int min, int max, int defaultValue) {
        super(id, min, max, defaultValue);
    }

    /**
     * 构造函数
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @param defaultValue 默认值
     */
    public VideoParam(int id, boolean defaultValue) {
        super(id, defaultValue);
    }

    /**
     * 构造函数
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @param value 当前值
     */
    public VideoParam(int id, int value) {
        super(id, value);
    }

    /**
     * 获取AVIN ID
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @return
     */
    public static int getAVId(int id) {
        return (id & 0xFF00) >> 8;
    }

    /**
     * 获取Sub ID
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @return
     */
    public static int getSubId(int id) {
        return (id & 0xFF);
    }

    /**
     * 视频参数，ID由两部分组成
     * @param avId Bit[16-8] 视频通道号，见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @param subId Bit[8-0] 亮度、对比度，见{@link VideoParam.SubId}
     * @return
     */
    public static int makeId(int avId, int subId) {
        return ((avId & 0xFF) << 8) | (subId & 0xFF);
    }

    /**
     * 获取视频参数名字
     * @return
     */
    public String getName() {
        return getName(mId);
    }

    /**
     * 通过视频参数ID获取名字
     * @param id 视频ID，通过{@link #makeId(int, int)}获得
     * @return
     */
    public static String getName(int id) {
        return IVIAVIn.Id.getName(getAVId(id)) + ":" + SubId.getName(getSubId(id));
    }
}
