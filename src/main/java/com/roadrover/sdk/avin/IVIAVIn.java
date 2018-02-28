package com.roadrover.sdk.avin;

import com.roadrover.sdk.audio.IVIAudio;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.sdk.utils.LogNameUtil;

import java.util.ArrayList;

public class IVIAVIn {

    /**
     * AVIN ID，VideoChannel共用这个ID定义
     */
    public static class Id {

        public static final int NONE = 0;

        /**
         * AV, 其值 {@value}
         */
        public static final int AV = 1;

        /**
         * AV2, 其值 {@value}
         */
        public static final int AV2 = 2;

        /**
         * TV, 其值 {@value}
         */
        public static final int TV = 11;

        /**
         * TV2, 其值 {@value}
         */
        public static final int TV2 = 12;

        /**
         * AUX, 其值 {@value}
         */
        public static final int AUX = 21;

        /**
         * 导游麦克风, 其值 {@value}
         */
        public static final int ANNOUNCE_MIC = 22;

        /**
         * 前视（车头）, 其值 {@value}
         */
        public static final int FRONT = 31;

        /**
         * 后视, 其值 {@value}
         */
        public static final int REAR = 32;

        /**
         * 左视, 其值 {@value}
         */
        public static final int LEFT = 33;

        /**
         * 右视, 其值 {@value}
         */
        public static final int RIGHT = 34;

        /**
         * 机器面板上的聊天摄像头, 其值 {@value}
         */
        public static final int WEB = 35;

        /**
         * 行车记录仪, 其值 {@value}
         */
        public static final int DRIVIND_RECORD = 36;

        /**
         * 大巴行李箱处摄像头, 其值 {@value}
         */
        public static final int BUS_LUGGAGE = 40;

        /**
         * 大巴内部摄像头, 其值 {@value}
         */
        public static final int BUS_INNER = 41;

        /**
         * 大巴中门摄像头, 其值 {@value}
         */
        public static final int BUS_MID_DOOR = 42;

        /**
         * 公交车调度模块出来的视频, 其值 {@value}
         */
        public static final int BUS_DISPATCH = 43;

        /**
         * 有的Radio声音是走AVIN通道, 其值 {@value}
         */
        public static final int RADIO = 44;

        /**
         * 获取ID对应的名字
         * @param id 传入的ID，见{@link IVIAVIn.Id}
         * @return
         */
        public static String getName(int id) {
            return LogNameUtil.getName(id, Id.class, "Unknown AV IN id: " + id);
        }

        /**
         * 获取所有AVIN
         * @return
         */
        public static ArrayList<Integer> getIds() {
            return LogNameUtil.getFields(Id.class);
        }

        /**
         * 获取指定AVIN名字的ID
         * @param name AVIN名字
         * @return
         */
        public static int getId(String name) {
            return LogNameUtil.getValue(Id.class, name, NONE);
        }

        /**
         * 获取AVIN对应的媒体类型
         * @param id 传入的ID，见{@link IVIAVIn.Id}
         * @return
         */
        public static int getMediaType(int id) {
            switch (id) {
                case AUX:   return IVIMedia.Type.AUX;
                case AV:    return IVIMedia.Type.AV;
                case AV2:   return IVIMedia.Type.AV2;
                case TV:    return IVIMedia.Type.TV;
                case TV2:   return IVIMedia.Type.TV2;

                default:
                    return IVIMedia.Type.NONE;
            }
        }

        /**
         * 判断指定的AVIN ID是否为媒体
         * @param id 传入的ID，见{@link IVIAVIn.Id}
         * @return
         */
        public static boolean isMedia(int id) {
            return getMediaType(id) !=  IVIMedia.Type.NONE;
        }

        /**
         * 根据AVIN ID获取音频通道
         * @param id 传入的ID，见{@link IVIAVIn.Id}
         * @return
         */
        public static int getAudioChannel(int id) {
            switch (id) {
                case AV:    return IVIAudio.Channel.AV;
                case TV:    return IVIAudio.Channel.TV;
                case AV2:   return IVIAudio.Channel.AV2;
                case TV2:   return IVIAudio.Channel.TV2;
                case AUX:   return IVIAudio.Channel.AUX;
                case ANNOUNCE_MIC:  return IVIAudio.Channel.ANNOUNCE_MIC;
                default:    return IVIAudio.Channel.NONE;
            }
        }
    }

    /**
     * AVIN 信号
     */
    public static class Signal {
        /**
         * 检测中的信号不稳定, 其值 {@value}
         */
        public static final int UNSTABLE_SIGNAL = -1;
        /**
         * 无信号, 其值 {@value}
         */
        public static final int NO_SIGNAL = 0;
        /**
         * 有信号, 其值 {@value}
         */
        public static final int HAS_SIGNAL = 1;

        /**
         * 获取信号值对应的名字
         * @param signal 传入的信号值，见{@link IVIAVIn.Signal}
         * @return
         */
        public static String getName(int signal) {
            switch (signal) {
                case UNSTABLE_SIGNAL: return "Unstable signal";
                case NO_SIGNAL: return "No signal";
                case HAS_SIGNAL: return "Has signal";
                default:
                    return "Unknown signal: " + signal;
            }
        }

        /**
         * 判断传入的信号值是否代表有信号
         * @param signal 传入的信号值，见{@link IVIAVIn.Signal}
         * @return
         */
        public static boolean hasSignal(int signal) {
            return (HAS_SIGNAL == signal);
        }

        /**
         * 判断传入的信号值是否稳定
         * @param signal 传入的信号值，见{@link IVIAVIn.Signal}
         * @return
         */
        public static boolean isSignalStable(int signal) {
            return (UNSTABLE_SIGNAL != signal);
        }
    }

    /**
     * AVIN监听
     */
    public interface AVInListener {
        /**
         * 视频信号发生变化
         * @param avId AVIN ID，见{@link IVIAVIn.Id}
         * @param signal AVIN信号值，见{@link IVIAVIn.Signal}
         */
        void onVideoSignalChanged(int avId, int signal);

        /**
         * CVBS类型发生变化
         * @param avId AVIN ID，见{@link IVIAVIn.Id}
         * @param cvbsType CVBS类型，见{@link com.roadrover.sdk.avin.VideoParam.CvbsType}
         */
        void onCvbsTypeChanged(int avId, int cvbsType);

        /**
         * 视频是否允许观看
         * @param show true允许，false禁止
         */
        void onVideoPermitChanged(boolean show);

        /**
         * AVIN媒体停止
         */
        void stop();

        /**
         * AVIN媒体恢复播放
         */
        void resume();

        /**
         * AVIN媒体下一曲
         */
        void next();

        /**
         * AVIN媒体上一曲
         */
        void prev();

        /**
         * AVIN媒体退出
         */
        void quitApp();

        /**
         * 选择AVIN媒体指定下标播放
         * @param index 下标
         */
        void select(int index);
    }

    /**视频信号变化EventBus事件类*/
    public static class EventVideoSignalChanged {
        /**{@link IVIAVIn.Id}*/
        public int mAvId;
        /**{@link IVIAVIn.Signal}*/
        public int mSignal;
        public EventVideoSignalChanged(int avId, int signal) {
            mAvId = avId;
            mSignal = signal;
        }

        /**
         * 是否有信号
         * @return true有信号，false无信号
         */
        public boolean hasSignal() {
            return Signal.hasSignal(mSignal);
        }
    }

    /**CVBS视频类型变化EventBus事件类*/
    public static class EventCvbsTypeChanged {
        /**{@link IVIAVIn.Id}*/
        public int mAvId;
        /**{@link com.roadrover.sdk.avin.VideoParam.CvbsType}*/
        public int mCvbsType;
        public EventCvbsTypeChanged(int avId, int cvbsType) {
            mAvId = avId;
            mCvbsType = cvbsType;
        }
    }

    /**源插入状态变化EventBus事件类*/
    public static class EventSourcePluginChanged {
        /**{@link IVIAVIn.Id*/
        public int mAvId;
        /**true插入，false未插入*/
        public boolean mPlugin;
        public EventSourcePluginChanged(int avId, boolean plugin) {
            mAvId = avId;
            mPlugin = plugin;
        }
    }

    /**EventBus事件控制类*/
    public static class EventControl {
        public static class Action {
            public static final int PREV = 1;
            public static final int NEXT = 2;
            public static final int SELECT = 3;
            public static final int QUIT_APP = 4;
            public static final int STOP = 5;
            public static final int VIDEO_PERMIT = 6;
            public static final int RESUME = 7;
        }

        /**{@link IVIAVIn.EventControl.Action}*/
        public int mAction;
        /**参数值*/
        public int mValue;

        public EventControl(int action) {
            mAction = action;
        }

        public EventControl(int action, int value) {
            mAction = action;
            mValue = value;
        }
    }
}
