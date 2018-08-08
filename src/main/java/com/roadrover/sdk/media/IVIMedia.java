package com.roadrover.sdk.media;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.roadrover.sdk.audio.IVIAudio;
import com.roadrover.sdk.utils.ByteUtil;
import com.roadrover.sdk.utils.LogNameUtil;

import java.nio.ByteBuffer;

/**
 * 媒体数据定义类
 */
public class IVIMedia {

    /**
     * 媒体类型，主要是通知其他应用，当前是音乐，还是收音机等
     */
    public static class Type {

        /**
         * 其他类型，其值为 {@value}
         */
        public static final int NONE        = -1;

        /**
         * 音乐类型，其值为 {@value}
         */
        public static final int MUSIC       = 0;

        /**
         * 收音机类型，其值为 {@value}
         */
        public static final int RADIO       = 1;

        /**
         * 视频类型，其值为 {@value}
         */
        public static final int VIDEO       = 2;

        /**
         * AV类型，其值为 {@value}
         */
        public static final int AV          = 3;

        /**
         * DVD类型，其值为 {@value}
         */
        public static final int DVD         = 4;

        /**
         * AUX类型，其值为 {@value}
         */
        public static final int AUX         = 5;

        /**
         * TV类型，其值为 {@value}
         */
        public static final int TV          = 6;

        /**
         * 蓝牙音乐类型，其值为 {@value}
         */
        public static final int A2DP        = 7;

        /**
         * AV2类型，其值为 {@value}
         */
        public static final int AV2         = 8;

        /**
         * TV2类型，其值为 {@value}
         */
        public static final int TV2         = 9;

        /**
         * 图库类型，其值为 {@value}
         */
        public static final int GALLERY     = 10;

        /**
         * 数字音频广播类型，其值为 {@value}
         */
        public static final int DAB         = 11;

        /**
         * 第三方媒体类型，其值为 {@value}
         */
        public static final int THIRD_PARTY = 20;

        /**
         * 音乐，声音从第二个DAC通道出来，其值为 {@value}
         */
        public static final int MUSIC_DAC2  = 21;

        /**
         * 视频，声音从第二个DAC通道出来，其值为 {@value}
         */
        public static final int VIDEO_DAC2  = 22;

        /**
         * PHONE ，其值为 {@value}
         */
        public static final int PHONE       = 23;

        /**
         * 通过类型获取当前的通道
         * @param type {@link Type}
         * @return 通道 {@link com.roadrover.sdk.audio.IVIAudio.Channel}
         */
        public static int getAudioChannel(int type) {
            switch (type) {
                case NONE:              return IVIAudio.Channel.PC;
                case MUSIC:             return IVIAudio.Channel.PC_MUSIC;
                case RADIO:             return IVIAudio.Channel.RADIO;
                case VIDEO:             return IVIAudio.Channel.PC_VIDEO;
                case A2DP:              return IVIAudio.Channel.BLUETOOTH_A2DP;
                case THIRD_PARTY:       return IVIAudio.Channel.PC_MUSIC;
                case AV:                return IVIAudio.Channel.AV;
                case TV:                return IVIAudio.Channel.TV;
                case AV2:               return IVIAudio.Channel.AV2;
                case TV2:               return IVIAudio.Channel.TV2;
                case AUX:               return IVIAudio.Channel.AUX;
                case GALLERY:           return IVIAudio.Channel.PC;
                case MUSIC_DAC2:        return IVIAudio.Channel.PC_SECONDARY;
                case VIDEO_DAC2:        return IVIAudio.Channel.PC_SECONDARY;
                case PHONE:             return IVIAudio.Channel.PHONE;
                default:                return IVIAudio.Channel.PC;
            }
        }

        /**
         * 通过类型打印名字，一般用于打印log
         * @param type {@link Type}
         * @return 例： 0 返回 "MUSIC"
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, Type.class, "Media " + type);
        }
    }

    /**
     * 媒体多分区定义
     */
    public static class Zone {

        public static final int UNKNOWN = 0;

        /**
         * 主分区：小车前排、大巴司机区，其值为 {@value}
         */
        public static final int MASTER = (1 << 0);

        /**
         * 从分区：小车后排、大巴乘客区，其值为 {@value}
         */
        public static final int SECONDARY = (1 << 1);

        /**
         * 主分区 + 从分区，其值为 {@value}
         */
        public static final int ALL = (MASTER | SECONDARY);

        /**
         * 通过类型打印名字，一般用于打印log
         * @param zone {@link Zone}
         * @return 例： 1 返回 "MASTER"
         */
        public static String getName(int zone) {
            return LogNameUtil.getName(zone, Zone.class, "Unknown zone :" + zone);
        }

        /**
         * 判断主分区是否是可用状态
         * @param zone {@link Zone}
         * @return 可用返回true，否则返回false
         */
        public static boolean isMasterEnable(int zone) {
            return (zone & MASTER) != 0;
        }

        /**
         * 判断乘客区是否是可用状态
         * @param zone {@link Zone}
         * @return 可用返回true，否则返回false
         */
        public static boolean isSecondaryEnable(int zone) {
            return (zone & SECONDARY) != 0;
        }

        /**
         * 判断两个区域是否存在覆盖
         * @param zone1 {@link Zone}
         * @param zone2 {@link Zone}
         * @return 如果有相交合的部分，则返回true，否则返回false
         */
        public static boolean isZoneInZone(int zone1, int zone2) {
            return (zone1 & zone2) != 0;
        }
    }

    /**
     * 媒体数据类型，用户通过contentProvider查找媒体数据，通过该类型查找，该定义与 scaninterface.cpp 里面定义一致
     */
    public static class MediaSqlDataType {

        /**
         * 在回调通知应用的时候，如果类型时 扫描全部，挂载，移除 盘符等，sql类型传该参数，其值为 {@value}
         */
        public static final int UNKNOWN_TYPE = 100;

        /**
         * 音乐类型，其值为 {@value}
         */
        public static final int AUDIO_TYPE = 0;

        /**
         * 视频类型，其值为 {@value}
         */
        public static final int VIDEO_TYPE = 1;

        /**
         * 电子书类型，其值为 {@value}
         */
        public static final int BOOK_TYPE  = 2;

        /**
         * 图片类型，其值为 {@value}
         */
        public static final int IMAGE_TYPE = 3;

        /**
         * 日志类型，其值为 {@value}
         */
        public static final int LOG_TYPE   = 4;

        /**
         * apk类型，其值为 {@value}
         */
        public static final int APK_TYPE   = 5;

        /**
         * 文件夹类型，其值为 {@value}
         */
        public static final int DIR_TYPE   = 10;

        /**
         * 通过类型打印名字，一般用于Log打印
         * @param type {@link MediaSqlDataType}
         * @return 例： 5 返回 "APK_TYPE"
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, MediaSqlDataType.class, "Unknown type:" + type);
        }
    }

    /**
     * 媒体扫描类型定义类
     */
    public static class MediaScannerType {

        /**
         * 扫描全部盘符，其值为 {@value}
         */
        public static final int SCAN_ALL_TYPE = 0;

        /**
         * 挂载盘符，其值为 {@value}
         */
        public static final int MOUNT_TYPE    = 1;

        /**
         * 移除盘符，其值为 {@value}
         */
        public static final int EJECT_TYPE    = 2;

        /**
         * 文件创建，其值为 {@value}
         */
        public static final int FILE_CREATE_TYPE = 3;

        /**
         * 文件删除，其值为 {@value}
         */
        public static final int FILE_DELETE_TYPE = 4;

        /**
         * 文件重命名，其值为 {@value}
         */
        public static final int FILE_RENAME_TYPE = 5;

        /**
         * 通过类型打印名字，一般用于log打印
         * @param type {@link MediaScannerType}
         * @return 例： 5 返回 "FILE_RENAME_TYPE"
         */
        public static String getName(int type) {
            return LogNameUtil.getName(type, MediaScannerType.class);
        }
    }

    /**
     * 用于传递歌手图片，专辑图片的类
     */
    public static class ArtImage {

        /**
         * 图片的最大尺寸，单位像素
         */
        public static final int MAX_SIZE = 240;
        public int mWidth;
        public int mHeight;
        public byte[] mPixels;

        /**
         * 构造函数
         * @param width 图片宽度
         * @param height 高度
         * @param pixels 图片的像素内容
         */
        public ArtImage(int width, int height, byte[] pixels) {
            mWidth = width;
            mHeight = height;
            mPixels = pixels;
        }

        /**
         * 构造函数
         * @param image 图片的 Bitmap
         */
        public ArtImage(Bitmap image) {
            mWidth = 0;
            mHeight = 0;
            mPixels = new byte[0];
            if (image == null) {
                return;
            }

            Bitmap gotImage = image;
            int width = image.getWidth();
            int height = image.getHeight();
            if (width > MAX_SIZE || height > MAX_SIZE) {
                // 图片尺寸超过了最大尺寸
                Matrix m = new Matrix();
                float sw = (float)MAX_SIZE / (float)width;
                float sh = (float)MAX_SIZE / (float)height;
                float scale = sw > sh ? sh : sw;
                m.postScale(scale, scale);
                gotImage = Bitmap.createBitmap(image, 0, 0, width, height, m, true);
            }

            if (gotImage != null) {
                ByteBuffer buff = ByteBuffer.allocate(gotImage.getByteCount());
                gotImage.copyPixelsToBuffer(buff);
                mWidth = gotImage.getWidth();
                mHeight = gotImage.getHeight();
                mPixels = buff.array();
            }
        }

        /**
         * 获取当前的歌手图片
         * @return
         */
        public Bitmap getBitmap() {
            if (mWidth <= 0 || mHeight <= 0 || mPixels == null) {
                return null;
            }

            // 根据mPixels的长度来决定bitmap的格式
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            if (mPixels.length == mWidth * mHeight * 2) {
                config = Bitmap.Config.RGB_565;
            }

            Bitmap image = Bitmap.createBitmap(mWidth, mHeight, config);
            image.copyPixelsFromBuffer(ByteBuffer.wrap(mPixels));
            return image;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o != null && o instanceof ArtImage) {
                ArtImage other = (ArtImage) o;
                if (other.mHeight == mHeight &&
                        other.mWidth == mWidth &&
                        ByteUtil.equals(other.mPixels, mPixels)) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * 媒体信息定义类，包括名字，信息，index，总歌曲数等等
     */
    public static class MediaInfo {
        public int mMediaType;
        public String mName;
        public String mInfo;
        public ArtImage mArtImage;
        public int mIndex;
        public int mTotalCount;

        /**
         * 是否弹出媒体信息变化的对话框，在上下曲的时候为true，其他为false
         * <b>如果部分需求，在按方控上，下曲时候，需要弹出一个提示框，可以通过该参数进行判断</br>
         */
        public boolean mPopup;

        /**
         * 默认构造函数
         */
        public MediaInfo() {
            mMediaType = Type.NONE;
            mArtImage = new ArtImage(0, 0, null);
            mPopup = false;
        }

        /**
         * 构造函数
         * @param mediaType 媒体类型 {@link Type}
         * @param name 媒体名字，如果是收音机，则为 FM 97.1
         * @param info 媒体信息，音乐为歌手，收音机如果有RDS为电台名称，无RDS为空
         * @param artWidth 歌手图片宽度，音乐才有，其他媒体无
         * @param artHeight 歌手图片高度，音乐才有，其他媒体无
         * @param artBytes 歌手图片数据，音乐才有，其他媒体无
         * @param index 第几首媒体
         * @param totalCount 媒体总数
         * @param popup 是否弹出媒体信息变化的对话框，在上下曲的时候为true，其他为false
         */
        public MediaInfo(int mediaType, String name, String info, int artWidth, int artHeight, byte[] artBytes,
                         int index, int totalCount, boolean popup) {
            mMediaType = mediaType;
            mName = name;
            mInfo = info;
            mIndex = index;
            mTotalCount = totalCount;
            mArtImage = new ArtImage(artWidth, artHeight, artBytes);
            mPopup = popup;
        }

        /**
         * 当前媒体是否有效
         * @return 如果无效，则返回false
         */
        public boolean isValid() {
            return mMediaType != Type.NONE;
        }

        /**
         * 将类的数据转换成string打印出来
         * @return
         */
        public String toString() {
            return LogNameUtil.toString(this);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o != null && o instanceof MediaInfo) {
                MediaInfo other = (MediaInfo) o;
                if (other.mMediaType == mMediaType &&
                        TextUtils.equals(other.mName, mName) &&
                        TextUtils.equals(other.mInfo, mInfo) &&
                        other.mIndex == mIndex &&
                        other.mPopup == mPopup ) {
                    if (other.mArtImage == mArtImage ||
                            (other.mArtImage != null && other.mArtImage.equals(mArtImage))) { // 判断两个对象数据完全一样
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 媒体的状态类
     */
    public static class MediaState {

        /**
         * 停止状态，一般是ACC，或者从一个媒体到另外一个媒体会调用该状态
         */
        public static final int STOPPED = 1;

        /**
         * 暂停状态，一般是蓝牙通话，或者倒车等状态，会调用 PAUSED
         */
        public static final int PAUSED = 2;

        /**
         * 播放状态
         */
        public static final int PLAYING = 3;
        public int mMediaType;
        public int mState;
        public int mPosition;
        public int mDuration;

        /**
         * 默认构造函数
         */
        public MediaState() {
            mMediaType = Type.NONE;
        }

        /**
         * 构造函数
         * @param mediaType 媒体类型 {@link Type}
         * @param state 状态 {@link MediaState}
         * @param position 当前的进度
         * @param duration 总进度
         */
        public MediaState(int mediaType, int state, int position, int duration) {
            mMediaType = mediaType;
            mState = state;
            mPosition = position;
            mDuration = duration;
        }

        /**
         * 判断当前类型是否有效
         * @return
         */
        public boolean isValid() {
            return mMediaType != Type.NONE;
        }

        /**
         * 当前是否是播放状态
         * @return 播放状态返回true，其他返回false
         */
        public boolean isPlaying() {
            return mState == PLAYING;
        }

        /**
         * 通过状态获取名字，一般用作打印 log
         * @param state {@link MediaState}
         * @return 例：1 返回 "STOPPED"
         */
        public static String getName(int state) {
            return LogNameUtil.getName(state, MediaState.class, "Unknown media state: " + state);
        }

        /**
         * toString，一般用作打印，会将类型，状态，进度，总时间打印出来
         * @return
         */
        @Override
        public String toString() {
            return LogNameUtil.toString(this);
        }
    }

    /**
     * 媒体多分区
     */
    public static class MediaZone {

        /**
         * 媒体类型， {@link Type}
         */
        public int mMediaType;

        /**
         * 多分区定义 {@link Zone}
         */
        public int mZone;

        /**
         * 构造函数
         * @param mediaType 媒体类型 {@link Type}
         * @param zone {@link Zone}
         */
        public MediaZone(int mediaType, int zone) {
            mMediaType = mediaType;
            mZone = zone;
        }
    }

    /**
     * 媒体源媒体类型显示信息。
     * 用于显示非媒体源的媒体类型信息，如图库等。
     */
    public static class MediaTypeShownInfo {

        /**
         * 媒体类型， {@link Type}
         */
        public int mMediaType;

        public MediaTypeShownInfo() {
            mMediaType = -1;
        }

        /**
         * 构造函数
         * @param mediaType 媒体类型 {@link Type}
         */
        public MediaTypeShownInfo(int mediaType) {
            mMediaType = mediaType;
        }
    }

    /**
     * 媒体控制监听. </br>
     * 1. 蓝牙电话，倒车(倒车暂停媒体)，锁屏，声控流程：suspend() -> resume()</br>
     * 2. ACC流程，stop() -> resume()</br>
     * 3. 进入其他媒体应用，stop()</br>
     * 4. 方控暂停：pause()，方控播放：play()，上一曲：prev()，下一曲：next()</br>
     * 5. 导航声音，压低媒体</br>
     *     1) 当导航声音过来时，需要压低声音流程：setVolume(1.0) -> setVolume(0.9) ... -> setVolume(0.4)</br>
     *     2) 当导航声音结束后，需恢复声音流程：setVolume(0.4) -> setVolume(0.5) ... -> setVolume(1.0)</br>
     * 6. 从SystemUI划掉当前APP，退出应用，回调接口：quitApp()</br>
     * 7. onVideoPermitChanged接口只是有视频的APP用，主要是手刹控制，车辆行驶过程中，不能显示视频播放界面</br>
     */
    public interface MediaControlListener {
        /**
         * 挂起媒体 </br>
         * 在蓝牙电话开始、Power键锁屏、声控开启的时候调用， </br>
         * 媒体如果是在播放状态就暂停播放，如果是在暂停状态就不处理 </br>
         */
        void suspend();

        /**
         * 停止媒体 </br>
         * 在其他apk内的媒体（包括第三方媒体）打开的时候会被调用，断ACC超过一定的时间也会被调用 </br>
         */
        void stop();

        /**
         * 恢复媒体 </br>
         * 蓝牙电话结束、Power键结束锁屏，ACC恢复的时候调用， </br>
         * 媒体恢复到suspend或者stop调用前的状态 </br>
         */
        void resume();

        /**
         * 暂停播放 </br>
         * 效果和UI上的暂停按钮相同，一般用于方控 </br>
         */
        void pause();

        /**
         * 开始播放 </br>
         * 效果和UI上的播放按钮相同，一般用于方控 </br>
         */
        void play();

        /**
         * 播放暂停 </br>
         * 效果和UI上的播放暂停按钮相同，一般用于方控 </br>
         */
        void playPause();

        /**
         * 设置自有媒体音量 </br>
         * 一般在有导航提示音的时候调用 </br>
         * @param volume  0.最小值，1.0最大值
         */
        void setVolume(float volume);

        /**
         * 下一曲 </br>
         * 一般方控或者声控调用 </br>
         */
        void next();

        /**
         * 上一曲 </br>
         * 一般方控或者声控调用 </br>
         */
        void prev();

        /**
         * 选择播放列表里面的指定歌曲 </br>
         * 一般声控调用 </br>
         * @param index 第几首歌曲
         */
        void select(int index);

        /**
         * 收藏当前曲目 </br>
         * 一般声控调用 </br>
         * @param isFavour 是否收藏
         */
        void setFavour(boolean isFavour);

        /**
         * 选择什么歌曲，一般声控调用 </br>
         * 一般声控调用 </br>
         * @param title 歌曲名
         * @param singer 歌手名
         */
        void filter(String title, String singer);

        /**
         * 随机播放 </br>
         * 一般声控调用 </br>
         */
        void playRandom();

        /**
         * 选择播放模式
         * @param mode 播放模式
         */
        void setPlayMode(int mode);

        /**
         * 退出当前应用
         * @param quitSource {@link QuitMediaSource}
         */
        void quitApp(int quitSource);

        /**
         * 视频是否被允许显示，比如手刹控制
         */
        void onVideoPermitChanged(boolean show);

        /**
         * 拖动进度
         * @param msec 单位ms
         */
        void seekTo(int msec);
    }

    /**
     * 媒体扫描监听
     */
    public interface MediaScannerListener {

        /**
         * 扫描开始
         * @param type {@link MediaScannerType}
         */
        void onScanStart(int type);

        /**
         * 扫描结束
         * @param type  {@link MediaScannerType}
         * @param path 当前操作的目录，如果是扫描全部，为空
         */
        void onScanFinish(int type, String path);

        /**
         * 盘符卸载
         * @param path 卸载目录
         */
        void onEject(String path);

        /**
         * 盘符挂载
         * @param path 挂载目录
         */
        void onMount(String path);
    }

    /**
     * 退出媒体时的控制来源
     */
    public static class QuitMediaSource {
        /**
         * 未知来源
         */
        public static final int UNKNOWN = 0;

        /**
         * SystemUI滑掉
         */
        public static final int SYSTEM_UI = 1;

        /**
         * 声控控制退出
         */
        public static final int VOICE = 2;
    }
}
