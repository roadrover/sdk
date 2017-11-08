// IMediaScannerCallback.aidl
package com.roadrover.services.media;

// 媒体扫描回调

interface IMediaScannerCallback {

    /**
     * 扫描开始
     * @param type 对应 Media.MediaScannerType 中类型
     * @param sqlType {@link Media.MediaSqlDataType}，只有当 type 为 删除，重命名，创建 类型时才有效
     */
    void onScanStart(int type, int sqlType);

    /**
     * 扫描结束
     * @param type 对应 Media.MediaScannerType 中类型
     * @param sqlType {@link Media.MediaSqlDataType}，只有当 type 为 删除，重命名，创建 类型时才有效
     * @param path 当前操作的目录，如果是扫描全部，为空
     * @param oldPath 当操作是重命名的时候，才会有该参数，其他类型不需要关心该参数
     */
    void onScanFinish(int type, int sqlType, String path, String oldPath);

    /**
     * 盘符卸载
     * @param path 卸载目录
     * @param isDiskPowerDown 是否是盘符掉电，如果是盘符掉电，音乐，视频等不做处理，但是状态栏需要处理
     */
    void onEject(String path, boolean isDiskPowerDown);

    /**
     * 盘符挂载
     * @param path 挂载目录
     */
    void onMount(String path);
}
