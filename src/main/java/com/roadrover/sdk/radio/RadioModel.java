package com.roadrover.sdk.radio;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;

import com.roadrover.sdk.utils.Logcat;

import java.util.ArrayList;
import java.util.List;

/**
 * 收音机数据.
 */

public class RadioModel {

    public static class Station {
        public String mName; // 名字
        public int mFreq; // 频率
        public int mKind; // 频率
        public int mBand; // 波段，取值见{@link IVIRadio#Band}
        public int mPosition;//
        public int mIsLove;
        public String mDesp;//

        @Override
        public String toString() {
            return "mName = " + mName + ", mFreq = " + mFreq +
                    ", mKind = " + mKind + ", mBand = " + mBand +
                    ", mPosition = " + mPosition + ", mIsLove = " + mIsLove +
                    ", mDesp = " + mDesp;
        }
    }

    /**
     * 获取收音机站台频率列表，该方法时直接获取收音机数据库的数据
     *
     * @param context
     * @return
     */
    public static List<RadioModel.Station> getStationList(Context context) {
        List<RadioModel.Station> stations = new ArrayList<>();
        if (context != null && checkValidProvider(Provider.TableColumns.CONTENT_URI, context)) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Provider.TableColumns.CONTENT_URI,
                        new String[]{Provider.TableColumns.KEY_ID
                                , Provider.TableColumns.KEY_NAME, Provider.TableColumns.KEY_FREQ
                                , Provider.TableColumns.KEY_DESP, Provider.TableColumns.KEY_BAND
                                , Provider.TableColumns.KEY_KIND, Provider.TableColumns.KEY_POSITION
                                , Provider.TableColumns.KEY_ISLOVE},
                        null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do { // 获取收音机站台频率数据
                        RadioModel.Station station = new RadioModel.Station();
                        station.mName = cursor.getString(cursor.getColumnIndex(Provider.TableColumns.KEY_NAME));
                        station.mFreq = cursor.getInt(cursor.getColumnIndex(Provider.TableColumns.KEY_FREQ));
                        station.mDesp = cursor.getString(cursor.getColumnIndex(Provider.TableColumns.KEY_DESP));
                        station.mBand = cursor.getInt(cursor.getColumnIndex(Provider.TableColumns.KEY_BAND));
                        station.mKind = cursor.getInt(cursor.getColumnIndex(Provider.TableColumns.KEY_KIND));
                        station.mPosition = cursor.getInt(cursor.getColumnIndex(Provider.TableColumns.KEY_POSITION));
                        station.mIsLove = cursor.getInt(cursor.getColumnIndex(Provider.TableColumns.KEY_ISLOVE));
                        stations.add(station);
                        Logcat.d("station = " + station.toString());
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
        return stations;
    }

    public interface ModelListener {
        void onChange(List<RadioModel.Station> stations);
    }

    private static ContentObserver mContentObserver = null;

    /**
     * 监听收音站台频率的接口，语音等应用需要知道收音站台频率发生改变
     *
     * @param context
     * @param listener
     */
    public static void registerStationListener(final Context context, final ModelListener listener) {
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
                            listener.onChange(getStationList(context));
                        }
                    }
                }
            };
            context.getContentResolver().registerContentObserver(Provider.TableColumns.CONTENT_URI, false, mContentObserver);
        }
    }

    /**
     * 不再监听收音站台频率的改变消息
     *
     * @param context
     */
    public static void unregisterStationListener(Context context) {
        if (context != null && mContentObserver != null) {
            context.getContentResolver().unregisterContentObserver(mContentObserver);
            mContentObserver = null;
        }
    }

    /**
     * 检测这个contentprovider是否合法
     *
     * @param uri
     * @return 合法返回true, 否则返回false
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
