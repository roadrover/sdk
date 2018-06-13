package com.roadrover.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.roadrover.sdk.system.IVISystem;

/**
 * 用来适配AndroidAuto的工具类
 */

public class AndroidAutoUtil extends BaseThreeAppUtil {

    private boolean mIsAndroidAutoStatus = false; // 当前是否是AndroidAuto状态

    /**
     * auto状态
     */
    public interface AndroidAutoListener {
        /**
         * auto进入
         */
        void onStartAuto();

        /**
         * auto结束
         */
        void onEndAuto();
    }
    private AndroidAutoListener mAndroidAutoListener; // 监听auto状态

    public AndroidAutoUtil(@NonNull Context context) {
        super(context);

        // 注册监听
        init(IVISystem.ACTION_ANDROID_AUTO_START, IVISystem.ACTION_ANDROID_AUTO_END);
    }

    /**
     * 监听安卓auto的打开和关闭状态
     * @param listener
     */
    public void setAndroidAutoListener(AndroidAutoListener listener) {
        mAndroidAutoListener = listener;
    }

    /**
     * 判断当前是否连接Auto状态
     * @return
     */
    public boolean isAndroidAutoStatus() {
        return mIsAndroidAutoStatus;
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Logcat.d("action:" + action);
            if (TextUtils.equals(action, IVISystem.ACTION_ANDROID_AUTO_START)) { // 进入Android Auto
                mIsAndroidAutoStatus = true;
                if (mAndroidAutoListener != null) {
                    mAndroidAutoListener.onStartAuto();
                }
            } else if (TextUtils.equals(action, IVISystem.ACTION_ANDROID_AUTO_END)) { // 退出Android Auto
                mIsAndroidAutoStatus = false;
                if (mAndroidAutoListener != null) {
                    mAndroidAutoListener.onEndAuto();
                }
            }
        }
    }
}
