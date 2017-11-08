package com.roadrover.sdk.setting;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 存放跟数据库有关的常量
 */

public class Provider {
    /**这个是每个Provider的标识，在Manifest中使用, 其值 {@value}*/
    public static final String AUTHORITY = "com.roadrover.settings.provider";
    /**内容类型, 其值 {@value}*/
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.roadrover.settings";
    /**内容单项类型, 其值 {@value}*/
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.roadrover.settings";

    /**
     * 跟NameValue表相关的常量
     */
    public static final class NameValueColumns implements BaseColumns {
        /**CONTENT_URI跟数据库的表关联，最后根据CONTENT_URI来查询对应的表, 其值 {@value}*/
        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/settings");
        /**表名字, 其值 {@value}*/
        public static final String TABLE_NAME = "settings";
        /**排序, 其值 {@value}*/
        public static final String DEFAULT_SORT_ORDER = "_id desc";
        /**名字, 其值 {@value}*/
        public static final String NAME = "name";
        /**数值, 其值 {@value}*/
        public static final String VALUE = "value";

    }
}
