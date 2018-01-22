// IDAB.aidl
package com.roadrover.services.dab;

import com.roadrover.services.dab.IDABCallback;

interface IDAB {
    /**
     * 打开设备
     * @param callback 回调接口
     */
	void open(IDABCallback callback);

    /**
     * 关闭设备
     */
    void close();

    /**
     * 获取中间件版本
     */
    String getMiddlewareVersion();

    /**
     * 获取固件版本
     */
    String getSWVersion();

    /**
     * 获取硬件版本
     */
    String getHWVersion();

    /**
     * 单个调谐器系统以触发全频段扫描
     */
    void triggerBandScan();
}
