package com.roadrover.sdk.setting;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.roadrover.sdk.system.SystemManager;
import com.roadrover.sdk.utils.Logcat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设置model类，和{@link SystemManager}分开处理</br></br>
 *
 * 使用实例：</br>
 * <b>一、获取背光值</br>
 * String value = SettingModel.getContent(this, IVISetting.Currency.NAME, IVISetting.Currency.BackLight);</br></br>
 *
 * <b>二、监听背光值变化</br>
 * <b>1、定义监听接口</br>
 * private SettingModel.ModelListener mSettingModelListener = new SettingModel.ModelListener() {</br>
 *    <b>@Override</br>
 *    public void onChange(String oldVal, String newVal) {</br>
 *        Log.d(TAG, "model backlight onChange, oldVal = " + oldVal + ", newVal = " + newVal);</br>
 *    }</br>
 * };</br></br>
 *
 * <b>2、注册监听器</br>
 * SettingModel.registerModelListener(this, IVISetting.Currency.NAME, IVISetting.Currency.BackLight, mSettingModelListener);</br></br>
 *
 * <b>3、反注册监听器</br>
 * SettingModel.unregisterModelListener(this, mSettingModelListener);
 */

public class SettingModel {

    /**model集合*/
    private static List<Bean> sModels = new ArrayList<>();

    /**单列线程池*/
    private static ExecutorService sSignalExecutorService = Executors.newSingleThreadExecutor();

    /**最后将数据返回到主线程handler对象*/
    private static Handler sHandler = null;

    /**model监听器*/
    public interface ModelListener {
        /**
         * model监听器
         * @param oldVal 变化之前的值
         * @param newVal 变化之后的值
         */
        void onChange(String oldVal, String newVal);
    }

    /**数据结构体*/
    private static class Bean {
        /**上下文对象*/
        public Context context;
        /**数据监听对象*/
        public ModelListener modelListener;
        /**数据库内容监听对象*/
        public ContentObserver contentObserver;
        /**名字*/
        public String name;
        /**数值*/
        public String value;
    }

    /**
     * 获取logcat 打印的路径
     * @param context 上下文对象
     * @return 返回打印logcat的输出目录
     */
    public static String getPrintLogcatPath(Context context) {
        if (context == null) {
            Logcat.w("context is null!");
            return "";
        }
        return SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.Logcat);
    }

    /**
     * 注册麦克风监听
     * @param context  上下文对象
     * @param listener 监听对象
     */
    public static void registerMicrophoneListener(Context context, ModelListener listener) {
        registerModelListener(context, IVISetting.Global.NAME, IVISetting.Global.Microphone, listener);
    }

    /**
     * 获取内置，外置mic选项
     * @param context 上下文对象
     * @return 返回是外置还是内置mic {@link com.roadrover.sdk.setting.IVISetting.Microphone}
     */
    public static String getMicrophone(Context context) {
        if (context == null) {
            Logcat.w("context is null!");
            return "";
        }
        return SettingModel.getContent(context, IVISetting.Global.NAME, IVISetting.Global.Microphone);
    }

    /**
     * 获取面板按键灯亮度
     * @param context 上下文对象
     * @return 面板按键灯亮度 0-100
     */
    public static int getPanelLight(Context context) {
        if (context == null) {
            Logcat.w("context is null!");
            return 0;
        }
        return getContentInt(context, IVISetting.Global.NAME, IVISetting.Global.LEDLight, 0);
    }

    /**
     * 注册面板按键灯监听
     * @param context  上下文对象
     * @param listener 监听对象
     */
    public static void registerPanelLightListener(Context context, ModelListener listener) {
        registerModelListener(context, IVISetting.Global.NAME, IVISetting.Global.LEDLight, listener);
    }

    /**
     * 获取智能休眠的时间
     * @param context 上下文对象
     * @return {@link IVISetting.SmartAccOff}
     */
    public static int getSmartAccOffTimer(Context context) {
        if (context == null) {
            Logcat.w("context is null!");
            return IVISetting.SmartAccOff.POWER_OFF;
        }
        return getContentInt(context, IVISetting.Global.NAME, IVISetting.Global.SmartAccOff, IVISetting.SmartAccOff.POWER_OFF);
    }

    /**
     * 获取默认导航
     * @param context 上下文
     * @return 返回默认导航的包名
     */
    public static String getNavigationPackage(Context context) {
        if (context == null) {
            Logcat.w("context is null!");
            return "";
        }
        return getContent(context, IVISetting.Global.NAME, IVISetting.Global.DefaultNaviPackage);
    }

    /**
     * 注册监听默认导航改变的消息
     * @param context 上下文
     * @param listener 监听器
     */
    public static void registerNavigationListener(Context context, ModelListener listener) {
        registerModelListener(context, IVISetting.Global.NAME, IVISetting.Global.DefaultNaviPackage, listener);
    }

    /**
     * 根据名字和每项获取设置的内容，返回一个int
     * @param context 上下文对象
     * @param name    名字, 比如{@link com.roadrover.sdk.setting.IVISetting.Network#NAME}
     * @param item    单项名字，比如WIFI状态{@link com.roadrover.sdk.setting.IVISetting.Network#WifiStatus}
     * @return
     */
    public static int getContentInt(Context context, @NonNull String name, @NonNull String item, int defValue) {
        String result = getContent(context, name, item);
        try {
            if (!TextUtils.isEmpty(result)) {
                return Integer.valueOf(result);
            }
        } catch (Exception e) {

        }
        return defValue;
    }

    /**
     * 根据名字和每项获取设置的内容
     *
     * @param name 名字, 比如{@link com.roadrover.sdk.setting.IVISetting.Network#NAME}
     * @return item 哪一项，比如获取热点名称{@link com.roadrover.sdk.setting.IVISetting.Network#HotspotName}
     */
    public static String getContent(Context context, @NonNull String name, @NonNull String item) {
        return getContent(context, IVISetting.getName(name, item));
    }

    /**
     * 注册model监听器
     * @param name 名字, 比如{@link com.roadrover.sdk.setting.IVISetting.Network}
     * @param item 哪一项，比如获取热点名称{@link com.roadrover.sdk.setting.IVISetting.Network#HotspotName}
     * @param listener model监听器对象
     */
    public static void registerModelListener(Context context, @NonNull final String name, @NonNull final String item, ModelListener listener) {
        if (null != context && null != listener) {
            final Bean bean = new Bean();
            bean.context = context;
            bean.name = IVISetting.getName(name, item);
            bean.modelListener = listener;
            sSignalExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    bean.value = getContent(bean.context, name, item);
                }
            });
            if (sHandler == null) {
                sHandler = new Handler();
            }
            bean.contentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    if (!selfChange) {
                        // 数据只允许被设置本身改变
                        if (null != sModels) {
                            for (Bean loop : sModels) {
                                if (null != loop) {
                                    if (this == loop.contentObserver) {
                                        if (null != bean.modelListener) {
                                            sSignalExecutorService.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String oldVal = bean.value;
                                                    final String newVal = getContent(bean.context, bean.name);
                                                    boolean change = false;
                                                    if (null == oldVal && null != newVal) {
                                                        change = true;
                                                    } else if (null != oldVal && null == newVal) {
                                                        change = true;
                                                    } else if (null != oldVal && null != newVal) {
                                                        if (!oldVal.equals(newVal)) {
                                                            change = true;
                                                        }
                                                    }
                                                    if (change) {
                                                        sHandler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                bean.modelListener.onChange(bean.value, newVal);
                                                                bean.value = newVal;
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            };
            sModels.add(bean);
            context.getContentResolver().registerContentObserver(Provider.NameValueColumns.CONTENT_URI,
                    false, bean.contentObserver);
        } else {
            Logcat.w("context = " + context + " listener = " + listener);
        }
    }

    /**
     * 注销掉所有通过context注册的监听器
     * @param context 上下文对象
     */
    public static void unregisterModelListener(Context context) {
        if (context != null) {
            Iterator<Bean> iterator = sModels.iterator();
            while (iterator.hasNext()) {
                Bean bean = iterator.next();
                if (bean != null && bean.context == context) {
                    context.getContentResolver().unregisterContentObserver(bean.contentObserver);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 反注册model监听器
     * @param context 上下文对象
     * @param listener model监听器
     */
    public static void unregisterModelListener(Context context, ModelListener listener) {
        if (null != context && null != listener) {
            if (null != sModels) {
                for (Bean bean : sModels) {
                    if (null != bean) {
                        if (listener == bean.modelListener) {
                            context.getContentResolver().unregisterContentObserver(bean.contentObserver);
                            sModels.remove(bean);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据名字获取设置的内容
     * @param context 上下文对象
     * @param name 名字
     */
    private static String getContent(Context context, @NonNull String name) {
        String ret = null;
        if (null != context) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Provider.NameValueColumns.CONTENT_URI,
                        new String[]{Provider.NameValueColumns.NAME, Provider.NameValueColumns.VALUE},
                        Provider.NameValueColumns.NAME + "=?", new String[]{name}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ret = cursor.getString(cursor.getColumnIndexOrThrow(Provider.NameValueColumns.VALUE));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return ret;
    }
}
