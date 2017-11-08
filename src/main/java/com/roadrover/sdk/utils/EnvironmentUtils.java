package com.roadrover.sdk.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.roadrover.sdk.system.IVIConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对盘符操作的工具类，主要是判断哪些盘符挂载上
 */

public class EnvironmentUtils {

    public static final String INAND = "iNand";//iNand storage
    public static final String SDCARD = "SD";// SD card storage
    public static final String SDCARD1 = "SD1";// SD card1 storage
    public static final String USB = "USB";// USB disk storage
    public static final String USB1 = "USB1";// USB1 disk storage
    public static final String USB2 = "USB2";// USB2 disk storage
    public static final String USB3 = "USB3";// USB3 disk storage
    public static final String USB4 = "USB4";// USB4 disk storage
    public static final String USB5 = "USB5";// USB5 disk storage
    public static final String USB6 = "USB6";// USB6 disk storage
    public static final String USB7 = "USB7";// USB7 disk storage

    private static final String CONFIG_NAME = "APPConfig.ini";//配置文件绝对路径
    private static final String SECTION_NAME = "Environment";//INI文件中存储设备节点名字
    private static Map<String, String> mStorageDevices = null;// 存储设备集合

    private StorageManager mStorageManager = null; // 存储设备管理对象
    private Context mContext = null;               // 上下文对象

    /**
     * 私有无参构造
     */
    private EnvironmentUtils() {

    }

    /**
     * 带参构造
     */
    public EnvironmentUtils(Context context) {
        if (null != context) {
            mContext = context;
            mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        } else {
            Logcat.e("context is null exception.");
        }

        // 注册盘符路径，从APP配置文件中读取
        if (null == mStorageDevices) {
            mStorageDevices = new HashMap<>();

            // 显示的绝对盘符路径是默认存储设备路径，无须修改它，针对车机无配置文件的异常情况的默认值
            registerStorageDevice(INAND, IVIConfig.getINandDiskPath(), "/storage/emulated/sdcard");
            registerStorageDevice(SDCARD, IVIConfig.getSDDiskPath(), "/storage/card");
            registerStorageDevice(SDCARD1, IVIConfig.getSD1DiskPath(), null);
            registerStorageDevice(USB, IVIConfig.getUSBDiskPath(), "/storage/udisk0");
            registerStorageDevice(USB1, IVIConfig.getUSB1DiskPath(), "/storage/udisk1");
            registerStorageDevice(USB2, IVIConfig.getUSB2DiskPath(), "/storage/udisk2");
            registerStorageDevice(USB3, IVIConfig.getUSB3DiskPath(), null);
            registerStorageDevice(USB4, IVIConfig.getUSB4DiskPath(), null);
            registerStorageDevice(USB5, IVIConfig.getUSB5DiskPath(), null);
            registerStorageDevice(USB6, IVIConfig.getUSB6DiskPath(), null);
            registerStorageDevice(USB7, IVIConfig.getUSB7DiskPath(), null);
        }
    }

    /**
     * get paths of all mounted storage
     * @return return mounted storage paths stored in a string array. eg: "mnt/udisk1", "mnt/ext_sdcard1" ...
     */
    public String[] getStorageMountedPaths() {

        ArrayList<String> arrayPath = new ArrayList<String>();
        int i, count;

        List<String> paths = getStoragePaths();

        if (null == paths)
            return null;

        for (i = 0; i < paths.size(); i++) {
            String strState = getStorageState(paths.get(i));
            if (strState != null && strState.equals(Environment.MEDIA_MOUNTED)) {
                arrayPath.add(paths.get(i));
            }
        }

        count = arrayPath.size();
        String[] pathsReturn = new String[count];
        for (i = 0; i < count; i++) {
            pathsReturn[i] = arrayPath.get(i);
        }

        return pathsReturn;

    }

    /**
     * 根据存储设备名获取存储设备路径
     *
     * @param name 存储设备名，见{@link #INAND}...
     * @return 存储设备路径
     */
    public String getStoragePath(String name) {
        String ret = null;

        if (!TextUtils.isEmpty(name)) {
            if (null != mStorageDevices) {
                for (Map.Entry<String, String> entry : mStorageDevices.entrySet()) {
                    if (name.equals(entry.getKey())) {
                        ret = entry.getValue();
                        break;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * 根据存储设备路径获取存储设备名
     *
     * @param path 存储设备路径
     * @return 存储设备名，见{@link #INAND}...
     */
    public String getStorageName(String path) {
        String ret = null;

        if (!TextUtils.isEmpty(path)) {
            if (null != mStorageDevices) {
                for (Map.Entry<String, String> entry : mStorageDevices.entrySet()) {
                    if (path.equals(entry.getValue())) {
                        ret = entry.getKey();
                        break;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * 判断传入路径是否为有效盘符路径
     */
    public boolean isValidPath(String path) {
        boolean ret = false;

        if (!TextUtils.isEmpty(path)) {
            if (null != mStorageDevices) {
                for (Map.Entry<String, String> entry : mStorageDevices.entrySet()) {
                    if (path.equals(entry.getValue())) {
                        ret = true;
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 判断传入路径是否为INAND
     */
    public boolean isINandPath(String path) {
        boolean ret = false;

        final String name = getStorageName(path);
        if (INAND.equals(name)) {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断传入路径是否为SD
     */
    public boolean isSDPath(String path) {
        boolean ret = false;

        final String name = getStorageName(path);
        if (SDCARD.equals(name) || SDCARD1.equals(name)) {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断传入路径是否为USB
     */
    public boolean isUSBPath(String path) {
        boolean ret = false;

        final String name = getStorageName(path);
        if (USB.equals(name) || USB1.equals(name) || USB2.equals(name) ||
                USB3.equals(name) || USB4.equals(name) || USB5.equals(name) ||
                USB6.equals(name) || USB7.equals(name)) {
            ret = true;
        }
        return ret;
    }

    /**
     * get list of paths of all storage
     * @return return storage paths stored in a string list.
     */
    public List<String> getStoragePaths() {
        List<String> ret = null;

        if (null != mStorageDevices) {
            ret = new ArrayList<>();
            for (Map.Entry<String, String> entry : mStorageDevices.entrySet()) {
                ret.add(entry.getValue());
            }
        } else {
            Logcat.w("mStorageDevices is null, get storage devices from StorageManager#getVolumePaths.");
            if (null != mStorageManager) {
                String paths[] = null;
                try {
                    paths = (String[]) mStorageManager.getClass()
                            .getMethod("getVolumePaths")
                            .invoke(mStorageManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (null != paths) {
                    ret = Arrays.asList(paths);
                }
            }
        }
        return ret;
    }

    /**
     * get list of SD paths of all storage
     * @return return SD storage paths stored in a string list.
     */
    public List<String> getSDStoragePaths() {
        List<String> ret = new ArrayList<>();

        List<String> paths = getStoragePaths();
        if (null != paths) {
            for (int i = 0; i < paths.size(); i++) {
                final String path = paths.get(i);
                if (isSDPath(path)) {
                    ret.add(path);
                }
            }
        }
        return ret;
    }

    /**
     * get list of USB paths of all storage
     * @return return USB storage paths stored in a string list.
     */
    public List<String> getUSBStoragePaths() {
        List<String> ret = new ArrayList<>();

        List<String> paths = getStoragePaths();
        if (null != paths) {
            for (int i = 0; i < paths.size(); i++) {
                final String path = paths.get(i);
                if (isUSBPath(path)) {
                    ret.add(path);
                }
            }
        }
        return ret;
    }

    /**
     * get list of paths of all mount storage
     * @return return mount storage paths stored in a string list.
     */
    public List<String> getMountStoragePaths() {
        List<String> ret = new ArrayList<>();

        List<String> paths = getStoragePaths();
        if (null != paths) {
            for (int i = 0; i < paths.size(); i++) {
                final String path = paths.get(i);
                if (isStorageMounted(path)) {
                    ret.add(path);
                }
            }
        }
        return ret;
    }

    /**
     * get list of SD paths of all storage
     * @return return mount SD storage paths stored in a string list.
     */
    public List<String> getMountSDStoragePaths() {
        List<String> ret = new ArrayList<>();

        List<String> paths = getSDStoragePaths();
        if (null != paths) {
            for (int i = 0; i < paths.size(); i++) {
                final String path = paths.get(i);
                if (isStorageMounted(path)) {
                    ret.add(path);
                }
            }
        }
        return ret;
    }

    /**
     * get list of USB paths of all storage
     * @return return mount USB storage paths stored in a string list.
     */
    public List<String> getMountUSBStoragePaths() {
        List<String> ret = new ArrayList<>();

        List<String> paths = getUSBStoragePaths();
        if (null != paths) {
            for (int i = 0; i < paths.size(); i++) {
                final String path = paths.get(i);
                if (isStorageMounted(path)) {
                    ret.add(path);
                }
            }
        }
        return ret;
    }

    /**
     * 判断指定路径是否挂载上
     *
     * @param path
     * @return 挂载成功返回true，否则返回false
     */
    public boolean isStorageMounted(String path) {
        boolean ret = false;

        final String state = getStorageState(path);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            ret = true;
        }
        return ret;
    }

    /**
     * 注册存储设备
     *
     * @param name    存储设备名，见{@link #INAND}...
     * @param path    存储设备的配置磁盘路径
     * @param defPath 默认存储设备路径，针对车机无配置文件的异常情况
     * @return 注册返回值
     */
    private boolean registerStorageDevice(String name, String path, String defPath) {
        boolean ret = false;
        if (null != mStorageDevices) {
            if (!TextUtils.isEmpty(path)) {
                Logcat.d("registerStorageDevice success, name = " + name + ", path = " + path);
                mStorageDevices.put(name, path);
                ret = true;
            } else {
                Logcat.d("The machine unsupport " + name + " device.");
            }
        }
        return ret;
    }

    /**
     * 反注册存储设备
     *
     * @param name 存储设备名，见{@link #INAND}...
     * @return 反注册返回值
     */
    private boolean unregisterStorageDevice(String name) {
        boolean ret = false;
        if (null != mStorageDevices) {
            final String path = mStorageDevices.remove(name);
            if (!TextUtils.isEmpty(path)) {
                Logcat.d("unregisterStorageDevice " + name + " success.");
            } else {
                Logcat.d("unregisterStorageDevice, not need to unregister " + name);
            }
            ret = true;
        }
        return ret;
    }

    /**
     * @param mountPoint
     * @return return string state. eg: "mounted" ...
     * @brief get mounted/unmounted state of storage device
     **/
    private String getStorageState(String mountPoint) {
        String ret = null;

        if (null != mStorageManager) {
            if (!TextUtils.isEmpty(mountPoint)) {
                try {
                    ret = (String) mStorageManager.getClass()
                            .getMethod("getVolumeState", String.class)
                            .invoke(mStorageManager, mountPoint);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }
}
