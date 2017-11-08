package com.roadrover.sdk.setting;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.roadrover.sdk.system.IVISystem;

/**
 * Setting定义
 */

public class IVISetting {

    /**
     * 设置页面跳转名字, 其值 {@value}
     */
    public static final String START_SETTINGS = "gotoWhich";
    /**
     * 设置子页面跳转名字, 其值 {@value}
     */
    public static final String KEY_SUBPAGE = "SubPage";
    /**
     * 广播方式导出名字, 其值 {@value}
     */
    public static final String ACTION = "com.roadrover.settings.model";

    /**
     * 启动设置页面
     * @param childName 设置名称，见{@link Global} {@link System} {@link Network}，为空则跳转设置主页面
     * @return
     */
    public static boolean startSettings(@NonNull Context context, String childName) {
        boolean ret = false;
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(IVISystem.PACKAGE_SETTINGS);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (!TextUtils.isEmpty(childName)) {
                    intent.putExtra(START_SETTINGS, childName);
                }
                context.startActivity(intent);
                ret = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 网络设置数据导出
     */
    public static class Network {
        /**名字或标签名-网络设置, 其值 {@value}*/
        public static final String NAME = "Network";

        /**WIFI状态，取值见{@link WIFIStatus}, 其值 {@value}*/
        public static final String WifiStatus = "WifiStatus";
        /**移动网络状态, 其值 {@value}*/
        public static final String MobileNetwork = "MobileNetwork";
        /**便携式热点, 其值 {@value}*/
        public static final String PortableHotSpot = "PortableHotSpot";

        /**热点名称, 其值 {@value}*/
        public static final String HotspotName = "HotspotName";
        /**热点密码, 其值 {@value}*/
        public static final String HotspotPassword = "HotspotPassword";

        /**WIFI信号强度，RSSI值, 其值 {@value}*/
        public static final String WifiSignalStrength = "WLANSignalStrength";
        /**WIFI信号等级最大值, 其值 {@value}*/
        public static final String WifiSignalMaxLevel = "WLANSignalMaxLevel";
        /**移动网络信号强度，ASU值, 其值 {@value}*/
        public static final String MobileSignalStrength = "MobileSignalStrength";
    }

    /**
     * 系统设置数据导出
     */
    public static class System {
        /**名字或标签名-系统设置, 其值 {@value}*/
        public static final String NAME = "System";
    }

    /**
     * 全局设置数据导出
     */
    public static class Global {
        /**名字或标签名-其它设置, 其值 {@value}*/
        public static final String NAME = "Global";

        /**背光亮度, 其值 {@value}*/
        public static final String BackLight = "BackLight";
        /**LED亮度, 其值 {@value}*/
        public static final String LEDLight = "LEDLight";
        /**浮动菜单设置, 其值 {@value}*/
        public static final String FloatMenu = "FloatMenu";

        /**蓝牙配对, 其值 {@value}*/
        public static final String BluetoothPaired = "BluetoothPaired";

        /**声音, 其值 {@value}*/
        public static final String Volume = "Volume";
        /**喇叭, 其值 {@value}*/
        public static final String Speaker = "Speaker";
        /**均衡器, 其值 {@value}*/
        public static final String EQ = "EQ";
        /**平衡声场, 其值 {@value}*/
        public static final String Sound = "Sound";


        /**倒车暂停媒体，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CCDPauseMedia = "CCDPauseMedia";
        /**倒车摄像头，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CCDCamera = "CCDCamera";
        /**倒车轨迹，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CCDCameraLine = "CCDCameraLine";
        /**倒车触发方式，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CCDCameraTrigger = "CCDCameraTrigger";
        /**倒车镜像，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CCDMirror = "CCDMirror";
        /**默认导航包名，取值为字符串, 其值 {@value}*/
        public static final String DefaultNaviPackage = "DefaultNaviPackage";
        /**外部空间，取值为整形, 其值 {@value}*/
        public static final String APPDataExternalSpace = "APPDataExternalSpace";
        /**内部空间，取值为整形, 其值 {@value}*/
        public static final String APPDataInternalSpace = "APPDataInternalSpace";
        /**已使用空间，取值为整形, 其值 {@value}*/
        public static final String APPDataUsedSpace = "APPDataUsedSpace";
        /**可用空间，取值为整形, 其值 {@value}*/
        public static final String APPDataAvailableSpace = "APPDataAvailableSpace";
        /**车门显示，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CarDoorSwitch = "CarDoorSwitch";
        /**雷达显示，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String CarRadarSwitch = "CarRadarSwitch";
        /**内置360全景，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String AVM360 = "AVM360";
        /**媒体通知，取值见{@link SwitchStatus}, 其值 {@value}*/
        public static final String MediaNotification = "MediaNotification";

        /**高级设置密码, 其值 {@value}*/
        public static final String Password = "Password";

        /**路畅渠道号, 其值 {@value}*/
        public static final String ProductFlavorsId = "ProductFlavorsId";

        /**开启日志，取值为实际的盘符路径, 其值 {@value}*/
        public static final String Logcat = "Logcat";
        /**刹车，取值为{@link Brake}, 其值 {@value}*/
        public static final String Brake = "Brake";
        /**USB ADB，取值为{@link SwitchStatus}, 其值 {@value}*/
        public static final String USBADB = "USBADB";
        /**Voice Optimization，取值为{@link SwitchStatus}, 其值 {@value}*/
        public static final String VoiceOptimization = "VoiceOptimization";
        /**TV，取值为{@link TV}, 其值 {@value}*/
        public static final String TV = "TV";
        /**倒车线电压，取值为{@link Brake}, 其值 {@value}*/
        public static final String CCDVoltage = "CCDVoltage";
        /**深度休眠，取值为{@link DeepSleep}, 其值 {@value}*/
        @Deprecated
        public static final String DeepSleep = "DeepSleep";
        /**麦克风，取值为{@link Microphone}, 其值 {@value}*/
        public static final String Microphone = "Microphone";
        /**智能休眠，取值为{@link SmartSleep}, 其值 {@value}*/
        @Deprecated
        public static final String SmartSleep = "SmartSleep";
        /**封闭系统，取值为{@link SystemClose}, 其值 {@value}*/
        public static final String SystemClose = "SystemClose";
        /**是否显示应用图标，取值为{@link ShowAppIcon}, 其值 {@value}*/
        public static final String ShowAppIcon = "ShowAppIcon";
        /**智能ACC OFF，取值为{@link SmartAccOff}, 其值 {@value}*/
        public static final String SmartAccOff = "SmartAccOff";
        /**原车360全景，取值为{@link SwitchStatus}, 其值 {@value}*/
        public static final String OriginalCarAVM = "OriginalCarAVM";
        /**时间设置页面, 其值 {@value}*/
        public static final String DateTimeSetting = "DateTimeSetting";
        /**疲劳预警的超时时间，取值为{@link FatigueDrivingCount}, 其值 {@value}*/
        public static final String FatigueDrivingCount = "FatigueDrivingCount";
        /**皮肤, 其值 {@value}*/
        public static final String Skin = "Skin";
        /**下位机数据调试, 其值 {@value}*/
        public static final String MCUDataDebug = "MCUDataDebug";
        /**原车字符编码, 其值 {@value}*/
        public static final String CarCharset = "CarCharset";
        /**是否录音, 其值 {@value}*/
        public static final String WhetherRecord = "WhetherRecord";
        /**默认方控还是学习方控, 其值 {@value}*/
        public static final String SquareControl = "SquareControl";
    }

    /**
     * 用于设置内部跳转不同activity和fragment
     */
    public static class Page {
        /**空, 其值 {@value}*/
        public static final int PAGE_NULL = -1;
        /**网络界面, 其值 {@value}*/
        public static final int PAGE_NETWORK = 0;
        /**蓝牙界面, 其值 {@value}*/
        public static final int PAGE_BIUETOOTH = 1;
        /**显示界面, 其值 {@value}*/
        public static final int PAGE_DISPLAY = 2;
        /**声音界面, 其值 {@value}*/
        public static final int PAGE_SOUNDEFFECT = 3;
        /**通用界面, 其值 {@value}*/
        public static final int PAGE_OTHER = 4;
        /**系统界面, 其值 {@value}*/
        public static final int PAGE_SYSTEM = 5;
        /**高级界面, 其值 {@value}*/
        public static final int PAGE_ADVANCE = 6;
    }

    /**
     * 用于设置内部跳转不同activity和fragment
     */
    public static class PageNetwork {
        /**wifi页面, 其值 {@value}*/
        public static final int PAGE_WIFI = 0;
        /**移动网络页面, 其值 {@value}*/
        public static final int PAGE_MONET = 1;
        /**热点页面, 其值 {@value}*/
        public static final int PAGE_HOTSPOT = 2;
    }

    /**
     * 用于设置内部跳转不同activity和fragment
     */
    public static class PageBluetooth {
        /**配对页面, 其值 {@value}*/
        public static final int PAGE_PAIRED = 0;
        /**新设备页面, 其值 {@value}*/
        public static final int PAGE_NEWDEVICES = 1;
    }

    /**
     * 用于设置内部跳转不同activity和fragment
     */
    public static class PageSoundEffect {
        /**声音页面, 其值 {@value}*/
        public static final int PAGE_VOLUME = 0;
        /**喇叭页面, 其值 {@value}*/
        public static final int PAGE_SPEAKER = 1;
        /**音效页面, 其值 {@value}*/
        public static final int PAGE_EQ = 2;
        /**声场页面, 其值 {@value}*/
        public static final int PAGE_SOUND = 3;
    }

    /**
     * 获取名字
     *
     * @param name 模块名字
     * @param item 子项名字
     * @return 名字
     */
    public static String getName(@NonNull String name, @NonNull String item) {
        return name.toLowerCase() + "_" + item.toLowerCase();
    }

    /**
     * 设置中的开关状态
     */
    public static class SwitchStatus {
        /**开, 其值 {@value}*/
        public static final String On = "true";
        /**关, 其值 {@value}*/
        public static final String Off = "false";
    }

    /**
     * WIFI状态
     */
    public static class WIFIStatus {
        /**禁用, 其值 {@value}*/
        public static final int DISABLE = 1;
        /**WIFI已连接, 其值 {@value}*/
        public static final int CONNECTED = 2;
        /**WIFI已启用, 其值 {@value}*/
        public static final int ENABLE = 3;
        /**默认, 其值 {@value}*/
        public static final int DEFAULT = ENABLE;
    }

    /**
     * 刹车
     */
    public static class Brake {
        /**功能启用，用户在行车中不允许进入三方视频应用（正在播放则退出）, 其值 {@value}*/
        public static final String ENABLE = "ENABLE";
        /**功能禁用，用户在行车中可以观看视频, 其值 {@value}*/
        public static final String DISABLE = "DISABLE";
        /**遮盖，视频有声音，图像为黑色背景并提示刹车警告, 其值 {@value}*/
        public static final String COVER = "COVER";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = ENABLE;
    }

    /**
     * TV
     */
    public static class TV {
        /**空, 其值 {@value}*/
        public static final String NULL = "NULL";
        /**BOX, 其值 {@value}*/
        public static final String BOX = "BOX";
        /**ISDB, 其值 {@value}*/
        public static final String ISDB = "ISDB";
        /**TCL, 其值 {@value}*/
        public static final String TCL = "TCL";
        /**ISDBT, 其值 {@value}*/
        public static final String ISDBT = "ISDBT";
        /**CMMB, 其值 {@value}*/
        public static final String CMMB = "CMMB";
        /**ISDBT-INN, 其值 {@value}*/
        public static final String ISDBT_INN = "ISDBT_INN";
        /**DVBT, 其值 {@value}*/
        public static final String DVBT = "DVBT";
        /**T108_T202, 其值 {@value}*/
        public static final String T108_T202 = "T108_T202";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = NULL;
    }

    /**
     * Ccd Voltage
     */
    public static class CCDVoltage {
        /**倒车12V供电, 其值 {@value}*/
        public static final String CCD_12 = "CCD_12";
        /**倒车6V供电, 其值 {@value}*/
        public static final String CCD_6 = "CCD_6";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = CCD_12;
    }

    /**
     * Deep Sleep
     */
    @Deprecated
    public static class DeepSleep {
        /**使能, 其值 {@value}*/
        public static final String ENABLE = "ENABLE";
        /**禁用, 其值 {@value}*/
        public static final String DISABLE = "DISABLE";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = ENABLE;
    }

    /**
     * Microphone
     */
    public static class Microphone {
        /**内置, 其值 {@value}*/
        public static final String INTERNAL = "INTERNAL";
        /**外置, 其值 {@value}*/
        public static final String EXTERNAL = "EXTERNAL";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = INTERNAL;
    }

    /**
     * SquareControl
     * LEARNING 学习的方控值
     * DEFAULT 默认的方控值
     */
    public static class SquareControl {
        /**学习, 其值 {@value}*/
        public static final String LEARNING = "LEARNING";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = "DEFAULT";
        /**发送默认, 其值 {@value}*/
        public static final int SEND_DEFAULT = 0xA0; // 表示使用默认的方控值
        /**发送学习, 其值 {@value}*/
        public static final int SEND_LEARNING = 0xA1; // 表示使用学习的方控值
    }

    /**
     * Smart Sleep
     */
    @Deprecated
    public static class SmartSleep {
        /**延时7天, 其值 {@value}*/
        public static final String DAY7 = "DAY7";
        /**延时15天, 其值 {@value}*/
        public static final String DAY15 = "DAY15";
        /**客制化, 其值 {@value}*/
        public static final String CUSTOM = "CUSTOM";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = "DEFAULT";
    }

    /**
     * Smart acc off
     */
    public static class SmartAccOff {
        /**深度休眠, 其值 {@value}*/
        public static final int DEEP_SLEEP = 0;

        /**设备断电, 其值 {@value}*/
        public static final int POWER_OFF = 1;

        /**自定义，设备休眠延时一定时间后进行设备断电动作, 其值 {@value}*/
        public static final int CUSTOM = Integer.MAX_VALUE;

        /**默认, 其值 {@value}*/
        public static final int DEFAULT = POWER_OFF;
    }

    /**
     * 皮肤
     */
    public static class Skin {
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = "default";
    }

    /**
     * <b>原车字符编码</b>
     * 默认（跟随系统编码/null），ASCII，GBK，UTF-8，UTF-16，UTF-16LE，UTF-16BE
     */
    public static class CarCharset {
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = "";
        /**ASCII码, 其值 {@value}*/
        public static final String ASCII = "ASCII";
        /**GBK码, 其值 {@value}*/
        public static final String GBK = "GBK";
        /**UTF8码, 其值 {@value}*/
        public static final String UTF8 = "UTF-8";
        /**UTF16码, 其值 {@value}*/
        public static final String UTF16 = "UTF-16";
        /**UTF16小端, 其值 {@value}*/
        public static final String UTF16LE = "UTF-16LE";
        /**UTF16大端, 其值 {@value}*/
        public static final String UTF16BE = "UTF-16BE";
    }

    /**
     * 疲劳预警的超时时间，单位min
     */
    public static class FatigueDrivingCount {
        /**关闭, 其值 {@value}*/
        public static final int CLOSE = 0;

        /**自定义，设备休眠延时一定时间后进行设备断电动作, 其值 {@value}*/
        public static final int CUSTOM = Integer.MAX_VALUE;

        /**默认值，两小时, 其值 {@value}*/
        public static final int DEFAULT = 120;
    }

    /**
     * System close
     */
    public static class SystemClose {
        /**否, 其值 {@value}*/
        public static final String NO = "NO";
        /**是, 其值 {@value}*/
        public static final String YES = "YES";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = NO;
    }

    /**
     * Show App Icon
     */
    public static class ShowAppIcon {
        /**是, 其值 {@value}*/
        public static final String YES = "YES";
        /**否, 其值 {@value}*/
        public static final String NO = "NO";
        /**默认, 其值 {@value}*/
        public static final String DEFAULT = YES;
    }

    /**
     * 移动网络等级
     */
    public static class MobileSignalLevel {
        /**未知, 其值 {@value}*/
        public static final int NONE_OR_UNKNOWN = 0;
        /**差, 其值 {@value}*/
        public static final int POOR = 1;
        /**中等, 其值 {@value}*/
        public static final int MODERATE = 2;
        /**好, 其值 {@value}*/
        public static final int GOOD = 3;
        /**比较好, 其值 {@value}*/
        public static final int GREAT = 4;
        /**最好, 其值 {@value}*/
        public static final int BINS = 5;
    }


    /**
     * 倒车摄像头是否打开
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isCcdEnable(Context context) {
        return TextUtils.equals(SettingModel.getContent(context, Global.NAME, Global.CCDCamera), SwitchStatus.On);
    }

    /**
     * 倒车轨迹线是否打开
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isCcdLineEnable(Context context) {
        return TextUtils.equals(SettingModel.getContent(context, Global.NAME, Global.CCDCameraLine), SwitchStatus.On);
    }

    /**
     * 倒车镜像是否打开
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isCcdMirror(Context context) {
        return TextUtils.equals(SettingModel.getContent(context, Global.NAME, Global.CCDMirror), SwitchStatus.On);
    }

    /**
     * 手刹是否有效
     * @param context 上下文对象
     * @return 有效返回true
     */
    public static boolean isHandbrakeEnable(Context context) {
        return !TextUtils.equals(SettingModel.getContent(context, Global.NAME, Global.Brake), Brake.DISABLE);
    }

    /**
     * 手刹是否为视频遮盖方式
     * @param context 上下文对象
     * @return 视频遮盖方式返回true
     */
    public static boolean isHandbrakeCover(Context context) {
        return TextUtils.equals(SettingModel.getContent(context, Global.NAME, Global.Brake), Brake.COVER);
    }

    /**
     * 获取默认导航包名
     * @param context 上下文对象
     * @return 返回默认导航包名，没有设置返回 ""
     */
    public static String getDefNaviPackage(Context context) {
        return SettingModel.getContent(context, Global.NAME, Global.DefaultNaviPackage);
    }

    /**
     * 悬浮窗是否使能
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isFloatMenuEnable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.FloatMenu));
    }

    /**
     * 车门开关状态发生变化的时候是否弹出车门状态的窗口
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isCarDoorWindowPopup(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.CarDoorSwitch));
    }

    /**
     * 雷达状态发生变化的时候是否弹出雷达状态的窗口
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isCarRadarWindowPopup(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.CarRadarSwitch));
    }

    /**
     * 内置360全景开关
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isAvm360Enable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.AVM360));
    }

    /**
     * 后台播放媒体是否显示通知开关
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean showMediaNotification(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, Global.MediaNotification));
    }

    /**
     * 获取智能ACC OFF休眠时间，单位为分钟
     * @param context 上下文对象
     * @return 断电方式为 {@link IVISetting.SmartAccOff#POWER_OFF}
     *         深度休眠方式为 {@link IVISetting.SmartAccOff#DEEP_SLEEP}
     *         其它值，则为智能ACC OFF休眠时间
     */
    public static int getSmartAccOff(Context context) {
        int ret = SmartAccOff.DEFAULT;
        final String string = SettingModel.getContent(context, IVISetting.Global.NAME, Global.SmartAccOff);
        try {
            ret = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取皮肤
     * @param context 上下文对象
     * @return 皮肤的名字 default,metal_gray,metal_blue
     */
    public static String getSkin(Context context) {
        final String string = SettingModel.getContent(context, IVISetting.Global.NAME, Global.Skin);
        return string;
    }

    /**
     * 获取疲劳预警的超时时间，单位为分钟
     * @param context 上下文对象
     * @return 功能关闭为 {@link IVISetting.FatigueDrivingCount#CLOSE}
     *         其它值，则为疲劳预警的超时时间
     */
    public static int getFatigueDrivingCount(Context context) {
        int ret = FatigueDrivingCount.DEFAULT;
        final String string = SettingModel.getContent(context, IVISetting.Global.NAME, Global.FatigueDrivingCount);
        try {
            ret = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 原车360全景开关
     * @param context 上下文对象
     * @return 打开返回true
     */
    public static boolean isOriginalCarAVMEnable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, Global.OriginalCarAVM));
    }

    /**
     * 获取热点名称
     * @param context 上下文对象
     * @return 返回wifi热点名字
     */
    public static String getHotspotName(Context context) {
        return SettingModel.getContent(context, Network.NAME, Network.HotspotName);
    }

    /**
     * 获取热点密码
     * @param context 上下文对象
     * @return 返回wifi热点密码
     */
    public static String getHotspotPassword(Context context) {
        return SettingModel.getContent(context, Network.NAME, Network.HotspotPassword);
    }

    /**
     * 获取WIFI信号强度
     * @param context 上下文对象
     * @return
     */
    public static int getWifiSignalStrength(Context context) {
        int ret = Integer.MAX_VALUE;
        String level = SettingModel.getContent(context, Network.NAME, Network.WifiSignalStrength);
        try {
            ret = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取WIFI信号等级最大值
     * @param context 上下文对象
     * @return
     */
    public static int getWifiSignalMaxLevel(Context context) {
        int ret = 5;        // WifiManager#RSSI_LEVELS
        String level = SettingModel.getContent(context, Network.NAME, Network.WifiSignalMaxLevel);
        try {
            ret = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取WIFI信号强度
     * @param context 上下文对象
     * @return
     */
    public static int getWifiSignalLevel(Context context) {
        final int strength = getWifiSignalStrength(context);
        return getWifiSignalLevel(context, strength);
    }

    /**
     * 根据信号强度获取信号等级
     * @param context  上下文对象
     * @param strength 信号强度
     * @return
     */
    public static int getWifiSignalLevel(Context context, int strength) {
        final int maxLevel = getWifiSignalMaxLevel(context);
        return (strength != Integer.MAX_VALUE) ? WifiManager.calculateSignalLevel(strength, maxLevel) : Integer.MAX_VALUE;
    }

    /**
     * 获取移动网络信号等级
     * @param context 上下文对象
     * @return {@link IVISetting.MobileSignalLevel}
     */
    public static int getMobileSignalStrength(Context context) {
        int ret = Integer.MAX_VALUE;
        String level = SettingModel.getContent(context, Network.NAME, Network.MobileSignalStrength);
        try {
            ret = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取移动网络信号等级，见{@link IVISetting.MobileSignalLevel}
     * @param context 上下文对象
     * @return {@link IVISetting.MobileSignalLevel}
     */
    public static int getMobileSignalLevel(Context context) {
        return getMobileSignalLevel(context, getMobileSignalStrength(context));
    }

    /**
     * 获取移动网络信号等级
     * @param context 上下文对象
     * @param asu     ASU值
     * @return {@link MobileSignalLevel}
     */
    public static int getMobileSignalLevel(Context context, int asu) {
        int level;
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        if (asu <= 2 || asu > 99) level = MobileSignalLevel.NONE_OR_UNKNOWN;
        else if (asu >= 18) level = MobileSignalLevel.BINS;
        else if (asu >= 12) level = MobileSignalLevel.GREAT;
        else if (asu >= 8) level = MobileSignalLevel.GOOD;
        else if (asu >= 5) level = MobileSignalLevel.MODERATE;
        else level = MobileSignalLevel.POOR;
        return level;
    }

    /**
     * 语音调优开关
     * @param context 上下文对象
     * @return 开启返回true
     */
    public static boolean isVoiceOptimizationEnable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, Global.VoiceOptimization));
    }

    /**
     * 是否录音开关
     * @param context 上下文对象
     * @return 开启返回true
     */
    public static boolean isWhetherRecordEnable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, Global.WhetherRecord));
    }

    /**
     * 是否开启下位机数据调试
     * @param context 上下文对象
     * @return 开启返回true
     */
    public static boolean isMCUDataDebugEnable(Context context) {
        return SwitchStatus.On.equals(SettingModel.getContent(context, IVISetting.Global.NAME, Global.MCUDataDebug));
    }

    /**
     * 原车字符编码
     * @param context 上下文对象
     * @return 默认（跟随系统编码/null），ASCII，GBK，UTF-8，UTF-16，UTF-16LE，UTF-16BE
     */
    public static String getCarCharset(Context context) {
        return SettingModel.getContent(context, IVISetting.Global.NAME, Global.CarCharset);
    }

    /**
     * 默认方控还是学习方控
     * @param context 上下文对象
     * @return DEFAULT, LEARNING
     */
    public static String getSquareControl(Context context) {
        return SettingModel.getContent(context, IVISetting.Global.NAME, Global.SquareControl);
    }
}
