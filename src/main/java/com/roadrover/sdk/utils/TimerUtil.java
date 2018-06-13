package com.roadrover.sdk.utils;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

/**
 * 安卓的定时器
 * @使用方法 TimerUtil timer TimerUtil(callback, isNeedHandler);
 *         isNeedHandler 该变量传false，回调函数在次线程执行，不能刷新UI
 *                       该变量传true, 回调函数在主线程执行，可以刷新UI
 *         start(传定时时间); 当定时结束时，需要调用 stop();尤其是Activity的销毁，必须调用stop()
 *        如果只是做一次的延时处理，可以使用Handler的sendMessageDelayed处理
 * @author bin.xie
 * @修改时间 2015/7/22
 */

public class TimerUtil {
    public interface TimerCallback {
        /**
         * 定时器时间到了，进行回调操作
         */
        void timeout();
    }

    private TimerCallback mTimerCallback = null; // 定时器时间到了的回调

    private final static int TIMER_MESSAGE_HANDLE = 0; // 定时器到了，发送消息
    private ScheduledFuture mScheduledFuture = null;
    private TimerTask mTimerTask = null;
    private Handler mHandler = null;

    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor ;

    private boolean mIsNeedHandler = true; // 是否需要通过handler转换到主线程，如果需要直接UI操作，需要该操作位true

    public TimerUtil(TimerCallback callback) {
        this(callback, true);
    }

    public TimerUtil(TimerCallback callback, boolean isNeedHandler) {
        mTimerCallback = callback;

        mIsNeedHandler = isNeedHandler;

        mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        if (isNeedHandler) {
            mHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (TIMER_MESSAGE_HANDLE == msg.what) {
                        if (null != mTimerCallback) {
                            mTimerCallback.timeout();
                        }
                    }
                }
            };
        }
    }

    private void initTimerTask() {
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                if (mIsNeedHandler) {
                    // 定时器中不能刷新界面，所以发送message
                    postHandler();
                } else {
                    // 不需要handler转换，直接调用
                    if (null != mTimerCallback) {
                        mTimerCallback.timeout();
                    }
                }
            }
        };
    }

    /**
     * 开始定时器
     * @param nMesc 多长时间触发一次
     *        如果传小于等于0的数，仅且只会调用一次timeout()
     */
    public void start(int nMesc) {
        stop();

        if (0 >= nMesc) { // 时间如果为0，不开启定时器，
            if (null != mHandler) { // 直接post一下
                postHandler();
            } else {
                if (null != mTimerCallback) { // 直接调用结果，并且只会调用一次
                    mTimerCallback.timeout();
                }
            }
            return;
        }

        initTimerTask();

        mScheduledFuture = mScheduledThreadPoolExecutor.scheduleAtFixedRate(mTimerTask, nMesc,
                nMesc, TimeUnit.MILLISECONDS);

    }

    /**
     * 停止定时器
     */
    public void stop() {
        try {
            if (null != mScheduledFuture) {
                mScheduledFuture.cancel(true);
                mScheduledFuture = null;
            }

            if (null != mTimerTask) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时器是否为激活状态
     * @return 激活状态返回true
     */
    public boolean isActive() {
        return (mScheduledFuture != null);
    }

    private void postHandler() {
        Message msg = mHandler.obtainMessage();
        msg.what = TIMER_MESSAGE_HANDLE;
        mHandler.sendMessage(msg);
    }
}
