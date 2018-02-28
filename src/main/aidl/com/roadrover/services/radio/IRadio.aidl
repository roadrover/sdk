package com.roadrover.services.radio;

import com.roadrover.services.radio.IRadioCallback;

interface IRadio {
    /**
     * 打开设备
     * @param callback 回调接口
     */
	void open(IRadioCallback callback, String packageName);
    
    /**
     * 关闭设备
     */
    void close();
    
    /**
	 * 设置频率
	 * @param freq 当前频率点
	 */
    void setFreq(int freq);

    /**
  	 * 获取当前频率
  	 */
    int getFreq();
    
    /**
	 * 设置 fm, am
	 * @param band
	 */
	void setBand(int band);

	/**
	 * 获取当前是fm还是am
	 */
	int getBand();

    /**
 	 * 设置 区域
 	 * @param location
 	 */
 	void setLocation(int location);

    /**
	 * 上搜索
	 * @param freqStart 搜索的起始位置
	 */
    void scanUp(int freqStart);
    
    /**
	 * 下搜索
	 * @param freqStart 搜索的起始位置
	 */
    void scanDown(int freqStart);

 	/**
     * 全局搜索
     */
 	void scanAll();

    /**
	 * 停止搜索
	 */
	boolean scanStop();
	
 	/**
     * 向上/下调一步频率
     * @param direction 1:向上，-1：向下
     */
 	void step(int direction);

  	/**
     * 得到收音机的设备ID
     */
    int getId();

    /**
     * RDS 打开或者关闭TA
     */
    void selectRdsTa(boolean on);

    /**
     * RDS 打开或者关闭AF
     */
    void selectRdsAf(boolean on);

    /**
     * RDS 选择PTY， PTY_UNKNOWN : 取消选择
     */
    void selectRdsPty(int pty);

    /**
     * RDS 选择是否只收听包含TP的节目
     */
    void selectRdsTp(boolean on);

    /**
     * 设置媒体信息，电台名或者RDS PS
     * @param popup 是否弹出媒体信息小窗口
     */
    void setStationDisplayName(String name, boolean popup);

    /**
     * 选择收音机声音输出区
     * @param zone IVIMedia.Zone
     */
    void setZone(int zone);

    /**
     * 获取收音机声音输出区
     * @param zone IVIMedia.Zone
     */
    int getZone();

    /**
     * 打开收音机双区媒体专用
     * @param callback 回调接口
     * @param zone IVIMedia.Zone
     */
    void openInZone(IRadioCallback callback, String packageName, int zone);

    /**
     * 获取PS文本
     * @param freq 指定频率
     */
    String getPSText(int freq);

    /**
     * 获取搜台动作
     */
    int getScanAction();

    /**
     * 设置FM搜台条件，开发者工具使用，协助硬件、销售支持、业务等部门现场调解搜台参数
     * @param usn UltraSonic Noise，超声波噪声，范围 0-100.0f
     * @param wam Wideband AM ，范围 0-100.0f
     * @param offset level-offset 电平偏移可用于校正天线增益，范围 0-100.0f
     * @param bw IF bandwidth 中频带宽，范围0-100.0f
     * @param autoLevel 自动搜台门限 范围 0-255.0f
     * @param manualLevel 手动搜台门限 范围 0-255.0f
     */
    void setFMScanCondition(float usn, float wam, float offset, float bw, float autoLevel, float manualLevel);

    /**
     * 获取FM搜台条件
     * @return float[0]:usn
     *         float[1]:wam
     *         float[2]:offset
     *         float[3]:bw
     *         float[4]:autoLevel
     *         float[5]:manualLevel
     */
    float[] getFMScanConditions();

    /**
     * 设置AM搜台条件，开发者工具使用，协助硬件、销售支持、业务等部门现场调解搜台参数
     * @param offset level-offset 电平偏移可用于校正天线增益，范围 0-100.0f
     * @param bw IF bandwidth 中频带宽，范围0-100.0f
     * @param autoLevel 自动搜台门限 范围 0-255.0f
     * @param manualLevel 手动搜台门限 范围 0-255.0f
     */
    void setAMScanCondition(float offset, float bw, float autoLevel, float manualLevel);

    /**
     * 获取AM搜台条件
     * @return float[0]:offset
     *         float[1]:bw
     *         float[2]:autoLevel
     *         float[3]:manualLevel
     */
    float[] getAMScanConditions();
}