/*
 * @(#)Logcat.java	1.00 11/04/20
 *
 * Copyright (c) 2011-2013  New Element Inc. 
 * 9/10f, Building 2, Financial Base, No.6 Keyuan Road, 
 * Nanshan District, Shenzhen 518057
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * New Element Medical Equipment Technology Development CO., Ltd 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with New Element.
 */

package com.roadrover.sdk.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志管理类
 *
 * @version 1.0
 * 新建类
 *
 * @version 1.1
 * 增加方法名，行号的打印
 *
 * @version 1.2
 * 增加不需要TAG的打印方法
 * @date 2016/8/11
 *
 * @author bin.xie
 */
public final class Logcat {

    private static boolean LOG_PRINT_FLAG = true;

    private static final String TAG_PREFIX = "RRIVI";

    private Logcat() {
    }

    /**
     * 打印日志
     * @param msg
     */
    public static void v(String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(TAG_PREFIX, msg);
            } else {
                Log.v(TAG_PREFIX, elements[3].getFileName()
                        + "(" + elements[3].getLineNumber() + "):" + elements[3].getMethodName()
                        + ": " + msg);
            }
        }
    }

    public static void v(String TAG, String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(TAG_PREFIX + TAG, msg);
            } else {
                if (TextUtils.isEmpty(TAG)) {
                    TAG = elements[3].getFileName();
                }
                Log.v(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
            }
        }
    }

    public static void v(String TAG, String msg, Throwable tr) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(TAG_PREFIX, TAG + msg);
            } else {
                Log.v(TAG_PREFIX, TAG + elements[3].getFileName()
                        + "(" + elements[3].getLineNumber() + "):" + elements[3].getMethodName()
                        + ": " + msg);
            }
        }
    }

    /**
     * 只打印函数名和行号
     */
    public static void d() {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG_PREFIX, "");
            } else {
                Log.d(TAG_PREFIX, elements[3].getFileName() + "(" + elements[3].getLineNumber() + "):"
                        + elements[3].getMethodName());
            }
        }
    }

    /**
     * 打印调试信息
     * @param msg
     */
    public static void d(String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG_PREFIX, msg);
            } else {
                Log.d(TAG_PREFIX, elements[3].getFileName() + "(" + elements[3].getLineNumber() + "):"
                        + elements[3].getMethodName() + ": " + msg);
            }
        }
    }

    public static void d(String TAG, String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG_PREFIX, TAG + msg);
            } else {
                if (TextUtils.isEmpty(TAG)) {
                    TAG = elements[3].getClassName();
                }
                Log.d(TAG_PREFIX, TAG + elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
            }
        }
    }

    public static void d(String TAG, String msg, Throwable tr) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG_PREFIX, TAG + msg);
            } else {
                Log.d(TAG_PREFIX, TAG + elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg, tr);
            }
        }
    }

    /**
     * 打印log信息
     * @param msg
     */
    public static void i(String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(TAG_PREFIX, msg);
            } else {
                Log.i(TAG_PREFIX + elements[3].getClassName(), elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
            }
        }
    }

    public static void i(String TAG, String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(TAG_PREFIX + TAG, msg);
            } else {
                if (TextUtils.isEmpty(TAG)) {
                    TAG = elements[3].getClassName();
                }
                Log.i(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
            }
        }

    }

    public static void i(String TAG, String msg, Throwable tr) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(TAG_PREFIX + TAG, msg);
            } else {
                Log.i(TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg, tr);
            }
        }

    }

    /**
     * 警告信息
     * @param msg
     */
    public static void w(String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG_PREFIX, msg);
            } else {
                Log.w(TAG_PREFIX, elements[3].getFileName() + "(" + elements[3].getLineNumber() + "):"
                        + elements[3].getMethodName() + ": " + msg);
            }
        }
    }

    public static void w(String TAG, String msg) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG_PREFIX + TAG, msg);
            } else {
                if (TextUtils.isEmpty(TAG)) {
                    TAG = elements[3].getClassName();
                }
                Log.w(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
            }
        }
    }

    public static void w(String TAG, Throwable tr) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG_PREFIX + TAG, tr);
            } else {
                Log.w(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber(), tr);
            }
        }
    }

    public static void w(String TAG, String msg, Throwable tr) {
        if (LOG_PRINT_FLAG) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG_PREFIX + TAG, msg);
            } else {
                Log.w(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg, tr);
            }
        }
    }

    /**
     * 错误信息
     * @param msg
     */
    public static void e(String msg) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements == null || elements.length < 4) {
            Log.e(TAG_PREFIX, msg);
        } else {
            Log.e(TAG_PREFIX, elements[3].getFileName() + "(" + elements[3].getLineNumber() + "):"
                    + elements[3].getMethodName() + ": " + msg);
        }
    }

    public static void e(String TAG, String msg) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements == null || elements.length < 4) {
            Log.w(TAG_PREFIX + TAG, msg);
        } else {
            if (TextUtils.isEmpty(TAG)) {
                TAG = elements[3].getClassName();
            }
            Log.w(TAG_PREFIX + TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg);
        }
    }

    public static void e(String TAG, String msg, Throwable tr) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements == null || elements.length < 4) {
            Log.w(TAG_PREFIX + TAG, msg);
        } else {
            Log.w(TAG, elements[3].getMethodName() + " " + elements[3].getLineNumber() + " " +  msg, tr);
        }
    }

}