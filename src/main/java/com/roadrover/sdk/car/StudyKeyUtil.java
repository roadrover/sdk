package com.roadrover.sdk.car;

import android.text.TextUtils;
import android.util.SparseArray;

import com.roadrover.sdk.utils.IniFileUtil;
import com.roadrover.sdk.utils.Logcat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 方控学习键值工具类
 */

public class StudyKeyUtil {

    // 配置文件路径,方控学习的ini要支持写的操作，所以单独建一个文件
    private static final String INI_STUDY_FILE_PATH = "/data/rr_data/ivi-studykey.ini";
    // 协议方控学习节点
    private static final String SECTION_STUDY_KEY = "StudyKey";
    // 长按分割符
    private static final String LONG_KEY_SPLIT = ",";
    // INI文件工具类对象
    public static IniFileUtil mIniFileUtil = null;

    /**
     * 获取配置字段
     *
     * @return 配置字段
     */
    public static synchronized SparseArray<String> getStudyKeyCodes() {
        SparseArray<String> keyCodes = new SparseArray<>();
        if (checkRead() && mIniFileUtil != null) {
            IniFileUtil.Section section = mIniFileUtil.get(SECTION_STUDY_KEY);
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
     * 获取长短按功能
     *
     * @param mixValue
     * @return
     */
    public static String[] loadStringValue(String mixValue) {
        if (!TextUtils.isEmpty(mixValue)) {
            if (mixValue.contains(LONG_KEY_SPLIT)) {
                String[] strs = mixValue.split(LONG_KEY_SPLIT);
                if (strs != null) {
                    return strs;
                }
            }
            return new String[]{mixValue, ""};
        }
        return null;
    }

    /**
     * 获取AD通道值
     *
     * @param mixkey
     * @return
     */
    public static int loadChannel(int mixkey) {
        if (mixkey > 0) {
            return (mixkey >> 16) & 0xFF;
        }
        return 0;
    }

    /**
     * 获取范围最大值
     *
     * @param mixkey
     * @return
     */
    public static int loadMax(int mixkey) {
        if (mixkey > 0) {
            return (mixkey >> 8) & 0xFF;
        }
        return 0;
    }

    /**
     * 获取范围最小值
     *
     * @param mixkey
     * @return
     */
    public static int loadMin(int mixkey) {
        if (mixkey > 0) {
            return mixkey & 0xFF;
        }
        return 0;
    }

    /**
     * 生成存储键值混合Id
     *
     * @param channel
     * @param max
     * @param min
     * @return
     */
    public static String makeMixKeyId(int channel, int max, int min) {
        if (channel >= 0 && max >= 0 && min >= 0) {
            return ((channel << 16) | (max << 8) | min) + "";
        }
        return 0+"";
    }

    /**
     * 生成存储键值混合值
     * @param shortAction
     * @param longAction
     * @return
     */
    public static String makeMixKeyValue(String shortAction, String longAction) {
        if (TextUtils.isEmpty(longAction)) {
            return shortAction;
        } else {
            return shortAction == null ? "," + longAction : shortAction + "," + longAction;
        }
    }

    /**
     * 存储学习完的方控表到ini文件
     * @param list
     */
    public static void saveListToFile(List<IVICar.StudyKeyItem> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (checkRead() && mIniFileUtil != null) {
            mIniFileUtil.remove(SECTION_STUDY_KEY);
            for (IVICar.StudyKeyItem item : list) {
                mIniFileUtil.set(SECTION_STUDY_KEY,
                        makeMixKeyId(item.mChannel, item.mMax, item.mMin),
                        makeMixKeyValue(item.mShortAction, item.mLongAction));
            }
            mIniFileUtil.save();
        }
    }

    /**
     * 检查可读
     */
    private static boolean checkRead() {
        if (null == mIniFileUtil) {
            File file = new File(INI_STUDY_FILE_PATH);
            if (file.exists()) {
                Logcat.d("ready to read " + INI_STUDY_FILE_PATH);
                mIniFileUtil = new IniFileUtil(file);
                return true;
            } else {
                Logcat.e("failed, file " + INI_STUDY_FILE_PATH + " not exist.");
                return false;
            }
        } else {
            return true;
        }
    }
}
