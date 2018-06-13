package com.roadrover.sdk.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * 与第三方应用通信的工具类 </br>
 * 一般第三方APP通讯都是通过广播，将广播流程封装
 */

public abstract class BaseThreeAppUtil {

    protected Context mContext;
    private boolean mIsRegister = false;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseThreeAppUtil.this.onReceive(context, intent);
        }
    };

    public BaseThreeAppUtil(@NonNull Context context) {
        mContext = context;
    }

    /**
     * 初始化监听
     * @param actions
     */
    public void init(String... actions) {
        if (mContext != null && !mIsRegister) {
            if (!ListUtils.isEmpty(actions)) {
                mIsRegister = true;
                IntentFilter filter = new IntentFilter();
                for (String action : actions) {
                    filter.addAction(action);
                }
                mContext.registerReceiver(mBroadcastReceiver, filter);
            }
        }
    }

    /**
     * 注销监听
     */
    public void release() {
        if (mIsRegister && mContext != null) {
            mIsRegister = false;
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    /**
     * 广播监听
     * @param context
     * @param intent
     */
    protected abstract void onReceive(Context context, Intent intent);
}
