package com.roadrover.sdk.system;

import android.os.RecoverySystem;
import android.view.View;

import com.roadrover.sdk.utils.LogNameUtil;

/**
 * 系统服务定义类
 */

public class IVISystem {

    /**
     * 屏幕操作的 Event 类
     */
    public static class EventScreenOperate {

        /**
         * 打开屏幕操作
         */
        public static final int OPEN_TYPE  = 0;

        /**
         * 关闭屏幕操作
         */
        public static final int CLOSE_TYPE = 1;

        /**
         * 应用调用的关闭，点屏幕可以解锁
         */
        public static final int APP_CLOSE_SCREEN = 0;

        /**
         * 系统调用的关闭，点屏幕不可以解锁
         */
        public static final int SYS_CLOSE_SCREEN = 1;

        /**
         * 应用调用的，进入时钟界面，点屏幕不可以解锁
         */
        public static final int APP_SCREEN_CLOCK = 2;

        public int mType;
        public int mFrom; // 调用源
        public EventScreenOperate(int type) {
            mType = type;
        }

        public EventScreenOperate(int type, int from) {
            mType = type;
            mFrom = from;
        }

        /**
         * 打印类型的名字
         * @param type {@link EventScreenOperate}
         * @return
         */
        public static String getTypeName(int type) {
            switch (type) {
                case OPEN_TYPE:
                    return "OPEN_TYPE";

                case CLOSE_TYPE:
                    return "CLOSE_TYPE";

            }
            return "unknown type:" + type;
        }

        /**
         * 打印来源的名字
         * @param from {@link EventScreenOperate}
         * @return
         */
        public static String getFromName(int from) {
            switch (from) {
                case APP_CLOSE_SCREEN:
                    return "APP_CLOSE_SCREEN";

                case SYS_CLOSE_SCREEN:
                    return "SYS_CLOSE_SCREEN";

                case APP_SCREEN_CLOCK:
                    return "APP_SCREEN_CLOCK";
            }
            return "unknown form:" + from;
        }
    }

    /**
     * 屏幕亮度发生改变
     */
    public static class EventScreenBrightnessChange {
        public int mId;
        public int mBrightness;

        /**
         * 构造函数
         * @param id {@link ScreenBrightnessId}
         * @param brightness 亮度值
         */
        public EventScreenBrightnessChange(int id, int brightness) {
            mId = id;
            mBrightness = brightness;
        }
    }

    /**
     * 当前屏幕亮度发生改变 </br>
     * 例：当前是夜间模式，夜间模式的亮度值发生了改变 </br>
     */
    public static class EventCurrentScreenBrightnessChange {
        public int mId;
        public int mBrightness;

        /**
         * 构造函数
         * @param id {@link ScreenBrightnessId}
         * @param brightness 亮度值
         */
        public EventCurrentScreenBrightnessChange(int id, int brightness) {
            mId = id;
            mBrightness = brightness;
        }
    }

    /**
     * 亮度id
     */
    public static class ScreenBrightnessId {
        /**
         * 晚上
         */
        public static final int NIGHT   = 0;
        /**
         * 白天
         */
        public static final int DAY     = 1;
        /**
         * 未知，获取屏幕亮度，设置屏幕亮度，如果设置该参数，则白天返回白天的亮度，晚上返回晚上的亮度
         */
        public static final int UNKNOWN = -1;

        /**
         * 通过id获取名字，一般用于log打印
         * @param id {@link ScreenBrightnessId}
         * @return
         */
        public static String getName(int id) {
            return LogNameUtil.getName(id, ScreenBrightnessId.class);
        }
    }

    /**
     * 退出应用
     */
    public static class EventQuitApp {
        public EventQuitApp() {}
    }

    /**
     * 打开导航 </br>
     * <b>该方法launcher需要监听，在services回调的时候，launcher要负责打开当前用户选择的导航</br></b>
     */
    public static class EventStartNavigationApp {
        public EventStartNavigationApp() {}
    }

    /**
     * 媒体app状态发生改变 </br>
     * <b>一般launcher, systemUi用于监听CarPlay,AndroidAuto等APP打开的状态 </br>
     * 在CarPlay和Auto打开时，launcher需要把蓝牙音乐等和他冲突的app图标隐藏，systemUi要从最近列表隐藏掉这些app</br></b>
     */
    public static class EventMediaAppChanged {
        /**
         * 包名
         */
        public String mPackageName;
        /**
         * 打开状态
         */
        public boolean mIsOpen;

        /**
         * 构造函数
         * @param packageName 操作的APP的包名
         * @param isOpen 是否是打开
         */
        public EventMediaAppChanged(String packageName, boolean isOpen) {
            mPackageName = packageName;
            mIsOpen = isOpen;
        }
    }

    /**
     * 系统休眠
     */
    public static class EventGoToSleep {
        public EventGoToSleep() {}
    }

    /**
     * 系统唤醒
     */
    public static class EventWakeUp {
        public EventWakeUp() {}
    }

    /**
     * 竖屏项目的中间部分悬浮窗的显示或者隐藏
     */
    public static class EventFloatBarVisibility {
        public int mVisibility = View.VISIBLE;
        public EventFloatBarVisibility(int visibility) {
            mVisibility = visibility;
        }
    }

    /**
     * Tbox开关发生变化
     */
    public static class EventTboxOpen {
        public boolean mIsOpen;
        public EventTboxOpen(boolean isOpen) {
            mIsOpen = isOpen;
        }
    }

    /**升级状态*/
    public static class UpgradeStatus {
        /** 升级成功，正在准备重启 */
        public static final int SUCCESS_REBOOT = 0;
        /** 正在升级，请等候*/
        public static final int UPGRADING = 1;
        /** 请等待当前升级动作完成*/
        public static final int WAIT_UPGRADE = 2;
        /** 无效升级文件 */
        public static final int ERROR_INVALID_UPGRADE_PACKAGE = 3;
        /** 升级文件不存在 */
        public static final int ERROR_FILE_DOES_NOT_EXIST = 4;
        /** 文件IO错误 */
        public static final int ERROR_FILE_IO_ERROR = 5;
        /** 输入参数错误 */
        public static final int ERROR_INPUT_PARAMS = 6;

        public static String getName(int type) {
            return LogNameUtil.getName(type, UpgradeStatus.class);
        }
    }

    /**
     * 升级的监听接口类
     */
    public interface ProgressListener extends RecoverySystem.ProgressListener {
        /**
         * 升级进度
         * @param progress 进度值
         */
        @Override
        void onProgress(int progress);

        /**
         * 校验失败
         * @param status 升级状态，见{@link IVISystem.UpgradeStatus}
         * @param filepath 文件路径
         */
        void onUpgradeStatus(int status, String filepath);
    }

    /**
     * 定义记忆App的状态
     */
    public static class MemoryAppStatus {
        /** 有些应用需要知道当前是记忆起来，通过该参数通知 */
        public static final String KEY_MEMORY = "boot_memory";
    }

    /**
     * Package定义，只能在这里保留一份，其他地方必须引用这里
     */
    /** RoadRover服务的包名 */
    public static final String PACKAGE_IVI_SERVICES = "com.roadrover.services";
    /** 蓝牙服务的包名 */
    public static final String PACKAGE_BT_SERVICE = "com.roadrover.btservice";
    /** 收音机包名 */
    public static final String PACKAGE_RADIO = "com.roadrover.radio_v2";
    /** 音乐包名 */
    public static final String PACKAGE_MUSIC = "com.roadrover.music";
    /** 视频包名 */
    public static final String PACKAGE_VIDEO = "com.roadrover.video";
    /** 设置包名 */
    public static final String PACKAGE_SETTINGS = "com.roadrover.settings";
    /** 日历包名 */
    public static final String PACKAGE_CALENDAR = "com.android.calendar";
    /** AV包名 */
    public static final String PACKAGE_AV = "com.roadrover.avinput";
    /** 计算器包名 */
    public static final String PACKAGE_CALCULATOR = "com.android.calculator2";
    /** 行车秘书包名 */
    public static final String PACKAGE_CAR_ASSISTANT = "com.roadrover.carassistant";
    /** 蓝牙包名 */
    public static final String PACKAGE_BLUETOOTH = "com.roadrover.bluetooth";
    /** 倒车包名 */
    public static final String PACKAGE_CCD = "com.roadrover.carcameras";
    /** 图库包名 */
    public static final String PACKAGE_GALLERY = "com.roadrover.gallery";
    /** CarPlay包名 */
    public static final String PACKAGE_CAR_PLAY = "com.roadrover.carplay";
    /** AndroidAuto包名 */
    public static final String PACKAGE_ANDROID_AUTO = "com.google.android.projection.sink";
    /** 360全景包名 */
    public static final String PACKAGE_AVM = "com.roadrover.avm";
	/** DAB应用包名 */
    public static final String PACKAGE_DAB = "com.roadrover.dab";
	/**外挂360全景设备包名*/
    public static final String PACKAGE_AVM_EXTRA = "com.roadrover.carcameras";
    /** 音频设置包名 */
    public static final String PACKAGE_AUDIO_SETTING = "com.roadrover.audiosettings";
    /** 文件管理器包名 */
    public static final String PACKAGE_FILE_MANAGER = "com.roadrover.filemanager";
    /** launcher包名 */
    public static final String PACKAGE_LAUNCHER = "com.android.launcher3";
    /** 原生设置包名 */
    public static final String PACKAGE_ANDROID_SETTINGS = "com.android.settings";
    /** Google服务设置包名 */
    public static final String PACKAGE_GOOGLE_ANDROID_GMS = "com.google.android.gms";
    /** 空调包名 */
    public static final String PACKAGE_AIRCONDITION = "com.roadrover.aircondition";
    /** roadrover日历包名 */
    public static final String PACKAGE_ROADROVER_CALENDAR = "com.roadrover.calendar";
    /** roadrover计算器包名 */
    public static final String PACKAGE_ROADROVER_CALCULATOR = "com.roadrover.calculator";
    /** 文本阅读器包名 */
    public static final String PACKAGE_READER = "com.roadrover.reader";
    /** 车辆信息应用包名 */
    public static final String PACKAGE_CAR_INFORM = "com.roadrover.carinform";
    /** 语音包名 */
    public static final String PACKAGE_VOICE_ASSISTANT = "com.roadrover.voiceassistant";
    /** 车联网包名 */
    public static final String PACKAGE_CAR_NET = "com.roadrover.carnetworking";

    // 三方应用包名定义 start
    /** 美行导航 */
    public static final String PACKAGE_MEIXING_NAVI = "com.mxnavi.mxnavi";
    /** 亿联 */
    public static final String PACKAGE_ELINK = "net.easyconn";
    /** waze导航 */
    public static final String PACKAGE_WAZE = "com.waze";

    // 三方应用包名定义 end

    /**
     * Activity定义，只能在这里保留一份，其他地方必须引用这里
     */
    // 本机APP Activity名定义 start
    /** 蓝牙主Activity名 */
    public static final String ACTIVITY_BLUETOOTH = PACKAGE_BLUETOOTH + ".ui.activity.BtMainActivity";
    /** 视频主Activity名 */
    public static final String ACTIVITY_VIDEO = PACKAGE_VIDEO + ".ui.activity.VideoListActivity";
    /** 收音机主Activity名 */
    public static final String ACTIVITY_RADIO = PACKAGE_RADIO + ".activity.RadioMainActivity";
    /** 音乐主Activity名 */
    public static final String ACTIVITY_MUSIC = PACKAGE_MUSIC + ".activity.MainActivity";
    /** 音频设置主Activity名 */
    public static final String ACTIVITY_AUDIO_SETTING = PACKAGE_AUDIO_SETTING + ".ui.activity.MainActivity";
    /** Aux Activity名 */
    public static final String ACTIVITY_AUX = PACKAGE_AV + ".ui.activity.AuxActivity";
    /** Av Activity名 */
    public static final String ACTIVITY_AV = PACKAGE_AV + ".ui.activity.AVInputActivity";
    /** 前置摄像头 Activity名 */
    public static final String ACTIVITY_FRONT_CAMERA = PACKAGE_AV + ".ui.activity.FrontCameraActivity";
    /** web摄像头 Activity名 */
    public static final String ACTIVITY_WEB_CAMERA = PACKAGE_AV + ".ui.activity.WebCameraActivity";
    /** Tv Activity名 */
    public static final String ACTIVITY_TV = PACKAGE_AV + ".ui.activity.TVActivity";
    /** 原车设置 Activity名 */
    public static final String ACTIVITY_CAR_ASSISTANT = "com.roadrover.libcarassistant.CarSetStarterActivity";
    /** 原车信息 Activity名 */
    public static final String ACTIVITY_MYCAR = PACKAGE_CAR_ASSISTANT + ".CarInfoStarterActivity";
    /** 倒车 Activity名 */
    public static final String ACTIVITY_CCD = PACKAGE_CCD + ".ui.activity.MainActivity";
    /** 语音 Activity名 */
    public static final String ACTIVITY_VOICE_ASSISTANT = PACKAGE_VOICE_ASSISTANT + ".activity.MainActivity";
    /** 文件管理器主 Activity名 */
    public static final String ACTIVITY_FILE_MANAGER = "com.roadrover.filemanager.activity.FileManagerActivity";
    /** 图库主Activity名 */
    public static final String ACTIVITY_GALLERY = "com.roadrover.gallery.ui.activity.GalleryListActivity";
    /** 设置主Activity名 */
    public static final String ACTIVITY_SETTINGS = "com.roadrover.settings.ui.activity.MainActivity";
    /** 蓝牙音乐 Activity名 */
    public static final String ACTIVITY_BT_MUSIC = "com.roadrover.music.activity.BTMusicActivity";
    /** Media demo Activity名 */
    public static final String ACTIVITY_MEDIA_DEMO = "com.roadrover.mediademo.activity.MainActivity";
    /** 说明书 Activity名 */
    public static final String ACTIVITY_MANUAL = "com.roadrover.manual.MainActivity";
    /** CarPlay Activity名 */
    public static final String ACTIVITY_CARPLAY = "com.roadrover.carplay.CarPlayActivity";
    /** launcher 导航 Activity名 */
    public static final String ACTIVITY_LAUNCHER_NAVI = "com.android.launcher3.LaunchNaviActivity";
    /** 360全景 Activity名 */
    public static final String ACTIVITY_AVM = "com.roadrover.avm.AVMActivity";
    /** 外挂360全景设备Activity名*/
    public static final String ACTIVITY_AVM_EXTRA = "com.roadrover.carcameras.activity.AVMActivity";
    /** Android auto 主 Activity名 */
    public static final String ACTIVITY_ANDROID_AUTO = "com.google.android.projection.sink.ui.MainActivity";
    /** 小畅车联 Activity名 */
    public static final String ACTIVITY_XCCL = "com.carapk.xccl.activity.FirstActivity";
    /** 小畅市场 Activity名 */
    public static final String ACTIVITY_CARAPK = "com.carapk.store.activity.WelActivity";
    /** 方控学习 Activity名 */
    public static final String ACTIVITY_STUDY = PACKAGE_CAR_ASSISTANT + ".keystudy.KeyStudyActivity";
    /** 设置主界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_HOME = PACKAGE_SETTINGS + ".ui.activity.HomeActivity";
    /** 设置亮度界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_DISPLAY = PACKAGE_SETTINGS + ".ui.activity.DisplayActivity";
    /** 倒车设置界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_BACKCAR = PACKAGE_SETTINGS + ".ui.activity.BackCarSettingActivity";
    /** 默认导航设置界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_CHOOSENAVI = PACKAGE_SETTINGS + ".ui.activity.ChooseDefaultNaviActivity";
    /** 内存界面界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_APPDATA = PACKAGE_SETTINGS + ".ui.activity.AppDataActivity";
    /** 时间设置界面 Activity名 */
    public static final String ACTIVITY_SETTINGS_DATETIME = PACKAGE_SETTINGS + ".ui.activity.DateTimeSettingActivity";
    /** 高级设置页面Activity名 */
    public static final String ACTIVITY_ADVANCED_SETTING = PACKAGE_SETTINGS + ".ui.activity.AdvancedActivity";
    /** DAB页面Activity名 */
    public static final String ACTIVITY_DAB = PACKAGE_DAB + ".ui.activity.MainActivity";
    /** 空调主Activity名 */
    public static final String ACTIVITY_AIRCONDITION = PACKAGE_AIRCONDITION + ".activity.MainActivity";
    /** roadrover日历主Activity名 */
    public static final String ACTIVITY_ROADROVER_CALENDAR = PACKAGE_ROADROVER_CALENDAR + ".MainActivity";
    /** roadrover计算器主Activity名 */
    public static final String ACTIVITY_ROADROVER_CALCULATOR = PACKAGE_ROADROVER_CALCULATOR + ".Calculator";
    /** 文本阅读器主Activity名 */
    public static final String ACTIVITY_READER = PACKAGE_READER + ".FileListActivity";
    /** 车辆信息主Activity名 */
    public static final String ACTIVITY_CAR_INFORM = PACKAGE_CAR_INFORM + ".activity.MainActivity";
    /** 车联网主Activity名 */
    public static final String ACTIVITY_CAR_NET = PACKAGE_CAR_NET + ".business.home.CarnetworkingActivity";
    // 本机APP Activity名定义 end

    // android 原生APP Activity名定义 start
    /** google play Activity名 */
    public static final String ACTIVITY_GOOGLE_PLAY = "com.android.vending.AssetBrowserActivity";
    /** 日历 Activity名 */
    public static final String ACTIVITY_CALENDAR = PACKAGE_CALENDAR + ".AllInOneActivity";
    /** 计算器 Activity名 */
    public static final String ACTIVITY_CALCULATOR = PACKAGE_CALCULATOR + ".Calculator";
    /** 原生设置 Activity名 */
    public static final String ACTIVITY_ANDROID_SETTINGS = PACKAGE_ANDROID_SETTINGS + ".Settings";
    /** 切换语言 Activity名 */
    public static final String ACTIVITY_ANDROID_LANGUAGE = PACKAGE_ANDROID_SETTINGS + ".LanguageSettings";
    /** 谷歌浏览器 Activity名 */
    public static final String ACTIVITY_BROWSER = "com.android.browser.BrowserActivity";
    /** 谷歌下载 Activity名 */
    public static final String ACTIVITY_DOWNLOAD = "com.android.providers.downloads.ui.DownloadList";
    /** GMain Activity名 */
    public static final String ACTIVITY_GMAIL = "com.google.android.gm.ConversationListActivityGmail";
    /** 谷歌搜索 Activity名 */
    public static final String ACTIVITY_GOOGLE_SEARCH = "com.google.android.googlequicksearchbox.SearchActivity";
    /** 谷歌地图 Activity名 */
    public static final String ACTIVITY_GOOGLE_MAPS = "com.google.android.maps.MapsActivity";
    /** 应用管理 Activity名 */
    public static final String ACTIVITY_MANAGE_APPLICATION = PACKAGE_ANDROID_SETTINGS + ".ManageApplications";
    /** Google服务设置 Activity名 */
    public static final String ACTIVITY_GOOGLE_ANDROID_GMS_SETTINGS = PACKAGE_GOOGLE_ANDROID_GMS + ".app.settings.GoogleSettingsLink";
    // android 原生APP Activity名定义 end

    // 三方应用Activity名定义 start
    /** 三方应用 PANGO Activity名 */
    public static final String ACTIVITY_PANGO = "com.unicell.pangoandroid.activities.BootActivity";
    /** 三方应用 RadioIn Activity名 */
    public static final String ACTIVITY_RADIOIN = "com.mediacast.views.SplashActivity";
    /** 三方应用 Rest Activity名 */
    public static final String ACTIVITY_REST = "com.ZapGroup.Rest.ui.MainActivity";
    /** 三方应用 RLive Activity名 */
    public static final String ACTIVITY_RLIVE = "com.greenshpits.RLive.MainActivity";
    /** 三方应用 Waze Activity名 */
    public static final String ACTIVITY_WAZE = "com.waze.FreeMapAppActivity";
    /** 三方应用 天气 Activity名 */
    public static final String ACTIVITY_WEATHER = "boair.weatherisrael.MainActivity";
    /** 三方应用 TuneRadio Activity名 */
    public static final String ACTIVITY_TUNERADIO = "tunein.ui.actvities.TuneInHomeActivity";
    /** 三方应用 SoundCloud Activity名 */
    public static final String ACTIVITY_SOUNDCLOUD = "com.soundcloud.android.main.LauncherActivity";
    /** 三方应用 GpsTest Activity名 */
    public static final String ACTIVITY_GPS_TEST = "com.chartcross.gpstest.GPSTest";
    /** 三方应用 Easy Activity名 */
    public static final String ACTIVITY_EASY = "easy.co.il.easy3.Splash_Activity";
    /** 三方应用 CelloPark Activity名 */
    public static final String ACTIVITY_CELLOPARK = "air.com.cellogroup.Cellopark.SplashActivity";
    /** 三方应用 99Music Activity名 */
    public static final String ACTIVITY_99MUSIC = "com.cyberserve.android.reco99fm.RadioEco99fmMain";
    /** 三方APP YouTuBe Activity名 */
    public static final String ACTIVITY_YOUTUBE = "com.google.android.youtube.app.honeycomb.Shell$HomeActivity";
    /** 三方APP Facebook Activity名 */
    public static final String ACTIVITY_FACEBOOK = "com.facebook.katana.LoginActivity";
    /** 三方APP IGO地图 Activity名 */
    public static final String ACTIVITY_IGO = "com.navngo.igo";
    /** 三方APP Skype Activity名 */
    public static final String ACTIVITY_SKYPE = "com.skype.raider.Main";
    /** 三方APP 亿联 Activity名 */
    public static final String ACTIVITY_ELINK = PACKAGE_ELINK + ".WelcomeActivity";
    /** 三方APP 美行导航 Activity名 */
    public static final String ACTIVITY_MEIXING_NAVI = PACKAGE_MEIXING_NAVI + ".MXNavi";

    // 三方应用Activity名定义 end

    /**
     * Service名定义，只能在这里保留一份，其他地方必须引用这里
     */
    /** 倒车服务的 service 名 */
    public static final String SERVICE_CCD = PACKAGE_CCD + ".service.CarCamerasService";
    /** 蓝牙App服务的 service 名 */
    public static final String SERVICE_BLUETOOTH_APP = PACKAGE_BLUETOOTH + ".services.BtAppService";
    /** 蓝牙服务的 service 名 */
    public static final String SERVICE_BLUETOOTH = PACKAGE_BT_SERVICE + ".BluetoothService";
    /** 360全景服务的 service 名 */
    public static final String SERVICE_AVM = PACKAGE_AVM + ".AVMService";

    /**
     * action 定义，只能在这里保留一份，其他地方必须引用这里
     */
    /** CarPlay 进入广播名 */
    public static final String ACTION_CARPLAY_START = "com.roadrover.libcarplay.carplay_start";
    /** CarPlay 退出广播名 */
    public static final String ACTION_CARPLAY_END = "com.roadrover.libcarplay.carplay_end";
    /** CarPlay 开始打电话广播名 */
    public static final String ACTION_CARPLAY_PHONE_START = "com.roadrover.action.CARPLAY_PHONE_START";
    /** CarPlay 结束打电话广播名 */
    public static final String ACTION_CARPLAY_PHONE_END = "com.roadrover.action.CARPLAY_PHONE_END";
    /** AndroidAuto 进入广播名 */
    public static final String ACTION_ANDROID_AUTO_START = "com.google.android.projection.android_auto_start";
    /** AndroidAuto 退出广播名 */
    public static final String ACTION_ANDROID_AUTO_END = "com.google.android.projection.android_auto_end";
    /** 360服务 action 名 */
    public static final String ACTION_START_AVM_SERVICE = "com.roadrover.avm.start_service";
    /** 倒车服务 action 名 */
    public static final String ACTION_START_CCD_SERVICE = "com.roadrover.ccd.start_service";
    /** android view action名 */
    public static final String ACTION_VIEW = "android.intent.action.VIEW";

    /** 手刹广播 */
    public static final String ACTION_BRAKE = "com.roadrover.services.action.brake";
    /** 手刹参数 */
    public static final String PARAM_BRAKE = "brake";
    /** 手刹限制的APP列表 */
    public static final String PARAM_APP_LIST = "com.roadrover.services.action.brake.app_list";

    /** Activity切换广播 */
    public static final String ACTION_ACTIVITY_CHANGED = "android.intent.action.ACTIVITY_CHANGED";
    /** Activity切换广播带的参数 */
    public static final String KEY_PACKAGE_NAME = "packageName";

    /** 截屏完成通知其它应用广播 */
    public static final String ACTION_SCREENSHOT_FINISHED = "com.roadrover.systemui.action.screenshot.finished";
    /** 截屏广播带的参数 */
    public static final String PARAM_SCREENSHOT_PATH = "path";

    /** 下载完成通知其它应用广播 */
    public static final String ACTION_DOWNLOAD_FINISHED = "com.roadrover.provider.action.download.finished";
    /** 下载广播带的参数 */
    public static final String PARAM_DOWNLOAD_ID = "id";

    /** 设置系统时间 */
    public static final String ACTION_SET_TIME = "roadrover.intent.action.SET_TIME";

    // 美行地图定义的广播 start
    /***导航过程中向控发送引导类型和引导属性/导航转向信息协议****/
    public static final String ACTION_TO_CTRL_TURNING_INFO = "com.mxnavi.mxnavi.TO_CTRL_TURNING_INFO";
    /***导航向中控发送引导停止消息****/
    public static final String ACTION_TO_CTRL_STOP_GUIDANCE = "com.mxnavi.mxnavi.TO_CTRL_STOP_GUIDANCE";
    /***自车行政区变更时，导航向中控发消息****/
    public static final String ACTION_NAVIGATION_CURRENT_DISTRICT = "com.mxnavi.mxnavi.NAVI_CURRENT_DISTRICT";
    /***导航指南针协议****/
    public static final String ACTION_TO_CTRL_COMPASS_INFO = "com.mxnavi.mxnavi.TO_CTRL_COMPASS_INFO";
    /***电子眼协议****/
    public static final String ACTION_TO_CTRL_EEYE_INFO = "com.mxnavi.mxnavi.TO_CTRL_EEYE_INFO";
    /***引导类型有2种方式:0=普通引导类型,  1= 12点钟方向引导类型; ****/
    public static final String PARAM_IS_TWELVE_CLOCK = "IsTwelveClock";
    /***普通引导类型id****/
    public static final String PARAM_NORMAL_TURN_ID = "normal_turnID";
    /***12点钟方向引导类型id****/
    public static final String PARAM_TWELVE_CLOCK_TURN_ID = "TwelveClock_turnID";
    /***12点钟背景****/
    public static final String PARAM_ARRAY_TURN = "arrayTurn";
    /***引导点属性****/
    public static final String PARAM_GUIDE_TYPE = "guidetype";
    /***到下一引导点的距离(米)****/
    public static final String PARAM_DISTANCE = "distance";
    /***到目的地的距离(米)****/
    public static final String PARAM_DEST_DISTANCE = "destdistance";
    /***当前道路名称****/
    public static final String PARAM_ROAD_NAME = "roadname";
    /***下一道路名称****/
    public static final String PARAM_NEXT_ROAD_NAME = "nextroadname";
    /***到达目的地时间****/
    public static final String PARAM_DEST_TIME = "desttime";
    /***目的地名称****/
    public static final String PARAM_DEST_NAME = "destname";
    /***电子眼类型****/
    public static final String PARAM_MX_EEYE_TYPE = "type";
    /***到下一电子眼的距离，以米为单位****/
    public static final String PARAM_MX_EEYE_DISTANCE = "distance";
    /***限速****/
    public static final String PARAM_MX_SPEED_LIMIT = "speedLimit";
    /***车头方向****/
    public static final String PARAM_MX_DIRECTION = "direction";
    /***自车行政区省****/
    public static final String PARAM_MX_PROVINCE = "Province";
    /***自车行政区市****/
    public static final String PARAM_MX_CITY = "City";
    /***自车行政区区****/
    public static final String PARAM_MX_COUNTY = "County";
    // 美行地图定义的广播 end

    // 讯飞告诉其他应用录音开始
    public static final String ACTION_IFLYTEK_RECORD_EXIT_BY_CALL = "action_iflytek_record_exit_by_call"; // 通知应用蓝牙来去电，需要退出录音
    /***是否是蓝牙通知关闭****/
    public static final String PARAM_BYCALL = "byCall";

    /**
     * gps 数据变化
     */
    public static class EventGpsChanged {
        /** 经度 */
        public String mLongitude;
        /** 维度 */
        public String mLatitude;
        /** 精确 */
        public float mAccuracy;
        /** 速度 km/h */
        public float mSpeed;
        /** 海拔 */
        public double mAltitude;

        /**
         * 构造函数
         * @param longitude 经度
         * @param latitude 维度
         * @param accuracy 精确
         * @param altitude 海拔
         * @param fSpeed 速度 km/h
         */
        public EventGpsChanged(String longitude, String latitude, float accuracy, double altitude, float fSpeed) {
            mLongitude = longitude;
            mLatitude = latitude;
            mAccuracy = accuracy;
            mAltitude = altitude;
            mSpeed = fSpeed;
        }
    }

    /**
     * gps 卫星颗数变化
     */
    public static class EventGpsCountChanged {
        public int mGpsInView, mGpsInUse, mGlonassInView, mGlonassInUse;

        /**
         * 构造函数
         * @param iGpsInView 有几颗 gps 卫星可见
         * @param iGpsInUse 有几颗 gps 卫星可用
         * @param iGlonassInView 有几颗 Glonass 卫星可见
         * @param iGLonassInUse 有几颗 Glonass 卫星可用
         */
        public EventGpsCountChanged(int iGpsInView, int iGpsInUse, int iGlonassInView, int iGLonassInUse) {
            mGpsInView = iGpsInView;
            mGpsInUse = iGpsInUse;
            mGlonassInView = iGlonassInView;
            mGlonassInUse = iGLonassInUse;
        }
    }
}
