package com.roadrover.sdk.radio;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量.
 */

public class Provider {

    // 这个是每个Provider的标识，在Manifest中使用
    public static final String AUTHORITY = "com.roadrover.radio_v2.provider";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.roadrover.radio_v2";

    /**
     * 跟表相关的常量
     */
    public static final class TableColumns implements BaseColumns {
        // CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表
        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/provider");
        public static final String TABLE_NAME = "tbRadio";
        public static final String DEFAULT_SORT_ORDER = "_id desc";

        public static final String KEY_ID = "_id";
        public static final String KEY_NAME = "name";
        public static final String KEY_FREQ = "mFreq";
        public static final String KEY_KIND = "kind";
        public static final String KEY_BAND = "mBand";
        public static final String KEY_POSITION = "position";
        public static final String KEY_RDSNAME = "rdsname";
        public static final String KEY_ISLOVE = "isLove";
        public static final String KEY_DESP = "desp";

    }
}
