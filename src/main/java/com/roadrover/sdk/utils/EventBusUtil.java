package com.roadrover.sdk.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * event bus util class.
 */

public class EventBusUtil {

    private EventBus mEventBus; // 封装的EventBus对象

    /**
     * Eventbus封装类构造
     * @param useDefaultEventBus 是否使用默认的EventBus，如果需要不使用默认的EventBus，可以使用false
     */
    public EventBusUtil(Object object, boolean useDefaultEventBus) {
        if (useDefaultEventBus) {
            mEventBus = EventBus.getDefault();
        } else {
            mEventBus = new EventBus();
        }
        register(object);
    }

    /**
     * 销毁
     */
    public void destroy(Object object) {
        unregister(object);
        mEventBus = null;
    }

    /**
     * 注册eventBus的监听
     * @param object
     */
    public void register(Object object) {
        try {
            if (mEventBus != null && !mEventBus.isRegistered(object)) {
                mEventBus.register(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销eventBus的监听
     * @param object
     */
    public void unregister(Object object) {
        try {
            if (mEventBus != null && mEventBus.isRegistered(object)) {
                mEventBus.unregister(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否注册
     * @return
     */
    public boolean isRegistered(Object object) {
        if (mEventBus != null) {
            try {
                return mEventBus.isRegistered(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 发送一个消息
     * @param object
     */
    public void post(Object object) {
        if (mEventBus != null) {
            try {
                mEventBus.post(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送一个粘性消息
     * @param object
     */
    public void postSticky(Object object) {
        if (mEventBus != null) {
            try {
                mEventBus.postSticky(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
