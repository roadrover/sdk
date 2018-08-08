package com.roadrover.sdk.system;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.roadrover.sdk.audio.AudioParam;
import com.roadrover.sdk.audio.IVIAudio;
import com.roadrover.sdk.avin.IVIAVIn;
import com.roadrover.sdk.avin.IVITV;
import com.roadrover.sdk.avin.VideoParam;
import com.roadrover.sdk.bluetooth.IVIBluetooth;
import com.roadrover.sdk.car.Climate;
import com.roadrover.sdk.car.IVICar;
import com.roadrover.sdk.cluster.IVICluster;
import com.roadrover.sdk.dab.IVIDAB;
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

    /** 配置文件路径 */
    public static final String INI_FILE_PATH = "/etc/ivi-config.ini";

    /** 存储设备section名称 */
    private static final String SECTION_ENVIRONMENT = "Environment";
    /** Android摄像头index组名 */
    private static final String SECTION_CAMERA_INDEX = "CameraIndex";
    /** AC8 CameraIndex和Port需要分开设置名 */
    private static final String SECTION_CAMERA_AVIN_PORT = "AVINVideoPort";
    /** AC8 AVIN音频端口配置名 */
    private static final String SECTION_AVIN_AUDIO_PORT = "AVINAudioPort";
    /** 是否支持AVIN视频参数调节功能名 */
    private static final String SECTION_AVIN_VIDEO_ADJUST = "AVInVideoAdjust";
    /** 只读文件定义组名 */
    private static final String SECTION_FILE_MANAGER = "FileManager";
    /** 遥控按键配置组名 */
    private static final String SECTION_REMOTE_CONTROL = "RemoteControl";
    /** 地图数据文件夹的组名 */
    private static final String SECTION_MAP_DATA = "MapData";
    /** 按键防抖时间配置的组名 */
    private static final String SECTION_KEY_ANTI_SHAKE = "KeyAntiShake";
    /** 亮度调节配置的组 */
    private static final String SECTION_BRIGHTNESS = "Brightness";
    /** 背光是否线性调节 */
    private static final String BRIGHTNESS_NEED_LINEAR_VARIATE = "LinearVariation";
    /** 最大亮度 */
    private static final String BRIGHTNESS_MAX = "Max";
    /** 最小亮度 */
    private static final String BRIGHTNESS_MIN = "Min";

    // AV功能配置 start
    /** AV功能配置定义组名 */
    private static final String SECTION_AVIN = "AVIn";
    /** 默认CVBS类型 */
    private static final String AVIN_DEFAULT_CVBS_TYPE = "DefaultCVBSType";
    /** CVBS类型检测次数 */
    private static final String AVIN_CHANGE_CVBS_TYPE_COUNT = "ChangeCVBSTypeCount";
    /** 默认亮度 */
    private static final String AVIN_DEFAULT_BRIGHTNESS = "DefaultBrightness";
    /** 默认对比度 */
    private static final String AVIN_DEFAULT_CONTRAST = "DefaultContrast";
    /** 默认饱和度 */
    private static final String AVIN_DEFAULT_SATURATION = "DefaultSaturation";
    /**延时显示视频 */
    private static final String AVIN_DELAY_SHOW_VIDEO = "DelayShowVideo";
    /** 倒车摄像头电源是否长供电 */
    private static final String CCD_LONG_SUPPLY_POWER = "CcdLongSupplyPower";
    /** 车内摄像头是否长供电 */
    private static final String INNER_CAMERA_LONG_SUPPLY_POWER = "InnerCameraLongSupplyPower";
    /** 行李箱摄像头是否长供电 */
    private static final String LUGGAGE_DOOR_LONG_SUPPLY_POWER = "LuggageDoorLongSupplyPower";
    /** 中门摄像头是否长供电 */
    private static final String MID_DOOR_CAMERA_SUPPLY_POWER = "MidDoorCameraSupplyPower";
    /** 是否支持AVOut功能*/
    private static final String ENABLE_AVOUT = "EnableAvOut";
    // AV功能配置 end

    /** Section Video Channel Config */
    private static final String SECTION_VIDEO_CHANNEL_CONFIG = "VideoChannelConfig";

    /** 音频通道配置 */
    private static final String SECTION_AUDIO_CHANNEL_CONFIG = "AudioChannelConfig";
    /** 第二音频通道配置 */
    private static final String SECTION_SECONDARY_AUDIO_CHANNEL_CONFIG = "SecondaryAudioChannelConfig";

    /** 语音配置 */
    private static final String SECTION_VOICE = "Voice";
    /** 声控是否可见 */
    private static final String VISIBLE = "Visible";

    /** 发送系统时间 */
    private static final String SECTION_SET_SYSTEM_TIME = "SetSystemTime";
    /** 每秒发一次系统时间 */
    private static final String SEND_TIME_EVERY_SECOND = "SendTimeEverySecond";
    /** 半小时机制配置 */
    private static final String SECTION_HALF_HOUR_MECHANISM = "HalfHourMechanism";
    /** 下位机是否支持半小时机制 */
    private static final String ENALE_HALF_HOUR_MECHANISM = "EnableHalfHourMechanism";

    /** 文件夹监听 */
    private static final String SECTION_FILE_OBSERVER = "MultiFileObserver";
    /** 是否需要监听文件目录变化 */
    private static final String NEED_OBSERVER = "NeedObserver";

    /** 倒车雷达配置*/
    private static final String SECTION_CCD_RADAR = "ccdRadar";
    /** 是否支持倒车雷达声音*/
    private static final String ENABLE_CCD_RADAR = "EnableCcdRadar";

    /** 音频焦点配置 */
    private static final String SECTION_AUDIO_FOCUS = "audioFocus";
    /** 是否根据当前音频焦点设置音量 */
    private static final String ENABLE_DYNAMIC_AUDIO_FOCUS = "EnableDynamicAudioFocus";

    // 音量配置 start
    /** 音量配置 */
    private static final String SECTION_VOLUME = "Volume";
    /** 第一次开机音频参数区域变化配置 */
    private static final String VOLUME_BATTERY_ON_AREA_CHANGE = "BatteryOnAreaChange";
    /** 开机音频参数区域变化配置 */
    private static final String VOLUME_POWER_ON_AREA_CHANGE = "PowerOnAreaChange";
    /** 唤醒开机音频参数区域变化配置*/
    private static final String VOLUME_WAKE_UP_AREA_CHANGE = "WakeUpAreaChange";
    /** 屏幕开机音频参数区域变化配置 */
    private static final String VOLUME_SCREEN_ON_AREA_CHANGE = "ScreenOnAreaChange";
    /**恢复默认设置时的主音量配置*/
    private static final String DEFAULT_VOLUME_MASTER = "DefaultVolumeMaster";
    /**恢复默认设置时的蓝牙通话音量配置*/
    private static final String DEFAULT_VOLUME_BLUETOOTH = "DefaultVolumeBluetooth";
    /**恢复默认设置时的蓝牙铃声配置*/
    private static final String DEFAULT_VOLUME_BLUETOOTH_RING = "DefaultVolumeBluetoothRing";
    // 音量配置 end

    /** 记忆应用 */
    private static final String SECTION_MEMORY = "Memory";
    /** 记忆的包名 */
    private static final String MEMORY_PACKAGE_NAME = "PackageName";
    /** 关屏acc on */
    private static final String SECTION_ACC_OPEN_SCREEN = "AccOpenScreen";
    /** 关屏状态下acc on 是否支持开屏 */
    private static final String IS_ACC_OPEN_SCREEN = "IsAccOpenScreenOnLockScreen";

    // power 按键定义 start
    /** power按键定义组名 */
    private static final String SECTION_POWER = "Power";
    /** 长按动作 {@link com.roadrover.sdk.system.IVIKey.PowerKeyAction} */
    private static final String POWER_LONG_ACTION = "LONG_ACTION";
    /** 短按动作 {@link com.roadrover.sdk.system.IVIKey.PowerKeyAction} */
    private static final String POWER_SHORT_ACTION = "SHORT_ACTION";
    /** 长按多少次算长按，一次100ms，第一次500ms，例：长按3S算长按，则应该配置 26 */
    private static final String POWER_LONG_MAX_COUNT = "LONG_MAX_COUNT";
    /** 默认次数 */
    private static final int POWER_LONG_MAX_COUNT_DEFAULT = 100;
    // power 按键定义 end

    // 收音机 定义 start
    /** 收音机配置组名 */
    private static final String SECTION_RADIO = "Radio";
    /** 区域配置 */
    private static final String RADIO_LOCATION = "Location";
    /** 是否需要rds */
    private static final String RADIO_NEED_RDS = "NeedRds";
    /** 是否需要区域选择 */
    private static final String RADIO_NEED_LOCATION = "NeedLocation";
    /** 是否允许重命名电台 */
    private static final String RADIO_RENAME_STATION = "RenameStation";
    /** 收音机id配置，{@link com.roadrover.sdk.radio.IVIRadio.Id} */
    private static final String RADIO_ID = "RadioId";
    /** 预存电台 */
    private static final String RADIO_PRESTORE_FM = "RadioPrestoreFm";
    /** 是否需要TA，该配置需要NeedRds为true，配置才有用 */
    private static final String RADIO_NEED_TA = "NeedTA";
    /** 获取I2C总线的index */
    private static final String RADIO_I2C_BUS_INDEX = "I2CBusIndex";
    /** 有效电台的最低信号强度 */
    private static final String RADIO_MIN_SIGNAL_LEVEL = "MinSignalLevel";
    /** 低于这个信号强度就开始AF搜索 */
    private static final String RADIO_AF_START_LEVEL = "AFStartLevel";
    /** 只有信号强度大于当前信号强度超过该值的时候才切换 */
    private static final String RADIO_AF_MIN_DELTA_LEVEL = "AFMinDeltaLevel";
    /** FM搜台条件 百分比 OLD: 20.0 */
    private static final String RADIO_HERO_FM_USN = "HeroFMUsn";
    /** FM搜台条件 百分比 OLD: 25.0 */
    private static final String RADIO_HERO_FM_WAM = "HeroFMWam";
    /** FM搜台条件 kHz OLD: 15.0 */
    private static final String RADIO_HERO_FM_OFFSET = "HeroFMOffset";
    /** FM搜台条件 kHz OLD: 97.0 */
    private static final String RADIO_HERO_FM_BW = "HeroFMBw";
    /** FM搜台条件 dBuV */
    private static final String RADIO_HERO_FM_AUTO_LEVEL = "HeroFMAutoLevel";
    /** FM搜台条件 dBuV */
    private static final String RADIO_HERO_FM_MANUAL_LEVEL = "HeroFMManualLevel";
    /** AM搜台条件 kHz */
    private static final String RADIO_HERO_AM_OFFSET = "HeroAMOffset";
    /** AM搜台条件 kHz */
    private static final String RADIO_HERO_AM_BW = "HeroAMBw";
    /** AM搜台条件 dBuV */
    private static final String RADIO_HERO_AM_AUTO_LEVEL = "HeroAMAutoLevel";
    /** AM搜台条件 dBuV */
    private static final String RADIO_HERO_AM_MANUAL_LEVEL = "HeroAMManualLevel";
    /** 获取收音立体声的信号强度，默认27dB */
    private static final String RADIO_ST_SIGNAL_STRENGTH = "STSignalStrength";
    /** 获取FM输出增益 */
    private static final String RADIO_FM_OUTPUT_GAIN = "FMOutputGain";
    /** 获取AM输出增益 */
    private static final String RADIO_AM_OUTPUT_GAIN = "AMOutputGain";
    /** 是否开启收音机无信号检测静音功能 */
    private static final String RADIO_NO_SIGNAL_DETECTION_MUTE = "IsOpenSignalDetectionMute";
    /** 从有信号到无信号检测次数，一次间隔50ms，实际时间会又偏差，获取信号会有一定的延时，间隔时间肯定会大于50ms */
    private static final String RADIO_NO_SIGNAL_DETECTION_COUNT = "NoSignalDetectionCount";
    /** 从无信号到有信号检测次数 */
    private static final String RADIO_HAS_SIGNAL_DETECTION_COUNT = "HasSignalDetectionCount";
    /** 临界信号值，最低信号强度 */
    private static final String RADIO_SIGNAL_MIN = "SignalMin";
    // 收音机定义 end

    // mode 键配置 start
    /** mode 按键定义组名 */
    private static final String SECTION_MODE_CONTROL = "ModeControl";
    /** mode 按键配置的应用个数 */
    private static final String MODE_COUNT = "COUNT";
    /** mode 按键配置的app，例:APP0=com.roadrover.radio_v2 */
    private static final String MODE_APP = "APP";
    /** 长按次数 */
    private static final String MODE_LONG_MAX_COUNT = "LONG_MAX_COUNT";
    /** 长按动作 */
    private static final String MODE_LONG_ACTION = "LONG_ACTION";
    // mode 键配置 end

    // Home 按键定义 start
    /** home 按键的组名 */
    private static final String SECTION_HOME = "Home";
    /** 长按次数 */
    private static final String HOME_LONG_MAX_COUNT = "LONG_MAX_COUNT";
    /** 长按动作，可以是包名，包名必须/结尾，也可以 {@link com.roadrover.sdk.system.IVIKey.Key.Name} */
    private static final String HOME_LONG_ACTION = "LONG_ACTION";
    // Home 按键定义 end

    /** 记忆home状态下的组名 */
    private static final String SECTION_MEMORY_TO_HOME = "MemoryToHome";
    /** 开机记忆启动应用的组名**/
    private static final String SECTION_BOOT_MEMORY = "BootMemory";
    /** 数量 */
    private static final String MEMORY_COUNT = "COUNT";
    /** 休眠唤醒后记忆到主页的应用列表 */
    private static final String MEMORY_APP = "APP";

    // 蓝牙配置 start
    private static final String SECTION_BLUETOOTH = "Bluetooth";
    /** 蓝牙默认名字 */
    private static final String BLUETOOTH_NAME = "BT_NAME";
    /** 蓝牙默认pin码 */
    private static final String BLUETOOTH_PIN = "BT_PIN";
    /** 蓝牙设备 */
    private static final String BLUETOOTH_DEVICE_ID = "BT_DEVICE";
    /** 蓝牙设备串口文件名 */
    private static final String BLUETOOTH_SERIAL_FILE = "BT_SERIAL";
    /** 蓝牙设备串口波特率 */
    private static final String BLUETOOTH_SERIAL_BAUD_RATE = "BT_BAUD_RATE";
    /** 连接超时自动重连次数 0表示禁止 */
    private static final String BLUETOOTH_TIMEOUT_RELINK = "BT_TIMEOUT_RELINK";
    /** 自动接听 */
    private static final String AUTO_LISTEN = "AUTO_LISTEN";
    /** 自动连接 */
    private static final String AUTO_LINK = "AUTO_LINK";
    /** 是否断开之后强制连接 */
    private static final String FORCE_LINK = "FORCE_LINK";
    /** 连接之后，强制调转哪个页面 */
    private static final String CONNECTED_GOTO_PAGE = "CONNECTED_GOTO_PAGE";
    /** 蓝牙联系人名字的格式 */
    private static final String BLUETOOTH_CONTACT = "BT_CONTACT";
    /** 蓝牙通讯录界面是否需要显示滑动条 */
    private static final String LETTER_SLIDE_BAR = "LETTER_SLIDE_BAR";
    /** 蓝牙通话界面是否显示启动导航按钮 */
    private static final String BLUETOOTH_NAVI_START = "BT_NAVI_START";
    /** 蓝牙通话界面静音按钮功能是否为静车机的麦克风输入 */
    private static final String BLUETOOTH_MUTE_MIC = "BT_MUTE_MIC";
    /** 蓝牙电话本排序方式，特殊字符排前还是排后 */
    private static final String BLUETOOTH_CONTACT_SORT = "BT_CONTACT_SPECIAL_FIRST";
    /** 本机蓝牙名称限制长度 */
    private static final String BLUETOOTH_NAME_LIMIT_LENGTH = "BLUETOOTH_NAME_LIMIT_LENGTH";
    // 蓝牙配置 end

    // 面板按键灯状态配置 start
    /** 面板按键 定义组名 */
    private static final String PANEL_LIGHT_POWER = "PanelLightPower";
    /** 面板按键灯的配置 */
    private static final String PANEL_LIGHT_POWER_STATUS = "PanelLightPowerStatus"; // 默认为0，0的时候正常亮灭，1常亮，2常灭

    public static class PanelLightPowerStatus {
        /** 大灯正常切换 */
        public static final int PANEL_LIGHT_NORMAL      = 0;
        /** 大灯保持常亮 */
        public static final int PANEL_LIGHT_KEEP_ON     = 1;
        /** 大灯保持常灭 */
        public static final int PANEL_LIGHT_KEEP_OFF    = 2;

    }
    // 面板按键灯状态配置 end

    // 倒车时媒体状态处理 start
    /** 倒车时媒体状态处理方式 定义组名 */
    private static final String CCD_MEDIA_STATUS = "CcdMediaStatus";
    /** 倒车时媒体状态处理 */
    private static final String MEDIA_STATUS_ON_CCD = "mediaStatusOnCcd";

    public static class MediaStatusOnCcd {
        /** 倒车时媒体降音 */
        public static final int MEDIA_STATUS_ON_CCD_VOLUME_PERCENT = 0;
        /** 倒车时媒体暂停 */
        public static final int MEDIA_STATUS_ON_CCD_PAUSE = 1;
    }

    // 倒车时媒体状态处理 end

    // launchApp方法过滤 不后台启动的APP begin
    /** ignore 过滤应用定义组名 */
    private static final String SECTION_IGNORE_APP = "IgnoreLaunchApp";
    /** ignore 配置的应用个数 */
    private static final String IGNORE_COUNT = "COUNT";
    /** ignore 配置的app，例:APP0=net.easyconn */
    private static final String IGNORE_APP = "APP";
    // launchApp方法过滤 不后台启动的APP end

    // 系统配置 定义 start
    /** 系统配置组名 */
    private static final String SECTION_SYSTEM = "System";
    /** 固件版本号 */
    private static final String FIRMWARE_VERSION = "FIRMWARE_VERSION";
    /** 下位机升级文件头部编号 */
    private static final String MCU_FILE_HEAD_CODE = "MCU_FILE_HEAD_CODE";
    // 系统配置 定义 end

    /**
     * 跳转蓝牙页
     */
    public static class BluetoothPage {
        /** 未定义 */
        public static final int UNKNOWN  = -1;
        /** 拨号页 */
        public static final int KEY_PAD  = 0;
        /** 联系人页 */
        public static final int CONTACTS = 1;
        /** 通话记录页 */
        public static final int HISTORY  = 2;
        /** 设备页 */
        public static final int DEVICES  = 3;
        /** 设置页 */
        public static final int SETTINGS = 4;
    }

    /** 图库 */
    private static final String SECTION_GALLERY = "Gallery";
    /** 图库是否作为播放源 */
    private static final String GALLERY_SOURCE = "Source";

    /** 悬浮窗 */
    private static final String SECTION_FLOAT_MENU = "FloatMenu";
    /** 是否贴边，默认true */
    private static final String NESTLE_SIDE = "NestleSide";

    /** 关屏 */
    private static final String SECTION_SCREEN_OFF = "ScreenOff";
    /** 在关屏状态下，有作用，并且保持关屏状态 */
    private static final String KEY_ACTION = "KeyAction";
    /** 在关屏状态下，按键不执行动作 */
    private static final String KEY_NO_ACTION = "KeyNoAction";
    /** 在关屏状态下，仅仅只是打开屏幕，不执行按键功能 */
    private static final String KEY_ONLY_OPEN_SCREEN = "OnlyOpenScreen";

    // Media Demo start
    /** MediaDemo路径定义组名 */
    private static final String SECTION_MEDIA_DEMO = "MediaDemo";
    /** 音频路径 */
    private static final String AUDIO_DIR = "AudioDir";
    /** 图片路径 */
    private static final String PICTURE_DIR = "PictureDir";
    /** 视频路径 */
    private static final String VIDEO_DIR = "VideoDir";
    // Media Demo end

    // 媒体类型定义 start
    /** 媒体类型定义组 */
    private static final String SECTION_MEDIA = "Media";
    /** 视频识别的类型，Video=3g2,3gp,3gp2,3gpp,amv */
    private static final String VIDEO_FORMAT = "Video";
    /** 音乐识别的类型，Music=mp3,wav,ape */
    private static final String MUSIC_FORMAT = "Music";
    /** 图片识别的类型，Image=png,jpg */
    private static final String IMAGE_FORMAT = "Image";
    /** 播放的时候是否需要发送播放时间 */
    private static final String MEDIA_NEED_PLAYTIME = "needPlayTime";
    /** 获取媒体信息改变最小时间 */
    private static final String MEDIA_CHANGE_MIN_TIME = "ChangeMinTime";
    // 媒体类型定义 end

    // 音频 start
    // 内置前置音量不可以被应用更改，由硬件工程师根据不同的平台预先调好
    // 音频设置中另外提供个性化前置音量调节，可以根据不同的项目隐藏
    /** audio 配置的组 */
    private static final String SECTION_AUDIO = "Audio";
    /** PC、MP3、Video内置前置音量 */
    private static final String PC_PRE_VOLUME = "PC_PreVolume";
    /** PC_MUSIC内置前置音量 */
    private static final String PC_MUSIC_PRE_VOLUME = "PC_MUSIC_PreVolume";
    /** 混音通道前置音量 */
    private static final String MONO_PRE_VOLUME = "Mono_PreVolume";
    /** 收音机内置前置音量 */
    private static final String RADIO_PRE_VOLUME = "Radio_PreVolume";
    /** AV内置前置音量 */
    private static final String AV_PRE_VOLUME = "AV_PreVolume";
    /** AV2内置前置音量 */
    private static final String AV2_PRE_VOLUME = "AV2_PreVolume";
    /** TV内置前置音量 */
    private static final String TV_PRE_VOLUME = "TV_PreVolume";
    /** TV2内置前置音量 */
    private static final String TV2_PRE_VOLUME = "TV2_PreVolume";
    /** AUX内置前置音量 */
    private static final String AUX_PRE_VOLUME = "AUX_PreVolume";
    /** A2DP内置前置音量 */
    private static final String A2DP_PRE_VOLUME = "A2DP_PreVolume";
    /** BLUETOOTH_TEL内置前置音量 */
    private static final String BLUETOOTH_TEL_PRE_VOLUME = "BLUETOOTH_TEL_PreVolume";
    /** 电话通道的内置前置音量 */
    private static final String PHONE_TELE_PRE_VOLUME = "PHONE_TEL_PreVolume";

    /** AK7601音量曲线 */
    private static final String AK7601_VOLUME_CURVE = "AK7601VolumeCurve";
    /** BD37534音量曲线 */
    private static final String BD37534_VOLUME_CURVE = "BD37534VolumeCurve";
    /** TEF6638音量曲线 */
    private static final String TEF6638_VOLUME_CURVE = "TEF6638VolumeCurve";

    // AC8音频参数 start
    /** AC8声道扩展类型 */
    private static final String UP_MIX_TYPE = "UpMixType";
    /** AC8喇叭布局类型 */
    private static final String SPEAKER_LAYOUT_MIN = "Speaker_Layout_MIN";
    private static final String SPEAKER_LAYOUT_LR = "Speaker_Layout_LR";
    private static final String SPEAKER_LAYOUT_MONO = "Speaker_Layout_MONO";
    private static final String SPEAKER_LAYOUT_STEREO = "Speaker_Layout_STEREO";
    private static final String SPEAKER_LAYOUT_LRC = "Speaker_Layout_LRC";
    private static final String SPEAKER_LAYOUT_LRS = "Speaker_Layout_LRS";
    private static final String SPEAKER_LAYOUT_LRCS = "Speaker_Layout_LRCS";
    private static final String SPEAKER_LAYOUT_LRLSRS = "Speaker_Layout_LRLSRS";
    private static final String SPEAKER_LAYOUT_LRCLSRS = "Speaker_Layout_LRCLSRS";
    private static final String SPEAKER_LAYOUT_SUBWOOFER = "Speaker_Layout_Subwoofer";
    private static final String SPEAKER_LAYOUT_MAX = "Speaker_Layout_Max";
    /** 设置后排两声道的音量值 */
    private static final String REAR_VOLUME = "RearVolume";
    /** 音乐、av、tv等音源情况下硬件最大音量 */
    private static final String AUDIO_HW_VOL_NORMAL = "Audio_HW_Vol_Normal";
    /** Radio音源情况下硬件最大音量 */
    private static final String AUDIO_HW_VOL_RADIO = "Audio_HW_Vol_Radio";
    // AC8音频参数 end

    // 音效配置 start
    /** 音效配置 */
    private static final String SECTION_AUDIO_EFFECT = "AudioEffect";
    // 音效配置 end
    // 音频 end

    // 状态栏设置
    private static final String SECTION_SYSTEM_UI = "SystemUI"; // 状态栏
    private static final String STATUS_BAR_COLOR  = "StatusBarColor"; // 设置状态栏颜色

    // 倒车后视 start
    /** 倒车轨迹配置的组 */
    private static final String SECTION_CAR_CCD = "CarCcd";
    /** 倒车轨迹X轴方向偏移量 */
    private static final String CARCCD_TRACKOFFSET_X = "TRACK_OFFSET_X";
    /** 倒车轨迹Y轴方向偏移量 */
    private static final String CARCCD_TRACKOFFSET_Y = "TRACK_OFFSET_Y";
    /** 倒车轨迹X轴方向偏移量默认值 */
    private static final int    CARCCD_TRACKOFFSET_DEFAULT = 0;
    // 倒车后视 end

    /** AC8 ATCCamera矩形剪裁设置 */
    private static final String SECTION_CAMERA_RECT = "CameraRect";
    /** Rect Left */
    private static final String CAMERA_RECT_LEFT = "RECT_LEFT";
    /** Rect Top */
    private static final String CAMERA_RECT_TOP = "RECT_TOP";
    /** 默认偏移量 */
    private static final int    CAMERA_RECT_DEFAULT = 0;

    // DAB
    private static final String SECTION_DAB = "DAB";
    private static final String DAB_CHIP = "DABChip"; // DAB芯片

    // TV
    /** TV模块配置的组 */
    private static final String SECTION_TV = "TV";
    /** TV模块 {@link IVITV.Module} */
    private static final String TV_MODULE = "Module";

    /** 手刹限制的应用列表配置 */
    private static final String SECTION_HANDBRAKE_LIMIT = "HandBrakeLimit";

    /** 面板按键灯 */
    private static final String SECTION_PANEL_LIGHT = "PanelLight";
    /** 面板按键灯时候可控制 */
    private static final String PANEL_LIGHT_CONTROL = "Control";

    /** 行车秘书 */
    private static final String SECTION_CAR_ASSISTANT = "CarAssistant";
    private static final String USABLE_SCREEN_HEIGHT = "UsableScreenHeight";

    /** 平台类配置 */
    private static final String SECTION_PLATFORM = "Platform";
    /** mcu串口波特率 */
    private static final String MCU_SERIAL_BAUD_RATE = "MCU_SERIAL_BAUD_RATE";
    /** 仪表通信的方式 */
    private static final String CLUSTER_ID = "Cluster";
    /**日志文件的时间显示，取值为整型，单位为小时*/
    private static final String LogcatTimeLimit = "LogcatTimeLimit";
    /**PowerOff 下能否发送ACC id*/
    private static final String IS_POWER_OFF_CAN_SEND_ACCID = "IsPowerOffCanSendAccId";
    /**是否静音按键显示弹框*/
    private static final String IS_MUTE_SHOW_VOLUMEBAR = "IsMuteShowVolumeBar";

    /** 倒车音量 */
    private static final String SECTION_CCD_VOLUME = "CcdVolume";
    /** 倒车默认音量 */
    private static final String CCD_VOLUME_PERCENT_DEFAULT = "CcdVolumePercentDefault";
    /** 倒车是蓝牙铃声音量 */
    private static final String CCD_VOLUME_PERCENT_BLUETOOTHRING = "CcdVolumePercentBluetoothRing";

    /** 硬件版本 */
    private static final String SECTION_HARDWARE = "Hardware";
    /** 硬件版本号 */
    private static final String HARDWARE_VERSION = "HardwareVersion";

    /** 日期时间 */
    private static final String SECTION_DATETIME = "DateTime";
    private static final String DATETIME_SYNC_MCU = "SyncMcu";

    /** TBox 相关配置*/
    private static final String SECTION_TBOX = "TBox";
    /** TBox 端口 配置 */
    private static final String TBOX_PORT_CONFIG = "TBoxPort";
    /** TBox 端口 默认配置 */
    private static final int DEF_TBOX_PORT = 20000;
    /** TBox HOST 配置 */
    private static final String TBOX_HOST_CONFIG = "TBoxHost";
    /** TBox HOST 默认配置 */
    private static final String DEF_TBOX_HOST = "192.168.100.1";

    private static IniFileUtil mIniFileUtil = null; // INI文件工具类对象
    // 动态轨迹线 start
    /** 动态轨迹线 */
    private static final String CAR_CCD_LINE                     = "car_ccd_line";
    /** 车型英文名 */
    private static final String CAR_EN_NAME                      = "car_en_name";
    /** 车型中文名 */
    private static final String CAR_CN_NAME                      = "car_cn_name";
    /** 摄像头变形量 */
    private static final String CAMERA_R                         = "camera_r";
    /** 轨迹长度 */
    private static final String TRACK_MAX_LEN                    = "track_max_len";
    /** 获取红线和车尾距离 */
    private static final String TRACK_MIN_LEN                    = "track_min_len";
    /** 车宽 */
    private static final String CAR_WIDTH                        = "car_width";
    /** 车轴长 */
    private static final String CAR_WHEELBASE                    = "car_wheelbase";
    /** 前轮距 */
    private static final String CAR_FORNT_TRACK_WIDTH            = "car_front_track_width";
    /** 后轮距 */
    private static final String CAR_REAR_TRACK_WIDTH             = "car_rear_track_width";
    /** 后轮长 */
    private static final String CAR_REAR_LEN                     = "car_rear_len";
    /** 后轮宽 */
    private static final String CAR_REAR_TYRE_WIDTH              = "car_rear_tyre_width";
    /** 方向盘角度最大值 */
    private static final String CAR_STEERING_WHEEL_ANGLE_MAX     = "car_steering_wheel_angle_max";
    /** 获取前轮转向内轮的最大角度 */
    private static final String CAR_WHEEL_ANGEL_MAX              = "car_wheel_angel_max";
    /** 显示样式 */
    private static final String DISP_STYLE                       = "disp_style";
    /** I点X值 */
    private static final String PTS00                            = "pts00";
    /** I点Y值 */
    private static final String PTS01                            = "pts01";
    /** J点X值 */
    private static final String PTS10                            = "pts10";
    /** J点Y值 */
    private static final String PTS11                            = "pts11";
    /** K点X值 */
    private static final String PTS20                            = "pts20";
    /** K点J值 */
    private static final String PTS21                            = "pts21";
    /** H点X值 */
    private static final String PTS30                            = "pts30";
    /** H点J值 */
    private static final String PTS31                            = "pts31";
    // 动态轨迹线 end

    // 屏保 start
    private static final String SECTION_SCREEN_PROTECTION = "ScreenProtection";
    /** 屏幕点击节点路径 */
    private static final String SCREEN_PROTECTION_PATH = "path";
    /** 屏幕点击节点名字 */
    private static final String SCREEN_PROTECTION_NAME = "name";
    /** 屏保默认时间 */
    private static final String SCREEN_PROTECTION_TIME = "time";
    // 屏保 end


    // 空调功能配置 start
    /** 空调功能配置定义组名 */
    private static final String SECTION_CLIMATE = "Climate";
    /** 空调针对锁屏的操作 */
    private static final String CLIMATE_LOCKS_SCREEN_ID = "climate_locks_screen_id";
    /** 空调弹框消失时间 */
    private static final String CLIMATE_MISS_TIME = "climate_miss_time";
    /** 空调相同数据是否过滤 */
    private static final String CLIMATE_SAME_VALUE = "climate_same_value";
    // 空调 end

    /**
     * 区域变化
     */
    public static class AreaChange {
        public int mId = AudioParam.Id.NONE;
        public int mDst = 0;
        public int mSrcMin = 0;
        public int mSrcMax = 0;

        /**
         * 解析为{@link AreaChange}对象
         * @param string VOLUME_MASTER#12(0~30)
         * @return
         */
        public static AreaChange obtain(String string) {
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            try {
                String[] array = string.split("&");
                final int length = array.length;
                if (length > 1) {
                    final int id = AudioParam.Id.getId(array[0]);
                    if (AudioParam.Id.NONE != id) {
                        final String item = array[1];
                        if (!TextUtils.isEmpty(item)) {
                            final int dstIndex = item.indexOf("(");
                            final int srcMinIndex = item.indexOf("~");
                            final int srcMaxIndex = item.indexOf(")");
                            if (dstIndex > 0 && srcMinIndex > dstIndex && srcMaxIndex > srcMinIndex) {
                                AreaChange areaChange = new AreaChange();
                                areaChange.mId = id;
                                areaChange.mDst = Integer.parseInt(item.substring(0, dstIndex));
                                areaChange.mSrcMin = Integer.parseInt(item.substring(dstIndex + 1, srcMinIndex));
                                areaChange.mSrcMax = Integer.parseInt(item.substring(srcMinIndex + 1, srcMaxIndex));
                                return areaChange;
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解析为{@link AreaChange}列表对象
         * @param string VOLUME_MASTER#6(0~5),VOLUME_MASTER#17(17~30)
         * @return
         */
        public static List<AreaChange> parse(String string) {
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            try {
                String[] array = string.split(",");
                final int length = array.length;
                if (length > 0) {
                    List<AreaChange> areaChanges = new ArrayList<>();
                    for (int i = 0;i < length;i++) {
                        AreaChange areaChange = obtain(array[i]);
                        if (null != areaChange) {
                            areaChanges.add(areaChange);
                        }
                    }
                    return areaChanges;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class MemoryPackageName {
        public String mPackageName;
        public Map<String, String> mIntentExtras;

        /**
         * 创建{@link MemoryPackageName}对象
         * @param string com.roadrover.radio_v2/freq:87500
         * @return
         */
        public static MemoryPackageName obtain(String string) {
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            try {
                String[] array = string.split("/");
                final int length = array.length;
                if (length > 0) {
                    MemoryPackageName memoryPackageName = new MemoryPackageName();
                    memoryPackageName.mPackageName = array[0];
                    if (length > 1) {
                        Map<String, String> extras = new HashMap<>();
                        for (int i = 1;i < length;i++) {
                            final String extra = array[i];
                            if (!TextUtils.isEmpty(extra)) {
                                final int index = extra.indexOf(":");
                                extras.put(extra.substring(0, index), extra.substring(index + 1, extra.length()));
                            }
                        }
                        memoryPackageName.mIntentExtras = extras;
                    }
                    return memoryPackageName;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 创建{@link MemoryPackageName}对象
         * @param appsMap
         * @param packageName 开机记忆启动的包名
         * @return
         */
        public static MemoryPackageName obtain(Map<String, String> appsMap, String packageName) {
            if (appsMap.isEmpty() || TextUtils.isEmpty(packageName)) {
                return null;
            }
            try {
                for (Map.Entry<String, String> entry : appsMap.entrySet()) {
                    Logcat.d("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    if (TextUtils.equals(packageName, entry.getKey())) {
                        MemoryPackageName memoryPackageName = new MemoryPackageName();
                        memoryPackageName.mPackageName = entry.getValue();
                        return memoryPackageName;
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

	/**
     * 获取车辆英文名
     * @return
     */
    public static String getCarEnName() {
        return getString(CAR_CCD_LINE, CAR_EN_NAME, IVICar.CarEnName.GOLF_7);
    }

	/**
     * 获取车辆中文名
     * @return
     */
    public static String getCarCnName() {
        return getString(CAR_CCD_LINE, CAR_CN_NAME, IVICar.CarCnName.GOLF_7);
    }

	/**
     * 获取变形度
     * @return
     */
    public static int getCamera_R() {
        return getInteger(CAR_CCD_LINE, CAMERA_R, 96);
    }

	/**
     * 获取轨迹长度
     * @return
     */
    public static int getTrack_Max_Len() {
        return getInteger(CAR_CCD_LINE, TRACK_MAX_LEN, 4);
    }

	/**
     * 获取红线和车尾距离
     * @return
     */
    public static int getTrack_Min_Len() {
        return getInteger(CAR_CCD_LINE, TRACK_MIN_LEN, 40);
    }

	/**
     * 获取车宽
     * @return
     */
    public static int getCar_Width() {
        return getInteger(CAR_CCD_LINE, CAR_WIDTH, 1799);
    }

	/**
     * 获取车长
     * @return
     */
    public static int getCar_WheelBase() {
        return getInteger(CAR_CCD_LINE, CAR_WHEELBASE, 2637);
    }

	/**
     * 获取前轮距
     * @return
     */
    public static int getCar_Front_Track_Width() {
        return getInteger(CAR_CCD_LINE, CAR_FORNT_TRACK_WIDTH, 1549);
    }

	/**
     * 获取后轮距
     * @return
     */
    public static int getCar_Rear_Track_Width() {
        return getInteger(CAR_CCD_LINE, CAR_REAR_TRACK_WIDTH, 1520);
    }

	/**
     * 获取后轮轮胎长度
     * @return
     */
    public static int getCar_Rear_Len() {
        return getInteger(CAR_CCD_LINE, CAR_REAR_LEN, 763);
    }

	/**
     * 获取后轮轮胎宽度
     * @return
     */
    public static int getCar_Rear_Tyre_Width() {
        return getInteger(CAR_CCD_LINE, CAR_REAR_TYRE_WIDTH, 195);
    }

	/**
     * 获取方向盘角度最大值
     * @return
     */
    public static int getCar_Steering_Wheel_Angle_Max() {
        return getInteger(CAR_CCD_LINE, CAR_STEERING_WHEEL_ANGLE_MAX, 200);
    }

	/**
     * 获取前轮转向内轮的最大角度
     * @return
     */
    public static int getCar_Wheel_Angel_Max() {
        return getInteger(CAR_CCD_LINE, CAR_WHEEL_ANGEL_MAX, 385);
    }

	/**
     * 获取辅助线显示样式
     * @return
     */
    public static int getDisp_Style() {
        return getInteger(CAR_CCD_LINE, DISP_STYLE, 1);
    }

	/**
     * 获取四个标定点的x y值
     * @param x
     * @param y
     * @return
     */
    public static float getPts(int x, int y) {
        if (x == 0 ) {
            if (y == 0) {
                return getFloat(CAR_CCD_LINE, PTS00, (float) -139.4);
            } else {
                return getFloat(CAR_CCD_LINE, PTS01, (float) -36.5);
            }
        } else if (x == 1) {
            if (y == 0) {
                return getFloat(CAR_CCD_LINE, PTS10, (float) 145.9);
            } else {
                return getFloat(CAR_CCD_LINE, PTS11, (float) -36.5);
            }
        } else  if (x == 2) {
            if (y == 0) {
                return getFloat(CAR_CCD_LINE, PTS20, (float) 228.1);
            } else {
                return getFloat(CAR_CCD_LINE, PTS21, (float) 34.4);
            }
        } else if (x == 3) {
            if (y == 0) {
                return getFloat(CAR_CCD_LINE, PTS30, (float) -225.7);
            } else {
                return getFloat(CAR_CCD_LINE, PTS31, (float) 34.4);
            }
        }
        return getFloat(CAR_CCD_LINE, PTS00, (float) -139.4);
    }

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
     * 获取CVBS Camera的Port口
     * @param avId
     * @return AVIn的Port，用于需要Camera的Index和Port需要分开设的情况（AC8）
     */
    public static int getCameraAVInPort(int avId) {
        return getInteger(SECTION_CAMERA_AVIN_PORT, IVIAVIn.Id.getName(avId), 1);
    }

    /**
     * 获取AVIN Audio 端口配置
     * @param avId 音源类型 : AV AV2 TV TV2 Aux
     * @return
     */
    public static int getAVINAudioPort(int avId) {
        return getInteger(SECTION_AVIN_AUDIO_PORT, IVIAVIn.Id.getName(avId), 0);
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

    /**
     * 获取AVIN默认亮度
     *
     * @return
     */
    public static int getAVInVideoBrightness() {
        return getInteger(SECTION_AVIN_VIDEO_ADJUST, AVIN_DEFAULT_BRIGHTNESS, 0x00);
    }

    /**
     * 获取AVIN默认对比度
     *
     * @return
     */
    public static int getAVInVideoContrast() {
        return getInteger(SECTION_AVIN_VIDEO_ADJUST, AVIN_DEFAULT_CONTRAST, 0x64);
    }

    /**
     * 获取AVIN默认饱和度
     *
     * @return
     */
    public static int getAVInVideoSaturation() {
        return getInteger(SECTION_AVIN_VIDEO_ADJUST, AVIN_DEFAULT_SATURATION, 0x80);
    }
    /**
     * 获取AVIN检测变化次数，500ms一次
     * @param def
     * @return
     */
    public static int getAVInChangeCVBSTypeCount(int def) {
        return getInteger(SECTION_AVIN, AVIN_CHANGE_CVBS_TYPE_COUNT, def);
    }

    /**
     * 获取AVI视频是否需要延时显示
     * @return
     */
    public static boolean getAVDelayShowVideo() {
        return getBoolean(SECTION_AVIN_VIDEO_ADJUST, AVIN_DELAY_SHOW_VIDEO, false);
    }

    public static int getPowerKeyLongAction() {
        return getInteger(SECTION_POWER, POWER_LONG_ACTION, IVIKey.PowerKeyAction.NONE);
    }

    public static int getPowerKeyShortAction() {
        return getInteger(SECTION_POWER, POWER_SHORT_ACTION, IVIKey.PowerKeyAction.BKL_OFF_AND_PAUSE_MEDIA);
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

    public static boolean isNeedLocation() {
        return getBoolean(SECTION_RADIO, RADIO_NEED_LOCATION, true);
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
     * 获取FM输出增益
     * @param def
     * @return
     */
    public static float getFMOutputGain(float def) {
        return getFloat(SECTION_RADIO, RADIO_FM_OUTPUT_GAIN, def);
    }

    /**
     * 获取AM输出增益
     * @param def
     * @return
     */
    public static float getAMOutputGain(float def) {
        return getFloat(SECTION_RADIO, RADIO_AM_OUTPUT_GAIN, def);
    }

    /**
     * 是否开启检测收音机信号功能
     * @return
     */
    public static boolean isOpenDetectionRadioSignal() {
        return getBoolean(SECTION_RADIO, RADIO_NO_SIGNAL_DETECTION_MUTE, false);
    }

    /**
     * 获取从有信号到无信号的检测次数
     * @return
     */
    public static int getNoSignalDetectionCount() {
        return getInteger(SECTION_RADIO, RADIO_NO_SIGNAL_DETECTION_COUNT, 10);
    }

    /**
     * 获取从无信号到有信号的检测次数
     * @return
     */
    public static int getHasSignalDetectionCount() {
        return getInteger(SECTION_RADIO, RADIO_HAS_SIGNAL_DETECTION_COUNT, 10);
    }

    /**
     * 获取信号的最小值
     * @return
     */
    public static int getRadioSignalMin() {
        return getInteger(SECTION_RADIO, RADIO_SIGNAL_MIN, 8);
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
     * 获取最大亮度
     * @return
     */
    public static int getBrightnessMax() {
        return getInteger(SECTION_BRIGHTNESS, BRIGHTNESS_MAX, 100);
    }

    /**
     * 获取最小亮度
     * @return
     */
    public static int getBrightnessMin() {
        return getInteger(SECTION_BRIGHTNESS, BRIGHTNESS_MIN, 0);
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

        return "/dev/ttyS3";
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
     * 获取蓝牙连接超时自动重连最大次数 0表示禁止，默认为0
     * @return
     */
    public static int getBluetoothTimeoutRelink() {
        return getInteger(SECTION_BLUETOOTH, BLUETOOTH_TIMEOUT_RELINK, 0);
    }

    /**
     * 获取蓝牙默认是否自动接听
     * @return
     */
    public static boolean getBluetoothDefAutoListen() {
        return getBoolean(SECTION_BLUETOOTH, AUTO_LISTEN, false);
    }

    /**
     * 蓝牙通话界面静音按钮功能是否为静车机的麦克风输入
     */
    public static boolean getBluetoothMuteMic() {
        return getBoolean(SECTION_BLUETOOTH, BLUETOOTH_MUTE_MIC, false);
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

    /**
     * 获取蓝牙通话界面是否显示导航按钮
     * @return
     */
    public static boolean getBluetoothNaviBtnVisibility() {
        return getBoolean(SECTION_BLUETOOTH, BLUETOOTH_NAVI_START, true);
    }

    public static boolean isBluetoothContactSpecialFirst() {
        return getBoolean(SECTION_BLUETOOTH, BLUETOOTH_CONTACT_SORT, false);
    }

    /**
     * 获取本机蓝牙名称限制长度
     * @return
     */
    public static int getBluetoothNameLimitLength() {
        return getInteger(SECTION_BLUETOOTH, BLUETOOTH_NAME_LIMIT_LENGTH, 16);
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
            ret.add(IVIKey.Key.Id.VOLUME_UP);
            ret.add(IVIKey.Key.Id.VOLUME_DOWN);
            ret.add(IVIKey.Key.Id.MUTE);
            ret.add(IVIKey.Key.Id.PLAY_PAUSE);
            ret.add(IVIKey.Key.Id.PREV);
            ret.add(IVIKey.Key.Id.NEXT);
            ret.add(IVIKey.Key.Id.HANG_UP);
            ret.add(IVIKey.Key.Id.PREV_ANSWER);
            ret.add(IVIKey.Key.Id.NEXT_HANGUP);
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
     * 在关屏状态下，仅仅只是开背光，不执行其功能
     * @return
     */
    public static boolean isOnlyOpenScreen() {
        return getBoolean(SECTION_SCREEN_OFF, KEY_ONLY_OPEN_SCREEN, false);
    }

    /**
     * 获取倒车轨迹X方向偏移量
     * @return
     */
    public static int getCarCCDTrackOffsetByX(){
        return getInteger(SECTION_CAR_CCD, CARCCD_TRACKOFFSET_X, CARCCD_TRACKOFFSET_DEFAULT);
    }

    /**
     * 获取倒车轨迹Y方向偏移量
     * @return
     */
    public static int getCarCCDTrackOffsetByY(){
        return getInteger(SECTION_CAR_CCD, CARCCD_TRACKOFFSET_Y, CARCCD_TRACKOFFSET_DEFAULT);
    }

    /**
     * 获取摄像头Left偏移
     * @return
     */
    public static int getCameraRectLeft(){
        return getInteger(SECTION_CAMERA_RECT, CAMERA_RECT_LEFT, CAMERA_RECT_DEFAULT);
    }

    /**
     * 获取摄像头Top偏移
     * @return
     */
    public static int getCameraRectTop(){
        return getInteger(SECTION_CAMERA_RECT, CAMERA_RECT_TOP, CAMERA_RECT_DEFAULT);
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
                final int id = IVIKey.Key.getId(array[i]);
                if (id != IVIKey.Key.Id.NONE) {
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

            case IVIAudio.Channel.PC_MUSIC:
                return getFloat(SECTION_AUDIO, PC_MUSIC_PRE_VOLUME, defaultValue);

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

            case IVIAudio.Channel.BLUETOOTH_TEL:
                return getFloat(SECTION_AUDIO, BLUETOOTH_TEL_PRE_VOLUME, defaultValue);

            case IVIAudio.Channel.PHONE:
                return getFloat(SECTION_AUDIO, PHONE_TELE_PRE_VOLUME, defaultValue);

            default:
                Logcat.w("Not realization channel: " + IVIAudio.Channel.getName(channel));
                return defaultValue;
        }
    }

    /**
     * 获取AK7601音量曲线
     * @return
     */
    public static float[] getAK7601VolumeCurve() {
        return getVolumeCurve(AK7601_VOLUME_CURVE);
    }

    /**
     * 获取TEF6638音量曲线
     * @return
     */
    public static float[] getTEF6638VolumeCurve() {
        return getVolumeCurve(TEF6638_VOLUME_CURVE);
    }

    /**
     * 获取BD37534音量曲线
     * @return
     */
    public static float[] getBD37534VolumeCurve() {
        return getVolumeCurve(BD37534_VOLUME_CURVE);
    }

    /**
     * 获取音频区音量曲线，音频曲线中配置中允许有xxx(音量)，以便于说明
     * @param name {@link #AK7601_VOLUME_CURVE} 或者 {@link #BD37534_VOLUME_CURVE}
     * @return
     */
    private static float[] getVolumeCurve(@NonNull String name) {
        float[] ret = null;
        String[] array = getCutStringArray(SECTION_AUDIO, name, "(");
        if (null != array) {
            final int length = array.length;
            ret = new float[length];
            for (int i = 0;i < length;i++) {
                try {
                    ret[i] = Float.parseFloat(array[i]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 获取AC8声道扩展类型
     * @return
     */
    public static int getUpMixType(int defaultType) {
        return getInteger(SECTION_AUDIO, UP_MIX_TYPE, defaultType);
    }

    /**
     * 获取AC8喇叭布局类型
     * @return
     */
    public static int getSpeakerLayoutType() {
        return getInteger(SECTION_AUDIO, SPEAKER_LAYOUT_LRLSRS, 0) | getInteger(SECTION_AUDIO, SPEAKER_LAYOUT_SUBWOOFER, 0);
    }

    /**
     * 获取后排声音大小
     * @param defaultVol
     * @return
     */
    public static int getReadVolume(int defaultVol) {
        return getInteger(SECTION_AUDIO, REAR_VOLUME, defaultVol);
    }

    /**
     * 根据音源获取不同配置设置硬件声音大小
     * @param channel
     * @param defaultVol
     * @return
     */
    public static int getAudioHWVolume(int channel, int defaultVol) {
        Logcat.d(IVIAudio.Channel.getName(channel));
        switch (channel) {
            case IVIAudio.Channel.PC:
            case IVIAudio.Channel.AV:
            case IVIAudio.Channel.AV2:
            case IVIAudio.Channel.TV:
            case IVIAudio.Channel.TV2:
            case IVIAudio.Channel.AUX:
            case IVIAudio.Channel.AUX2:
                return getInteger(SECTION_AUDIO, AUDIO_HW_VOL_NORMAL, defaultVol);

            case IVIAudio.Channel.RADIO:
            case IVIAudio.Channel.RADIO_TA:
                return getInteger(SECTION_AUDIO, AUDIO_HW_VOL_RADIO, defaultVol);

            default:
                return getInteger(SECTION_AUDIO, AUDIO_HW_VOL_NORMAL, defaultVol);
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
     * 获取Launcher需要忽略的APP包名列表
     * @return
     */
    public static List<String> getIgnoreLaunchApps() {
        List<String> apps = new ArrayList<>();
        if (checkRead() && mIniFileUtil != null) {
            Integer count = getInteger(SECTION_IGNORE_APP, IGNORE_COUNT);
            Logcat.d("count:" + count);
            if (count != null) {
                for (int i = 0; i < count; ++i) {
                    String app = getString(SECTION_IGNORE_APP, IGNORE_APP + i);
                    if (!TextUtils.isEmpty(app)) {
                        apps.add(app);
                    }
                }
            }
        }
        return apps;
    }

    /**
     * mode 按键长按次数
     * @return
     */
    public static int getModeKeyLongCount() {
        return getInteger(SECTION_MODE_CONTROL, MODE_LONG_MAX_COUNT, 0);
    }

    /**
     * 获取mode按键长按的动作
     * @return
     */
    public static String getModeKeyLongAction() {
        return getString(SECTION_MODE_CONTROL, MODE_LONG_ACTION);
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
     * 获取开机记忆的应用列表
     * @return
     */
    public static Map<String, String> getBootMemoryAppMap() {
        Map<String, String> appsMap = new HashMap<>();
        if (checkRead() && mIniFileUtil != null) {
            Integer count = getInteger(SECTION_BOOT_MEMORY, MEMORY_COUNT);
            Logcat.d("count:" + count);
            if (count != null) {
                for (int i = 0; i < count; ++i) {
                    String packageName = getString(SECTION_BOOT_MEMORY, MEMORY_APP + i);
                    if (!TextUtils.isEmpty(packageName)) {
                            String[] array = packageName.split("->");
                            int length = array.length;
                            Logcat.d("length : " +length);
                            if (2 == length) {       //限制为A->B最多2个包名
                                if(!TextUtils.isEmpty(array[0]) && !TextUtils.isEmpty(array[1])) {
                                    appsMap.put(array[0],array[1]);
                                }
                            } else if (1 == length){ //会有A->情况,
                                if(!TextUtils.isEmpty(array[0])) {
                                    appsMap.put(array[0],"");
                                }
                            }

                    }
                }
            }
        }
        return appsMap;
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
     * 获取DAB芯片
     * @return
     */
    public static int getDABChip() {
        return getInteger(SECTION_DAB, DAB_CHIP, IVIDAB.DABChip.NONE);
    }

    /**
     * 获取媒体信息改变最小时间
     * @return
     */
    public static int getMediaChangeMinTime(int def) {
        return getInteger(SECTION_MEDIA, MEDIA_CHANGE_MIN_TIME, def);
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
     * @param cut
     * @return
     */
    private static String[] getCutStringArray(String section, String name, String cut) {
        String[] strings = getStringArray(section, name);
        if (null != strings) {
            final int length = strings.length;
            for (int i = 0;i < length;i++) {
                final String string = strings[i];
                if (!TextUtils.isEmpty(string)) {
                    strings[i] = string.substring(0, string.indexOf(cut));
                }
            }
        }
        return strings;
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
                e.printStackTrace();
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

    /**
     * 获取MCU串口波特率，默认为 115200
     * @return
     */
    public static int getMcuSerialBaudRate() {
        return getInteger(SECTION_PLATFORM, MCU_SERIAL_BAUD_RATE, 115200);
    }

    /**
     * 获取按键防抖的时间
     * @param id 按键ID，见{@link com.roadrover.sdk.system.IVIKey.Key.Id}
     * @return
     */
    public static int getKeyAntiShakeTime(int id) {
        int ret = 0;
        final String name = IVIKey.Key.getName(id);
        if (!TextUtils.isEmpty(name)) {
            ret = getInteger(SECTION_KEY_ANTI_SHAKE, name, ret);
        }
        return ret;
    }

    /**
     * 获取仪表通信的方式
     * @return
     */
    public static int getClusterId() {
        return getInteger(SECTION_PLATFORM, CLUSTER_ID, IVICluster.ID.UNKNOWN);
    }

    /**获取日志文件保持期限,单位为小时*/
    public static int getLogcatTimeLimit() {
        return getInteger(SECTION_PLATFORM, LogcatTimeLimit, 0);
    }

    /**
     * 获取倒车时降低音量默认值
     * @return
     */
    public static int getCcdVolumePercentDefault() {
        return getInteger(SECTION_CCD_VOLUME, CCD_VOLUME_PERCENT_DEFAULT, IVIAudio.VOLUME_CCD_DUCK_PERCENT);
    }

    /**
     * 获取倒车时降低蓝牙铃声音量默认值
     * @return
     */
    public static int getCcdVolumePercentBluetoothRing() {
        return getInteger(SECTION_CCD_VOLUME, CCD_VOLUME_PERCENT_BLUETOOTHRING, IVIAudio.VOLUME_CCD_DUCK_PERCENT);
    }

    /**
     * 获取是否支持动态写入硬件版本号
     */
    public static boolean isSupportWriteHardware() {
        return getBoolean(SECTION_HARDWARE, HARDWARE_VERSION, false);
    }

    /**
     * 获取第一次开机音频参数区域变化配置
     * @return 武汉雷诺项目恢复音量12，开机自动播放收音FM87.5
     */
    public static List<AreaChange> getVolumeBatteryOnAreaChanges() {
        return AreaChange.parse(getString(SECTION_VOLUME, VOLUME_BATTERY_ON_AREA_CHANGE));
    }

    /**
     * 获取记开机音频参数区域变化配置
     * @return 武汉雷诺项目在开机前音量0~6恢复到6，大于17恢复到17，6~17保持不变，开机自动播放上次关机的记忆音源，USB支持断点播放
     */
    public static List<AreaChange> getVolumePowerOnAreaChanges() {
        return AreaChange.parse(getString(SECTION_VOLUME, VOLUME_POWER_ON_AREA_CHANGE));
    }

    /**
     * 获取唤醒开机音频参数区域变化配置
     * @return
     */
    public static List<AreaChange> getVolumeWakeUpAreaChanges() {
        return AreaChange.parse(getString(SECTION_VOLUME, VOLUME_WAKE_UP_AREA_CHANGE));
    }

    /**
     * 获取屏幕开机音频参数区域变化配置
     * @return
     */
    public static List<AreaChange> getVolumeScreenOnAreaChanges(int action) {
        final String string = getString(SECTION_VOLUME, VOLUME_SCREEN_ON_AREA_CHANGE);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            String[] array = string.split("/");
            if (array.length > 1) {
                if (action == Integer.parseInt(array[0])) {
                    return AreaChange.parse(array[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取记忆的包名信息
     * @return
     */
    public static MemoryPackageName getMemoryPackageName() {
        return MemoryPackageName.obtain(getString(SECTION_MEMORY, MEMORY_PACKAGE_NAME));
    }

    /**
     * 获取记忆启动的包名信息
     * @return
     */
    public static MemoryPackageName getMemoryPackageName(String packageName) {
        return MemoryPackageName.obtain(getBootMemoryAppMap(), packageName);
    }

    /**
     * 是否使用MCU同步时间
     * @return
     */
    public static boolean isSyncMcuDateTime() {
        return getBoolean(SECTION_DATETIME, DATETIME_SYNC_MCU, false);
    }

    /**
     * 倒车摄像头是否长供电
     * @return 是返回true，默认返回false
     */
    public static boolean isCcdLongSupplyPower() {
        return getBoolean(SECTION_AVIN, CCD_LONG_SUPPLY_POWER, false);
    }

    /**
     * 车内摄像头是否长供电
     * @return 是返回true，默认返回false
     */
    public static boolean isInnerCameraLongSupplyPower() {
        return getBoolean(SECTION_AVIN, INNER_CAMERA_LONG_SUPPLY_POWER, false);
    }

    /**
     * 行李箱摄像头是否长供电
     * @return 是返回true，默认返回false
     */
    public static boolean isLuggageDoorLongSupplyPower() {
        return getBoolean(SECTION_AVIN, LUGGAGE_DOOR_LONG_SUPPLY_POWER, false);
    }

    /**
     * 中门摄像头是否长供电
     * @return 是返回true，默认返回false
     */
    public static boolean isMidDoorCameraSupplyPower() {
        return getBoolean(SECTION_AVIN, MID_DOOR_CAMERA_SUPPLY_POWER, false);
    }

    /**
     * 锁屏状态下acc on 是否支持开屏
     * @return 是返回true，默认返回false
     */
    public static boolean isAccOpenScreenOnLockScreen() {
        return getBoolean(SECTION_ACC_OPEN_SCREEN, IS_ACC_OPEN_SCREEN, false);
    }

    /**
     * 每秒钟发送一次系统时间给下位机
     * @return 是返回true，默认返回false
     */
    public static boolean isSendSystemTimeEverySecond() {
        return getBoolean(SECTION_SET_SYSTEM_TIME, SEND_TIME_EVERY_SECOND, false);
    }

    /**
     * 下位机是否支持半小时机制
     */
    public static boolean isSupportHalfHourMechanism() {
        return getBoolean(SECTION_HALF_HOUR_MECHANISM, ENALE_HALF_HOUR_MECHANISM, false);
    }
    /**
     * 获取屏幕保护驱动节点路径
     * @return
     */
    public static String getScreenProtectionPath() {
        return getString(SECTION_SCREEN_PROTECTION, SCREEN_PROTECTION_PATH, "");
    }

    /**
     * 获取屏幕保护驱动节点名字
     * @return
     */
    public static String getScreenProtectionName() {
        return getString(SECTION_SCREEN_PROTECTION, SCREEN_PROTECTION_NAME, "");
    }

    /**
     * 获取屏幕保护时间
     * @return
     */
    public static int getScreenProtectionTime() {
        return getInteger(SECTION_SCREEN_PROTECTION, SCREEN_PROTECTION_TIME, 0);
    }

    /**
     * 是否需要监听所有文件夹变化
     * @return
     */
    public static boolean isNeedFileObserver() {
        return getBoolean(SECTION_FILE_OBSERVER, NEED_OBSERVER, true);
    }


    /**
     * 获取默认设置的主音量值
     */
    public static int getDefaultVolumeMaster(int defaultValue) {
        return getInteger(SECTION_VOLUME, DEFAULT_VOLUME_MASTER, defaultValue);
    }

    /**
     * 获取默认设置的蓝牙通话音量值
     */
    public static int getDefaultVolumeBluetooth(int defaultValue) {
        return getInteger(SECTION_VOLUME, DEFAULT_VOLUME_BLUETOOTH, defaultValue);
    }

    /**
     * 获取默认设置的蓝牙铃声音量值
     */
    public static int getDefaultVolumeBluetoothRing(int defaultValue) {
        return getInteger(SECTION_VOLUME, DEFAULT_VOLUME_BLUETOOTH_RING, defaultValue);
    }

    /**
     * 配置AC8的Av输出功能
     * @return 是返回true， 默认返回true
     */
    public static boolean isEnableAVOut() {
        return getBoolean(SECTION_AVIN, ENABLE_AVOUT, true);
    }

    /**
     * 倒车雷达声音
     * @return 是返回true， 默认返回false
     */
    public static boolean getCcdRadar() {
        return getBoolean(SECTION_CCD_RADAR, ENABLE_CCD_RADAR, false);
    }

    /**
     * 获取根据音频焦点调节音量
     * @return 是返回true， 默认返回false
     */
    public static boolean getEnableAuduoFocus() {
        return getBoolean(SECTION_AUDIO_FOCUS, ENABLE_DYNAMIC_AUDIO_FOCUS, false);
    }

     /* 获取面板按键灯默认状态
     * @return
     */
    public static int getPanelLightPowerStatus() {
        int status = PanelLightPowerStatus.PANEL_LIGHT_NORMAL;
        if (checkRead() && mIniFileUtil != null) {
            status = getInteger(PANEL_LIGHT_POWER, PANEL_LIGHT_POWER_STATUS, PanelLightPowerStatus.PANEL_LIGHT_NORMAL);
            Logcat.d("status:" + status);
        }
        return status;
    }

    /**
     * 获取固件版本号
     * @return
     */
    public static String getFirmwareVersion() {
        return getString(SECTION_SYSTEM, FIRMWARE_VERSION, "");
    }

    /**
     * 获取下位机升级文件头部编号
     * @return
     */
    public static String[] getMcuFileHeadCodes() {
        return getStringArray(SECTION_SYSTEM, MCU_FILE_HEAD_CODE);
    }

	/**
     * 获取倒车时多媒体的处理方式
     * @return
     */
    public static int getMediaStatusCcdOn() {
        int status = MediaStatusOnCcd.MEDIA_STATUS_ON_CCD_VOLUME_PERCENT;
        if (checkRead() && mIniFileUtil != null) {
            status = getInteger(CCD_MEDIA_STATUS, MEDIA_STATUS_ON_CCD, MediaStatusOnCcd.MEDIA_STATUS_ON_CCD_VOLUME_PERCENT);
            Logcat.d("status:" + status);
        }
        return status;
    }

    /**
     * 获取空调弹框消失时间
     * @return
     */
    public static int getClimateMissTime() {
        return getInteger(SECTION_CLIMATE, CLIMATE_MISS_TIME, 0);
    }

    /**
     * 获取空调对锁屏的操作
     * @return
     */
    public static int getClimateLockScreenId() {
        return getInteger(SECTION_CLIMATE, CLIMATE_LOCKS_SCREEN_ID, Climate.ClimateLockScreenId.NONE);
    }

    /**
     * 获取空调相同数据是否过滤
     * @return
     */
    public static boolean getClimateSameValue() {
        return getBoolean(SECTION_CLIMATE, CLIMATE_SAME_VALUE, true);
    }

    /**
     * 获取PowerOff 下能否发送ACC id
     * @return
     */
    public static boolean isPowerOffCanSendAccId() {
        return getBoolean(SECTION_PLATFORM, IS_POWER_OFF_CAN_SEND_ACCID, false);
    }

    /**
     * 是否静音按键显示弹框
     * @return
     */
    public static boolean isMuteShowVolumeBar() {
        return getBoolean(SECTION_PLATFORM, IS_MUTE_SHOW_VOLUMEBAR, false);
    }

    /**
     * 获取TBox 端口配置
     * @return 默认Host配置"192.168.100.1"
     */
    public static int getTBoxPort() {
        return getInteger(SECTION_TBOX, TBOX_PORT_CONFIG, DEF_TBOX_PORT);
    }

    /**
     * 获取TBox Host配置
     * @return 默认Host配置"192.168.100.1"
     */
    public static String getTBoxHost() {
        return getString(SECTION_TBOX, TBOX_HOST_CONFIG, DEF_TBOX_HOST);
    }

    /**
     * 获取音效配置
     * @return 返回eq等音效配置结果
     */
    public static Map<Integer, int[]> getAudioEffect() {
        Map<Integer, int[]> results = new HashMap<>();
        boolean isHasRet = false;
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(SECTION_AUDIO_EFFECT);
            if (section != null) {
                Map<String, Object> maps = section.getValues();
                Set<String> keys = maps.keySet();
                if (keys != null && keys.size() != 0) {
                    try {
                        for (String key : keys) {
                            String values = (String) maps.get(key);
                            String[] effects = values.split(",");
                            if (!ListUtils.isEmpty(effects)) {
                                int[] intEffects = new int[effects.length];
                                for (int i = 0; i < effects.length; ++i) {
                                    intEffects[i] = Integer.valueOf(effects[i]);
                                }
                                results.put(Integer.valueOf(key), intEffects);
                            }
                        }
                        isHasRet = true;
                    } catch (Exception e) {
                        // 异常，没有数据
                    }
                }
            }
        }

        if (!isHasRet) { // 默认数据
            results.clear();
            results.put(AudioParam.EqMode.DEFAULT,  new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            results.put(AudioParam.EqMode.USER,     new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            results.put(AudioParam.EqMode.CLASSICAL,new int[]{5, 5, 3, 0, 0, 0, 0, 2, 2, 2});
            results.put(AudioParam.EqMode.POP,      new int[]{3, 1, -2, -3, -4, -3, -2, 0, 1, 2});
            results.put(AudioParam.EqMode.DANCE,    new int[]{3, 5, 1, -1, -1, 0, 0, 4, 4, 4});
            results.put(AudioParam.EqMode.ROCK,     new int[]{1, 2, 3, -1, -1, 0, 0, 4, 4, 4});
            results.put(AudioParam.EqMode.LIVE,     new int[]{0, 0, 3, 4, 2, 5, 2, 0, 0, 0});
            results.put(AudioParam.EqMode.SOFT,     new int[]{0, 2, 1, 0, 1, 0, 0, -2, -3, -5});
        }
        return results;
    }
}
