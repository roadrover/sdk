package com.roadrover.sdk.system;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;

import com.roadrover.sdk.audio.IVIAudio;
import com.roadrover.sdk.avin.IVIAVIn;
import com.roadrover.sdk.avin.IVITV;
import com.roadrover.sdk.avin.VideoParam;
import com.roadrover.sdk.bluetooth.IVIBluetooth;
import com.roadrover.sdk.car.IVICar;
import com.roadrover.sdk.radio.IVIRadio;
import com.roadrover.sdk.utils.EnvironmentUtils;
import com.roadrover.sdk.utils.IniFileUtil;
import com.roadrover.sdk.utils.ListUtils;
import com.roadrover.sdk.utils.Logcat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局配置.
 */

public class IVIConfig {

    public static final String INI_FILE_PATH = "/etc/ivi-config.ini"; // 配置文件路径

    private static final String SECTION_ENVIRONMENT = "Environment"; // 存储设备section名称
    private static final String SECTION_CAMERA_INDEX = "CameraIndex";
    private static final String SECTION_AVINPUT = "AVInput";         // AV Input显示
    private static final String SECTION_AVIN_VIDEO_ADJUST = "AVInVideoAdjust";  // 是否支持AVIN视频参数调节功能
    private static final String SECTION_POWER = "Power";
    private static final String SECTION_MEDIA_DEMO = "MediaDemo";
    private static final String SECTION_FILE_MANAGER = "FileManager";
    private static final String SECTION_AVIN = "AVIn";

    private static final String SECTION_RADIO = "Radio";
    private static final String SECTION_REMOTE_CONTROL = "RemoteControl";
    private static final String SECTION_MODE_CONTROL = "ModeControl";
    private static final String SECTION_MEMORY_TO_HOME = "MemoryToHome";
    private static final String SECTION_MAP_DATA = "MapData";

    private static final String SECTION_HOME = "Home";
    private static final String SECTION_AUDIO = "Audio";
    private static final String SECTION_CAR_CCD = "CarCcd";
    private static final String SECTION_TV = "TV";

    private static final String SECTION_BRIGHTNESS = "Brightness";
    private static final String BRIGHTNESS_NEED_LINEAR_VARIATE = "LinearVariation";

    // Section AVIn
    private static final String AVIN_DEFAULT_CVBS_TYPE = "DefaultCVBSType";

    // Section Video Channel Config
    private static final String SECTION_VIDEO_CHANNEL_CONFIG = "VideoChannelConfig";

    // Section Audio Channel Config
    private static final String SECTION_AUDIO_CHANNEL_CONFIG = "AudioChannelConfig";
    private static final String SECTION_SECONDARY_AUDIO_CHANNEL_CONFIG = "SecondaryAudioChannelConfig";

    // Section Voice
    private static final String SECTION_VOICE = "Voice";
    private static final String VISIBLE = "Visible";

    // Section Power
    private static final String POWER_LONG_ACTION = "LONG_ACTION";
    private static final String POWER_SHORT_ACTION = "SHORT_ACTION";
    private static final String POWER_LONG_MAX_COUNT = "LONG_MAX_COUNT";
    private static final int POWER_LONG_MAX_COUNT_DEFAULT = 100;

    // 收音机
    private static final String RADIO_LOCATION = "Location";
    private static final String RADIO_NEED_RDS = "NeedRds";
    private static final String RADIO_RENAME_STATION = "RenameStation";
    private static final String RADIO_ID = "RadioId"; // 收音机id
    private static final String RADIO_PRESTORE_FM = "RadioPrestoreFm";//预存电台
    private static final String RADIO_NEED_TA = "NeedTA";
    private static final String RADIO_I2C_BUS_INDEX = "I2CBusIndex";
    private static final String RADIO_MIN_SIGNAL_LEVEL = "MinSignalLevel";
    private static final String RADIO_AF_START_LEVEL = "AFStartLevel";
    private static final String RADIO_AF_MIN_DELTA_LEVEL = "AFMinDeltaLevel";
    private static final String RADIO_HERO_FM_USN = "HeroFMUsn";
    private static final String RADIO_HERO_FM_WAM = "HeroFMWam";
    private static final String RADIO_HERO_FM_OFFSET = "HeroFMOffset";
    private static final String RADIO_HERO_FM_BW = "HeroFMBw";
    private static final String RADIO_HERO_FM_AUTO_LEVEL = "HeroFMAutoLevel";
    private static final String RADIO_HERO_FM_MANUAL_LEVEL = "HeroFMManualLevel";
    private static final String RADIO_HERO_AM_OFFSET = "HeroAMOffset";
    private static final String RADIO_HERO_AM_BW = "HeroAMBw";
    private static final String RADIO_HERO_AM_AUTO_LEVEL = "HeroAMAutoLevel";
    private static final String RADIO_HERO_AM_MANUAL_LEVEL = "HeroAMManualLevel";
    private static final String RADIO_ST_SIGNAL_STRENGTH = "STSignalStrength";

    // mode键
    private static final String MODE_COUNT = "COUNT";
    private static final String MODE_APP = "APP";

    // Home 按键
    private static final String HOME_LONG_MAX_COUNT = "LONG_MAX_COUNT"; // 长按次数
    private static final String HOME_LONG_ACTION = "LONG_ACTION"; // 长按动作

    // 休眠唤醒后记忆到主页的应用列表
    private static final String MEMORY_COUNT = "COUNT";
    private static final String MEMORY_APP = "APP";

    // 蓝牙
    private static final String SECTION_BLUETOOTH = "Bluetooth";
    private static final String BLUETOOTH_NAME = "BT_NAME"; // 蓝牙默认名字
    private static final String BLUETOOTH_PIN = "BT_PIN"; // 蓝牙默认pin码
    private static final String BLUETOOTH_DEVICE_ID = "BT_DEVICE"; // 蓝牙设备
    private static final String BLUETOOTH_SERIAL_FILE = "BT_SERIAL"; // 蓝牙设备串口文件名
    private static final String BLUETOOTH_SERIAL_BAUD_RATE = "BT_BAUD_RATE"; // 蓝牙设备串口波特率
    private static final String AUTO_LISTEN = "AUTO_LISTEN"; // 自动接听
    private static final String AUTO_LINK = "AUTO_LINK"; // 自动连接
    private static final String FORCE_LINK = "FORCE_LINK"; // 是否断开之后强制连接
    private static final String CONNECTED_GOTO_PAGE = "CONNECTED_GOTO_PAGE"; // 连接之后，强制调转哪个页面
    private static final String BLUETOOTH_CONTACT = "BT_CONTACT"; // 蓝牙联系人名字的格式
    private static final String LETTER_SLIDE_BAR = "LETTER_SLIDE_BAR"; // 蓝牙通讯录界面是否需要显示滑动条
    private static final String T3_BT_SERIAL_FILE = "/dev/ttyS3";
    private static final String BLUETOOTH_CONTACT_SORT = "BT_CONTACT_SPECIAL_FIRST"; // 蓝牙电话本排序方式，特殊字符排前还是排后

    public static class BluetoothPage {
        public static final int UNKNOWN  = -1; // 未定义
        public static final int KEY_PAD  = 0;
        public static final int CONTACTS = 1;
        public static final int HISTORY  = 2;
        public static final int DEVICES  = 3;
        public static final int SETTINGS = 4;
    }

    // 图库
    private static final String SECTION_GALLERY = "Gallery";
    private static final String GALLERY_SOURCE = "Source"; // 图库是否作为模式

    // 悬浮窗
    private static final String SECTION_FLOAT_MENU = "FloatMenu";
    private static final String NESTLE_SIDE = "NestleSide"; // 是否贴边，默认true

    // 关屏
    private static final String SECTION_SCREEN_OFF = "ScreenOff";
    private static final String KEY_ACTION = "KeyAction"; // 在关屏状态下，有作用，并且保持关屏状态
    private static final String KEY_NO_ACTION = "KeyNoAction"; // 在关屏状态下，按键不执行动作

    public static class PowerKeyAction {
        public static final int NONE = 0; // 不动作
        public static final int BKL_OFF = 1; // 关背光
        public static final int BKL_OFF_AND_PAUSE_MEDIA = 2; // 关背光、暂停媒体
        public static final int BKL_OFF_AND_QUIT_APP = 3; // 关背光、结束当前媒体应用,
        public static final int MUTE_AND_QUIT_APP_AND_CLOCK = 4; // 静音、结束应用、进入时钟界面
        public static final int MUTE = 5; // 静音
        public static final int REBOOT = 9; // 复位

        public static String getDesc(int action) {
            switch (action) {
                case NONE:                              return "NONE";
                case BKL_OFF:                           return "BackLight OFF";
                case BKL_OFF_AND_PAUSE_MEDIA:           return "BackLight OFF, Mute, Pause Media";
                case BKL_OFF_AND_QUIT_APP:              return "BackLight OFF, Mute, Quit App";
                case MUTE_AND_QUIT_APP_AND_CLOCK:       return "Mute, Quit App, Enter Clock UI";
                case MUTE:                              return "Mute";
                case REBOOT:                            return "Reboot";
                default:
                    return "Unknown action " + action;
            }
        }
    }

    // Media Demo
    public static final String AUDIO_DIR = "AudioDir";// AudioDir
    public static final String PICTURE_DIR = "PictureDir";// PictureDir
    public static final String VIDEO_DIR = "VideoDir";// VideoDir

    // Media format
    private static final String SECTION_MEDIA = "Media";
    private static final String VIDEO_FORMAT = "Video";
    private static final String MUSIC_FORMAT = "Music";
    private static final String IMAGE_FORMAT = "Image";
    private static final String MEDIA_NEED_PLAYTIME = "needPlayTime";

    // 音频
    // 内置前置音量不可以被应用更改，由硬件工程师根据不同的平台预先调好
    // 音频设置中另外提供个性化前置音量调节，可以根据不同的项目隐藏
    private static final String PC_PRE_VOLUME = "PC_PreVolume";         //PC、MP3、Video内置前置音量
    private static final String MONO_PRE_VOLUME = "Mono_PreVolume";     //混音通道前置音量
    private static final String RADIO_PRE_VOLUME = "Radio_PreVolume";   //收音机内置前置音量
    private static final String AV_PRE_VOLUME = "AV_PreVolume";         //AV内置前置音量
    private static final String AV2_PRE_VOLUME = "AV2_PreVolume";       //AV2内置前置音量
    private static final String TV_PRE_VOLUME = "TV_PreVolume";         //TV内置前置音量
    private static final String TV2_PRE_VOLUME = "TV2_PreVolume";       //TV2内置前置音量
    private static final String AUX_PRE_VOLUME = "AUX_PreVolume";       //AUX内置前置音量
    private static final String A2DP_PRE_VOLUME = "A2DP_PreVolume";     //A2DP内置前置音量

    // 状态栏设置
    private static final String SECTION_SYSTEM_UI = "SystemUI"; // 状态栏
    private static final String STATUS_BAR_COLOR  = "StatusBarColor"; // 设置状态栏颜色

    //倒车后视
    private static final String CARCCD_TRACKOFFSET_X = "TRACK_OFFSET_X";   //倒车轨迹X轴方向偏移量
    private static final String CARCCD_TRACKOFFSET_Y = "TRACK_OFFSET_Y";   //倒车轨迹Y轴方向偏移量
    private static final int    CARCCD_TRACKOFFSET_DEFAULT = 0;        //倒车轨迹X轴方向偏移量默认值

    // TV
    private static final String TV_MODULE = "Module";   // TV模块

    // 手刹限制的应用列表配置
    private static final String SECTION_HANDBRAKE_LIMIT = "HandBrakeLimit";

    // 面板按键灯
    private static final String SECTION_PANEL_LIGHT = "PanelLight";
    private static final String PANEL_LIGHT_CONTROL = "Control"; // 面板按键灯时候可控制

    // 行车秘书
    private static final String SECTION_CAR_ASSISTANT = "CarAssistant";
    private static final String USABLE_SCREEN_HEIGHT = "UsableScreenHeight";

    private static IniFileUtil mIniFileUtil = null; // INI文件工具类对象

    /**
     * 获取配置的INAND磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getINandDiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.INAND);
    }

    /**
     * 获取配置的SD磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getSDDiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.SDCARD);
    }

    /**
     * 获取配置的SD1磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getSD1DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.SDCARD1);
    }

    /**
     * 获取配置的USB磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSBDiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB);
    }

    /**
     * 获取配置的USB1磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB1DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB1);
    }

    /**
     * 获取配置的USB2磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB2DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB2);
    }

    /**
     * 获取配置的USB3磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB3DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB3);
    }

    /**
     * 获取配置的USB4磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB4DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB4);
    }

    /**
     * 获取配置的USB5磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB5DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB5);
    }

    /**
     * 获取配置的USB6磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB6DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB6);
    }

    /**
     * 获取配置的USB7磁盘路径
     *
     * @return 配置的磁盘路径
     */
    public static String getUSB7DiskPath() {
        return getString(SECTION_ENVIRONMENT, EnvironmentUtils.USB7);
    }


    /**
     * 获取安卓摄像头Index
     * @param avId IVIAVIn.Id
     * @return 摄像头Index，用在Camera.open函数
     */
    public static int getCameraIndex(int avId) {
        return getInteger(SECTION_CAMERA_INDEX, IVIAVIn.Id.getName(avId), 0);
    }

    /**
     * 获取AVIN是否支持视频参数调节功能
     *
     * @return true支持，false不支持，默认为true
     */
    public static boolean getAVInVideoAdjustment(int avId) {
        return getBoolean(SECTION_AVIN_VIDEO_ADJUST, IVIAVIn.Id.getName(avId), true);
    }

    /**
     * 获取AVIN默认CVBS制式
     *
     * @return 见{@link {@link com.roadrover.sdk.avin.VideoParam.CvbsType}}
     */
    public static int getAVInDefaultCVBSType() {
        return getInteger(SECTION_AVIN, AVIN_DEFAULT_CVBS_TYPE, VideoParam.CvbsType.AUTO);
    }

    public static int getPowerKeyLongAction() {
        return getInteger(SECTION_POWER, POWER_LONG_ACTION, PowerKeyAction.NONE);
    }

    public static int getPowerKeyShortAction() {
        return getInteger(SECTION_POWER, POWER_SHORT_ACTION, PowerKeyAction.BKL_OFF_AND_PAUSE_MEDIA);
    }

    public static int getPowerKeyLongMaxCount() {
        return getInteger(SECTION_POWER, POWER_LONG_MAX_COUNT, POWER_LONG_MAX_COUNT_DEFAULT);
    }

    public static int getRadioDefaultLocation() {
        return getInteger(SECTION_RADIO, RADIO_LOCATION, IVIRadio.Location.ASIA);
    }

    public static boolean isRadioNeedRds() {
        return getBoolean(SECTION_RADIO, RADIO_NEED_RDS, false);
    }

    public static boolean isRadioCanRenameStation() {
        return getBoolean(SECTION_RADIO, RADIO_RENAME_STATION, true);
    }

    public static int getRadioId() {
        return getInteger(SECTION_RADIO, RADIO_ID, IVIRadio.Id.TEF6686_12MHZ);
    }

    public static String getRadioPrestoreFm(){
        return getString(SECTION_RADIO, RADIO_PRESTORE_FM);
    }

    public static boolean isRadioNeedTA() {
        return getBoolean(SECTION_RADIO, RADIO_NEED_TA, true);
    }

    /**
     * 获取收音I2C总线index
     * @param def 默认的index
     * @return
     */
    public static int getRadioI2CBusIndex(int def) {
        return getInteger(SECTION_RADIO, RADIO_I2C_BUS_INDEX, def);
    }

    /**
     * dBuV 有效电台的最低信号强度
     * @param def
     * @return
     */
    public static int getMinSignalLevel(int def) {
        return getInteger(SECTION_RADIO, RADIO_MIN_SIGNAL_LEVEL, def);
    }

    /**
     * dBuV 低于这个信号强度就开始AF搜索
     * @param def
     * @return
     */
    public static int getAFStartLevel(int def) {
        return getInteger(SECTION_RADIO, RADIO_AF_START_LEVEL, def);
    }

    /**
     * dBuV 只有信号强度大于当前信号强度超过该值的时候才切换
     * @param def
     * @return
     */
    public static int getAFMinDeltaLevel(int def) {
        return getInteger(SECTION_RADIO, RADIO_AF_MIN_DELTA_LEVEL, def);
    }

    /**
     * FM搜台条件 百分比 OLD: 20.0
     * @param def
     * @return
     */
    public static float getHeroFMUsn(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_USN, def);
    }

    /**
     * FM搜台条件 百分比 OLD: 25.0
     * @param def
     * @return
     */
    public static float getHeroFMWam(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_WAM, def);
    }

    /**
     * FM搜台条件 kHz OLD: 15.0
     * @param def
     * @return
     */
    public static float getHeroFMOffset(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_OFFSET, def);
    }

    /**
     * FM搜台条件 kHz OLD: 97.0
     * @param def
     * @return
     */
    public static float getHeroFMBw(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_BW, def);
    }

    /**
     * FM搜台条件 dBuV
     * @param def
     * @return
     */
    public static float getHeroFMAutoLevel(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_AUTO_LEVEL, def);
    }

    /**
     * FM搜台条件 dBuV
     * @param def
     * @return
     */
    public static float getHeroFMManualLevel(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_FM_MANUAL_LEVEL, def);
    }

    /**
     * AM搜台条件 kHz
     * @param def
     * @return
     */
    public static float getHeroAMOffset(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_AM_OFFSET, def);
    }

    /**
     * AM搜台条件 kHz
     * @param def
     * @return
     */
    public static float getHeroAMBw(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_AM_BW, def);
    }

    /**
     * AM搜台条件 dBuV
     * @param def
     * @return
     */
    public static float getHeroAMAutoLevel(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_AM_AUTO_LEVEL, def);
    }

    /**
     * AM搜台条件 dBuV
     * @param def
     * @return
     */
    public static float getHeroAMManualLevel(float def) {
        return getFloat(SECTION_RADIO, RADIO_HERO_AM_MANUAL_LEVEL, def);
    }

    /**
     * 获取收音立体声的信号强度，默认27dB
     * @return
     */
    public static int getSTSignalStrength() {
        return getInteger(SECTION_RADIO, RADIO_ST_SIGNAL_STRENGTH, 27);
    }

    /**
     * 获取MediaDemo的音频目录
     * @return 音频目录
     */
    public static String getMediaDemoAudioDir() {
        return getString(SECTION_MEDIA_DEMO, AUDIO_DIR);
    }

    /**
     * 获取MediaDemo的视频目录
     * @return 视频目录
     */
    public static String getMediaDemoVideoDir() {
        return getString(SECTION_MEDIA_DEMO, VIDEO_DIR);
    }

    /**
     * 获取MediaDemo的图片目录
     * @return 图片目录
     */
    public static String getMediaDemoPictureDir() {
        return getString(SECTION_MEDIA_DEMO, PICTURE_DIR);
    }

    /**
     * 获取图库是否作为模式
     * @return true模式 反之
     */
    public static boolean getGallerySource() {
        return getBoolean(SECTION_GALLERY, GALLERY_SOURCE, true);
    }

    /**
     * 背光变化是否全部线性
     * @return
     */
    public static boolean getBrightnessChangeLinearable() {
        return getBoolean(SECTION_BRIGHTNESS, BRIGHTNESS_NEED_LINEAR_VARIATE, false);
    }

    /**
     * 获取 home 的长按次数
     * @return
     */
    public static int getHomeKeyLongCount() {
        return getInteger(SECTION_HOME, HOME_LONG_MAX_COUNT, 0);
    }

    /**
     * 获取 home 按键的长按动作
     * @return
     */
    public static String getHomeKeyLongAction() {
        return getString(SECTION_HOME, HOME_LONG_ACTION);
    }

    /**
     * 获取蓝牙默认名字
     * @return
     */
    public static String getBluetoothName() {
        return getString(SECTION_BLUETOOTH, BLUETOOTH_NAME);
    }

    /**
     * 获取蓝牙默认pin码
     * @return
     */
    public static String getBluetoothPin() {
        return getString(SECTION_BLUETOOTH, BLUETOOTH_PIN);
    }

    /**
     * 获取蓝牙设备Id
     * @return IVIBluetooth.ID
     */
    public static int getBluetoothDeviceId() {
        Integer device = getInteger(SECTION_BLUETOOTH, BLUETOOTH_DEVICE_ID);
        if (device != null) {
            return device;
        }
        return IVIBluetooth.ID.LR181;
    }

    /**
     * 获取蓝牙串口文件
     * @return 串口文件字符串
     */
    public static String getBluetoothSerialFile() {
        String serialFile = getString(SECTION_BLUETOOTH, BLUETOOTH_SERIAL_FILE);
        if (serialFile != null) {
            return serialFile;
        }

        return T3_BT_SERIAL_FILE;
    }

    /**
     * 获取蓝牙串口波特率
     * @return 串口波特率
     */
    public static int getBluetoothSerialBaudRate() {
        Integer baudRate = getInteger(SECTION_BLUETOOTH, BLUETOOTH_SERIAL_BAUD_RATE);
        if (baudRate != null) {
            return baudRate;
        }

        return 9600;
    }

    /**
     * 获取蓝牙默认是否自动接听
     * @return
     */
    public static boolean getBluetoothDefAutoListen() {
        return getBoolean(SECTION_BLUETOOTH, AUTO_LISTEN, false);
    }

    /**
     * 获取蓝牙模块，默认是否自动连接
     * @return
     */
    public static boolean getBluetoothDefAutoLink() {
        return getBoolean(SECTION_BLUETOOTH, AUTO_LINK, true);
    }

    /**
     * 获取蓝牙断开之后，是否强制连接标志
     * @return
     */
    public static boolean getBluetoothForceLink() {
        return getBoolean(SECTION_BLUETOOTH, FORCE_LINK, false);
    }

    /**
     * 获取蓝牙模块连接之后，跳转哪个页面
     * @return
     */
    public static int getBluetoothGotoPage() {
        return getInteger(SECTION_BLUETOOTH, CONNECTED_GOTO_PAGE, BluetoothPage.UNKNOWN);
    }

    /**
     * 获取蓝牙联系人名字的格式
     * @return
     */
    public static String getBluetoothContactFormat() {
        return getString(SECTION_BLUETOOTH, BLUETOOTH_CONTACT, "");
    }

    /**
     * 获取蓝牙联系人页面字母滑动条是否显示
     * @return
     */
    public static boolean getBluetoothLetterSlideBarVisibility() {
        return getBoolean(SECTION_BLUETOOTH, LETTER_SLIDE_BAR, true);
    }

    public static boolean isBluetoothContactSpecialFirst() {
        return getBoolean(SECTION_BLUETOOTH, BLUETOOTH_CONTACT_SORT,false);
    }
    /**
     * 获取声控可见性
     * @return
     */
    public static boolean getVoiceVisible() {
        return getBoolean(SECTION_VOICE, VISIBLE, true);
    }

    /**
     * 获取悬浮窗是否贴边
     * @return
     */
    public static boolean getFloatMenuNestleSide() {
        return getBoolean(SECTION_FLOAT_MENU, NESTLE_SIDE, true);
    }

    /**
     * 在关屏状态下，有作用，并且保持关屏状态
     * @return
     */
    public static List<Integer> getScreenOffKeyActionList() {
        List<Integer> ret = getScreenOffKeyList(KEY_ACTION);
        if (ListUtils.isEmpty(ret)) {
            // 如果没有配置，则使用如下默认值
            ret = new ArrayList<>();
            ret.add(IVICar.Key.Id.VOLUME_UP);
            ret.add(IVICar.Key.Id.VOLUME_DOWN);
            ret.add(IVICar.Key.Id.MUTE);
            ret.add(IVICar.Key.Id.PLAY_PAUSE);
            ret.add(IVICar.Key.Id.PREV);
            ret.add(IVICar.Key.Id.NEXT);
            ret.add(IVICar.Key.Id.HANG_UP);
            ret.add(IVICar.Key.Id.PREV_ANSWER);
            ret.add(IVICar.Key.Id.NEXT_HANGUP);
        }
        return ret;
    }

    /**
     * 在关屏状态下，按键不执行动作
     * @return
     */
    public static List<Integer> getScreenOffKeyNoActionList() {
        return getScreenOffKeyList(KEY_NO_ACTION);
    }

    /**
     * 获取倒车轨迹X方向偏移量
     * @return
     */
    public static int getCarCCDTrackOffsetByX(){
        return getInteger(SECTION_CAR_CCD,CARCCD_TRACKOFFSET_X,CARCCD_TRACKOFFSET_DEFAULT);
    }

    /**
     * 获取倒车轨迹Y方向偏移量
     * @return
     */
    public static int getCarCCDTrackOffsetByY(){
        return getInteger(SECTION_CAR_CCD,CARCCD_TRACKOFFSET_Y,CARCCD_TRACKOFFSET_DEFAULT);
    }

    /**
     * 获取TV模块
     * @return
     */
    public static int getTVModule() {
        return getInteger(SECTION_TV, TV_MODULE, IVITV.Module.NONE);
    }

    /**
     * 获取关屏状态下按键执行配置列表
     * @param name
     * @return
     */
    private static List<Integer> getScreenOffKeyList(String name) {
        List<Integer> ret = new ArrayList<>();
        String[] array = getStringArray(SECTION_SCREEN_OFF, name);
        if (array != null) {
            final int length = array.length;
            for (int i = 0;i < length;i++) {
                final int id = IVICar.Key.getId(array[i]);
                if (id != IVICar.Key.Id.NONE) {
                    ret.add(id);
                }
            }
        }
        return ret;
    }

    /**
     * 获取内置前置音量，单位dB
     * @param  channel IVIAudio.Channel
     * @param  defaultValue  默认值
     * @return 前置音量，单位dB
     */
    public static float getBuildInPreVolume(int channel, float defaultValue) {
        switch (channel) {
            case IVIAudio.Channel.PC:
                return getFloat(SECTION_AUDIO, PC_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.MONO:
                return getFloat(SECTION_AUDIO, MONO_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.RADIO:
            case IVIAudio.Channel.RADIO_TA:
                return getFloat(SECTION_AUDIO, RADIO_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.AV:
                return getFloat(SECTION_AUDIO, AV_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.AV2:
                return getFloat(SECTION_AUDIO, AV2_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.TV:
                return getFloat(SECTION_AUDIO, TV_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.TV2:
                return getFloat(SECTION_AUDIO, TV2_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.AUX:
                return getFloat(SECTION_AUDIO, AUX_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.BLUETOOTH_A2DP:
                return getFloat(SECTION_AUDIO, A2DP_PRE_VOLUME, defaultValue);

            default:
                Logcat.w("Not realization channel: " + IVIAudio.Channel.getName(channel));
                return defaultValue;
        }
    }

    /**
     * 获取只可读文件夹
     * @return 文件夹列表
     */
    public static synchronized List<String> getOnlyReadDirs() {
        ArrayList<String> dirs = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(SECTION_FILE_MANAGER);
            if (section != null) {
                Map<String, Object> values = section.getValues();
                String prefix = getINandDiskPath() + "/";
                if (values != null) {
                    Set<String> keys = values.keySet();
                    for (String key : keys) {
                        try {
                            String value = (String) values.get(key);
                            dirs.add(prefix + value);
                        } catch (Exception e) {

                        }
                    }
                }
            }

            dirs.addAll(getMapDataDirs());
        }
        return dirs;
    }

    public static synchronized List<String> getMapDataDirs() {
        ArrayList<String> dirs = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(SECTION_MAP_DATA);
            if (section != null) {
                Map<String, Object> values = section.getValues();
                if (values != null) {
                    Set<String> keys = values.keySet();
                    for (String key : keys) {
                        try {
                            String value = (String) values.get(key);
                            dirs.add(value);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
        return dirs;
    }

    public static Integer getUsableScreenHeight() {
        return getInteger(SECTION_CAR_ASSISTANT, USABLE_SCREEN_HEIGHT);
    }

    /**
     * 获取INI解析对象
     * @return INI解析对象
     */
    public static IniFileUtil getIniFileUtil() {
        checkRead();
        return mIniFileUtil;
    }
	
	/**
     * 获取配置字段
     *
     * @param section section名称
     * @param key     key名称
     * @return 配置字段
     */
    public static synchronized Object get(String section, String key) {
        boolean ret = false;
        if (checkRead() && mIniFileUtil != null) {
            Object object = mIniFileUtil.get(section, key);
            if (null != object) {
                if (object instanceof Boolean) {
                    ret = (Boolean) object;
                } else {
                    Logcat.w("failed, " + object + " is not instanceof String.");
                }
            }
        }

        return ret;
    }

    /**
     * 获取配置字段
     * @return 配置字段
     */
    public static synchronized SparseArray<String> getRemoteControlKeyCodes() {
        SparseArray<String> keyCodes = new SparseArray<>();
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(SECTION_REMOTE_CONTROL);
            if (section != null) {
                Map<String, Object> values = section.getValues();
                if (values != null) {
                    Set<String> keys = values.keySet();
                    for (String key : keys) {
                        try {
                            int keyCode = Integer.valueOf(key);
                            String value = (String) values.get(key);
                            keyCodes.append(keyCode, value); // 获取所有键值
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
        return keyCodes;
    }

    /**
     * 获取mode键控制的app列表
     * @return
     */
    public static List<String> getModeControlAppList() {
        List<String> apps = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            Integer count = getInteger(SECTION_MODE_CONTROL, MODE_COUNT);
            Logcat.d("count:" + count);
            if (count != null) {
                for (int i = 0; i < count; ++i) {
                    String app = getString(SECTION_MODE_CONTROL, MODE_APP + i);
                    if (!TextUtils.isEmpty(app)) {
                        apps.add(app);
                    }
                }
            }
        }
        return apps;
    }

    /**
     * 获取休眠唤醒后记忆到主页的应用列表
     * @return
     */
    public static List<String> getMemoryToHomeAppList() {
        List<String> apps = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            Integer count = getInteger(SECTION_MEMORY_TO_HOME, MEMORY_COUNT);
            Logcat.d("count:" + count);
            if (count != null) {
                for (int i = 0; i < count; ++i) {
                    String app = getString(SECTION_MEMORY_TO_HOME, MEMORY_APP + i);
                    if (!TextUtils.isEmpty(app)) {
                        apps.add(app);
                    }
                }
            }
        }
        return apps;
    }

    /**
     * 获取平台支持的视频格式
     * @return
     */
    public static String[] getVideoFormats() {
        return getStringArray(SECTION_MEDIA, VIDEO_FORMAT);
    }

    /**
     * 获取平台支持的音乐格式
     * @return
     */
    public static String[] getMusicFormats() {
        return getStringArray(SECTION_MEDIA, MUSIC_FORMAT);
    }

    /**
     * 获取平台支持的图片格式
     * @return
     */
    public static String[] getImageFormats() {
        return getStringArray(SECTION_MEDIA, IMAGE_FORMAT);
    }

    /**
     * 获取状态栏的颜色，不设置沉浸式为 -1
     * @return
     */
    public static int getStatusBarColor() {
        String color = getString(SECTION_SYSTEM_UI, STATUS_BAR_COLOR);
        if (!TextUtils.isEmpty(color)) {
            try {
                return Color.parseColor("#" + color);
            } catch (Exception e) {

            }
        }
        return -1;
    }
	
	/**
     * 获取是否需要发送播放时间
     * @return
     */
    public static boolean getNeedMediaPlayTime() {
        return getBoolean(SECTION_MEDIA, MEDIA_NEED_PLAYTIME, false);
    }

    /**
     * 获取配置的视频通道
     * @param avId 见{@link com.roadrover.sdk.avin.IVIAVIn.Id}
     * @return
     */
    public static int getConfigVideoChannel(int avId) {
        Map<Integer, Integer> map = getConfigVideoChannels();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (avId == entry.getKey()) {
                return entry.getValue();
            }
        }
        return avId;
    }

    /**
     * 获取配置的音频通道
     * @param channel 见{@link com.roadrover.sdk.audio.IVIAudio.Channel}
     * @return
     */
    public static int getConfigAudioChannel(int channel) {
        Map<Integer, Integer> map = getConfigAudioChannels();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (channel == entry.getKey()) {
                return entry.getValue();
            }
        }
        return channel;
    }

    /**
     * 获取配置的第二音频通道
     * @param channel 见{@link com.roadrover.sdk.audio.IVIAudio.Channel}
     * @return
     */
    public static int getConfigSecondaryAudioChannel(int channel) {
        Map<Integer, Integer> map = getConfigSecondaryAudioChannels();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (channel == entry.getKey()) {
                return entry.getValue();
            }
        }
        return channel;
    }

    /**
     * 获取配置的视频通道
     * @return
     */
    private static Map<Integer, Integer> getConfigVideoChannels() {
        Map<Integer, Integer> ret = new HashMap<>();
        ArrayList<Integer> list = IVIAVIn.Id.getIds();
        if (null != list) {
            for (Integer id : list) {
                String configAVName = getString(SECTION_VIDEO_CHANNEL_CONFIG, IVIAVIn.Id.getName(id));
                int configAVId = IVIAVIn.Id.getId(configAVName);
                if (IVIAVIn.Id.NONE != configAVId) {
                    ret.put(id, configAVId);
                }
            }
        }
        return ret;
    }

    /**
     * 获取配置的音频通道
     * @return
     */
    private static Map<Integer, Integer> getConfigAudioChannels() {
        Map<Integer, Integer> ret = new HashMap<>();
        ArrayList<Integer> list = IVIAudio.Channel.getChannels();
        if (null != list) {
            for (Integer channel : list) {
                String configChannelName = getString(SECTION_AUDIO_CHANNEL_CONFIG, IVIAudio.Channel.getName(channel));
                int configChannelId = IVIAudio.Channel.getChannel(configChannelName);
                if (IVIAVIn.Id.NONE != configChannelId) {
                    ret.put(channel, configChannelId);
                }
            }
        }
        return ret;
    }

    /**
     * 获取配置的第二音频通道
     * @return
     */
    private static Map<Integer, Integer> getConfigSecondaryAudioChannels() {
        Map<Integer, Integer> ret = new HashMap<>();
        ArrayList<Integer> list = IVIAudio.Channel.getChannels();
        if (null != list) {
            for (Integer channel : list) {
                String configChannelName = getString(SECTION_SECONDARY_AUDIO_CHANNEL_CONFIG, IVIAudio.Channel.getName(channel));
                int configChannelId = IVIAudio.Channel.getChannel(configChannelName);
                if (IVIAVIn.Id.NONE != configChannelId) {
                    ret.put(channel, configChannelId);
                }
            }
        }
        return ret;
    }

    /**
     * 获取配置文件中的 StringArray
     * @param section
     * @param name
     * @return
     */
    private static String[] getStringArray(String section, String name) {
        String tmp = getString(section, name);
        if (!TextUtils.isEmpty(tmp)) {
            return tmp.split(",");
        }
        return new String[0];
    }

    /**
     * 获取配置字符串
     *
     * @param section section名称
     * @param key     key名称
     * @return 配置字符串
     */
    public static synchronized String getString(String section, String key) {
        String ret = null;
        if (checkRead() && mIniFileUtil != null) {
            Object object = mIniFileUtil.get(section, key);
            if (null != object) {
                if (object instanceof String) {
                    ret = (String) object;
                } else {
                    Logcat.w("failed, " + object + " is not instanceof String.");
                }
            }
        }

        return ret;
    }

    /**
     * 获取配置的字符串
     * @param section
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static String getString(String section, String key, String defValue) {
        String ret = getString(section, key);
        if (TextUtils.isEmpty(ret)) {
            ret = defValue;
        }
        return ret;
    }

    /**
     * 获取整型
     *
     * @param section section名称
     * @param key     key名称
     * @return 整型值
     */
    public static Integer getInteger(String section, String key) {
        Integer ret = null;
        final String string = getString(section, key);
        if (null != string) {
            try {
                ret = Integer.valueOf(string);
            } catch (Exception e) {
                Logcat.w("failed, " + section + " " + key);
            }
        }
        return ret;
    }

    /**
     * 获取浮点值
     *
     * @param section section名称
     * @param key     key名称
     * @return 浮点值
     */
    private static Float getFloat(String section, String key) {
        Float ret = null;
        final String string = getString(section, key);
        if (null != string) {
            try {
                ret = Float.valueOf(string);
            } catch (Exception e) {
                Logcat.w("failed, " + section + " " + key);
            }
        }
        return ret;
    }

    /**
     * 获取整型
     *
     * @param section section名称
     * @param key     key名称
     * @param defaultValue    默认值
     * @return 整型值
     */
    public static int getInteger(String section, String key, int defaultValue) {
        Integer ret = getInteger(section, key);
        if (ret != null) {
            return ret;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取浮动数
     *
     * @param section section名称
     * @param key     key名称
     * @param defaultValue    默认值
     * @return 整型值
     */
    public static float getFloat(String section, String key, float defaultValue) {
        Float ret = getFloat(section, key);
        if (ret != null) {
            return ret;
        } else {
            return defaultValue;
        }
    }

    /**
     * 获取布尔型
     *
     * @param section      section名称
     * @param key          key名称
     * @param defaultValue 默认值
     * @return 整型值
     */
    public static synchronized boolean getBoolean(String section, String key, boolean defaultValue) {
        boolean ret = defaultValue;
        final String string = getString(section, key);
        if (null != string) {
            if ("true".equals(string)) {
                ret = true;
            } else if ("false".equals(string)) {
                ret = false;
            } else {
                Logcat.w("failed, " + section + " " + key + " " + defaultValue);
            }
        }
        return ret;
    }

    /**
     * 检查可读
     */
    private static boolean checkRead() {
        if (null == mIniFileUtil) {
            File file = new File(INI_FILE_PATH);
            if (file.exists()) {
                Logcat.d("ready to read " + INI_FILE_PATH);
                mIniFileUtil = new IniFileUtil(file);
                return true;
            } else {
                Logcat.e("failed, file " + INI_FILE_PATH + " not exist.");
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 获取手刹限制的应用列表
     * @return
     */
    public static List<String> getHandBrakeLimitAppList() {
        return getStringList(SECTION_HANDBRAKE_LIMIT);
    }

    /**
     * 返回一个string list
     * @return
     */
    public static synchronized List<String> getStringList(String sectionName) {
        List<String> apps = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(sectionName);
            if (section != null) {
                Map<String, Object> values = section.getValues();
                if (values != null) {
                    Set<String> keys = values.keySet();
                    for (String key : keys) {
                        try {
                            String value = (String) values.get(key);
                            apps.add(value);
                        } catch (Exception e) {
                        }
                    } // end of  for (String key : keys)
                } // end of  if (values != null)
            } // end of  if (section != null)
        }
        return apps;
    }

    /**
     * 判断面板按键灯是否可控制，当按下的时候，亮面板按键灯，一段时间之后，灭
     * @return
     */
    public static boolean isPanelLightAllowControl() {
        return getBoolean(SECTION_PANEL_LIGHT, PANEL_LIGHT_CONTROL, false);
    }
}
