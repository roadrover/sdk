package com.roadrover.sdk.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.roadrover.sdk.system.IVISystem;

/**
 * 用来适配CarPlay的工具类
 */

public class CarPlayUtil extends BaseThreeAppUtil {

    /** 当前是否在CarPlay状态 */
    private boolean mIsCarPlayStatus = false;

    /**
     * CarPlay状态监听
     */
    public interface CarPlayListener {
        /**
         * CarPlay状态连接
         */
        void onStartCarPlay();

        /**
         * CarPlay断开
         */
        void onEndCarPlay();

        /**
         * CarPlay电话开始
         */
        void onStartPhone();

        /**
         * CarPlay电话结束
         */
        void onEndPhone();
    }
    /** CarPlay接口回调 */
    private CarPlayListener mCarPlayListener;

    public CarPlayUtil(@NonNull Context context) {
        super(context);

        // 注册监听
        init(IVISystem.ACTION_CARPLAY_START, IVISystem.ACTION_CARPLAY_END,
                IVISystem.ACTION_CARPLAY_PHONE_START, IVISystem.ACTION_CARPLAY_PHONE_END);
    }

    /**
     * 判断当前是否在CarPlay状态
     * @return
     */
    public boolean isCarPlayStatus() {
        return mIsCarPlayStatus;
    }

    /**
     * 设置CarPlay
     * @param listener
     */
    public void setCarPlayListener(CarPlayListener listener) {
        mCarPlayListener = listener;
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Logcat.d("action:" + action);
            if (TextUtils.equals(action, IVISystem.ACTION_CARPLAY_START)) { // 进入CarPlay
                mIsCarPlayStatus = true;
                if (mCarPlayListener != null) {
                    mCarPlayListener.onStartCarPlay();
                }
            } else if (TextUtils.equals(action, IVISystem.ACTION_CARPLAY_END)) { // 退出CarPlay
                mIsCarPlayStatus = false;
                if (mCarPlayListener != null) {
                    mCarPlayListener.onEndCarPlay();
                }
            } else if (TextUtils.equals(action, IVISystem.ACTION_CARPLAY_PHONE_START)) { // carPlay 电话开始
                if (mCarPlayListener != null) {
                    mCarPlayListener.onStartPhone();
                }
            } else if (TextUtils.equals(action, IVISystem.ACTION_CARPLAY_PHONE_END)) { // carPlay 电话结束
                if (mCarPlayListener != null) {
                    mCarPlayListener.onEndPhone();
                }
            }
        }
    }
}
