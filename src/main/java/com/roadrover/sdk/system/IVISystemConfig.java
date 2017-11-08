package com.roadrover.sdk.system;

import android.text.TextUtils;

import com.roadrover.sdk.utils.IniFileUtil;
import com.roadrover.sdk.utils.Logcat;

import java.io.File;

/**
 * 底层配置获取全局类
 */

public class IVISystemConfig {

    private static final String RR_SYSTEM_CONFIG_PATH = "system/etc/RR-system-config.ini";
    private static final String SECTION_NAVI = "Navi";
    private static final String KEY_NAVI_LOCATION = "location";

    private static IniFileUtil sIniFileUtil = null; // INI文件工具类对象

    /**
     * 获取导航栏位置
     *
     * @return true代表左，false代表右
     */
    public static boolean isNaviLocationLeft() {
        String location = getString(SECTION_NAVI, KEY_NAVI_LOCATION);
        return "left".equals(location);
    }

    /**
     * 获取INI解析对象
     *
     * @return INI解析对象
     */
    public static IniFileUtil getIniFileUtil() {
        checkRead();
        return sIniFileUtil;
    }

    /**
     * 获取配置文件中的 StringList
     *
     * @param section
     * @param name
     * @return
     */
    private static String[] getStringList(String section, String name) {
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
        if (checkRead() && sIniFileUtil != null) {
            Object object = sIniFileUtil.get(section, key);
            if (object instanceof String) {
                ret = (String) object;
            } else {
                Logcat.w("failed, " + object + " is not instanceof String.");
            }
        }

        return ret;
    }

    /**
     * 获取配置的字符串
     *
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
        try {
            ret = Integer.valueOf(getString(section, key));
        } catch (Exception e) {
            Logcat.w("failed, " + section + " " + key);
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
        try {
            ret = Float.valueOf(getString(section, key));
        } catch (Exception e) {
            Logcat.w("failed, " + section + " " + key);
        }
        return ret;
    }

    /**
     * 获取整型
     *
     * @param section      section名称
     * @param key          key名称
     * @param defaultValue 默认值
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
     * @param section      section名称
     * @param key          key名称
     * @param defaultValue 默认值
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
        if ("true".equals(string)) {
            ret = true;
        } else if ("false".equals(string)) {
            ret = false;
        } else {
            Logcat.w("failed, " + section + " " + key + " " + defaultValue);
        }
        return ret;
    }

    /**
     * 检查可读
     */
    private static boolean checkRead() {
        if (null == sIniFileUtil) {
            File file = new File(RR_SYSTEM_CONFIG_PATH);
            if (file.exists()) {
                Logcat.d("ready to read " + RR_SYSTEM_CONFIG_PATH);
                sIniFileUtil = new IniFileUtil(file);
                return true;
            } else {
                Logcat.e("failed, file " + RR_SYSTEM_CONFIG_PATH + " not exist.");
                return false;
            }
        } else {
            return true;
        }
    }
}
