package com.roadrover.sdk.bluetooth;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.roadrover.sdk.utils.ListUtils;
import com.roadrover.sdk.utils.Logcat;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取蓝牙应用里面的数据.
 * 可以获取到电话本的数据，主要方式是通过aidl连接蓝牙app的数据库
 * 注：要使用该类，必须你的蓝牙app，要实现联系人数据库的ContentProvider
 */

public class BluetoothModel {

    /**
     * 联系人定义类
     */
    public static class Contact {
        public String mName; // 名字
        public String mPhoneNumber; // 电话号码
    }

    /**
     * 联系人的ContentProvider的数据接口
     */
    public static class Provider {
        private static final String AUTHORITY = "com.roadrover.bluetooth";
        public static final Uri URI = Uri.parse("content://"+ AUTHORITY +"/tbl_stPhoneBook");
        private static final String KEY_NAME = "name";
        private static final String KEY_PHONE_NUMBER = "phoneNumber";
    }

    private static final int EQUALS_PHONE_NUMBER_COUNT = 7; // 判断两个电话号码是否一样，只判断最后7位

    /**
     * 获取蓝牙联系人列表，该方法时直接获取蓝牙联系数据库的数据，不是从串口获取
     * @param context 上下文
     * @return 返回当前联系人列表
     */
    public static List<Contact> getContactList(Context context) {
        List<Contact> contacts = new ArrayList<>();
        if (context != null && checkValidProvider(Provider.URI, context)) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Provider.URI,
                        new String[]{Provider.KEY_NAME, Provider.KEY_PHONE_NUMBER},
                        null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do { // 获取蓝牙联系人数据
                        Contact contact = new Contact();
                        contact.mName = cursor.getString(cursor.getColumnIndex(Provider.KEY_NAME));
                        contact.mPhoneNumber = cursor.getString(cursor.getColumnIndex(Provider.KEY_PHONE_NUMBER));
                        contacts.add(contact);
                    } while (cursor.moveToNext());
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
        return contacts;
    }

    /**
     * 通过电话号码，获取联系人名字
     * @param phoneNumber 电话号码
     * @param contacts    联系人列表
     * @return 返回联系人名字
     */
    public static String getContactName(String phoneNumber, List<Contact> contacts) {
        if (!ListUtils.isEmpty(contacts) && !TextUtils.isEmpty(phoneNumber)) {
            for (Contact contact : contacts) {
                if (isPhoneNumberEquals(phoneNumber, contact.mPhoneNumber)) {
                    return contact.mName;
                }
            }
        }
        return "";
    }


    /**
     * 判断两个电话号码是否完全相等
     * @param src 源电话号码
     * @param dest 目标电话号码
     * @return 相等返回true，否则返回false
     */
    public static boolean isPhoneNumberEquals(String src, String dest) {
        if (src != null && dest != null) {
            String srcCompare = src.replace(" ", "");
            String destCompare = dest.replace(" ", ""); // 去掉空格之后再比较

            if (TextUtils.equals(srcCompare, destCompare)) { // 完全相等
                return true;
            }

            // 有些地区会在前面+86等，所以只判断最后7位
            if (srcCompare.length() >= EQUALS_PHONE_NUMBER_COUNT && destCompare.length() >= EQUALS_PHONE_NUMBER_COUNT) {
                String lastNumberCount = destCompare.substring(destCompare.length() - EQUALS_PHONE_NUMBER_COUNT);
                if (srcCompare.endsWith(lastNumberCount)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 监听数据库的改变接口定义
     */
    public interface ModelListener {
        void onChange(List<Contact> contacts);
    }

    private static ContentObserver mContentObserver = null;

    /**
     * 监听下载联系人的接口，语音等应用需要知道蓝牙联系人发生改变
     * @param context
     * @param listener
     */
    public static void registerContactListener(final Context context, final ModelListener listener) {
        if (context != null && listener != null) {
            if (mContentObserver != null) {
                return;
            }
            mContentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    Logcat.d("selfChange:" + selfChange);
                    if (!selfChange) {
                        if (listener != null) {
                            listener.onChange(getContactList(context));
                        }
                    }
                }
            };
            context.getContentResolver().registerContentObserver(Provider.URI, false, mContentObserver);
        }
    }

    /**
     * 不再监听联系人的改变消息
     * @param context
     */
    public static void unregisterContactListener(Context context) {
        if (context != null && mContentObserver != null) {
            context.getContentResolver().unregisterContentObserver(mContentObserver);
            mContentObserver = null;
        }
    }

    /**
     * 检测这个contentprovider是否合法
     *
     * @param uri
     * @return 合法返回true,否则返回false
     */
    private static boolean checkValidProvider(Uri uri, Context context) {
        if (null == context) {
            return false;
        }
        ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(uri);
        if (null == client) {
            return false;
        } else {
            client.release();
            return true;
        }
    }
}
