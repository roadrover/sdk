package com.roadrover.services.radio;

interface IRadioCallback {
    /**
     * 收音频率发生变化
     * @param freq 收音频率
     */
    void onFreqChanged(int freq);
    /**
     * 搜索电台结果信息回调
     * @param freq           收音频率
     * @param signalStrength 信号强度
     */
    void onScanResult(int freq, int signalStrength);
    /**
     * 收音开始搜索
     * @param isScanAll true全部搜索，false上下扫台
     */
    void onScanStart(boolean isScanAll);
    /**
     * 收音搜索正常结束
     * @param isScanAll true全部搜索，false上下扫台
     */
    void onScanEnd(boolean isScanAll);
    /**
     * 收音搜索异常结束（被打断）
     * @param isScanAll true全部搜索，false上下扫台
     */
    void onScanAbort(boolean isScanAll);
    /**
     * 收音当前信号强度更新（不在搜台状态下）
     * @param freq           收音频率
     * @param signalStrength 信号强度
     */
    void onSignalUpdate(int freq, int signalStrength);
    /**
     * 挂起
     */
    void suspend();
    /**
     * 恢复
     */
    void resume();
    /**
     * 暂停
     */
    void pause();
    /**
     * 播放
     */
    void play();
    /**
     * 播放暂停
     */
    void playPause();
    /**
     * 停止
     */
    void stop();
    /**
     * 下一曲
     */
    void next();
    /**
     * 上一曲
     */
    void prev();
    /**
     * 退出应用
     */
    void quitApp();
    /**
     * 选择指定index的电台进行收听
     */
    void select(int index);
    /**
     * 设置收藏/取消收藏频点
     * @param isFavour true收藏，false取消收藏
     */
    void setFavour(boolean isFavour);

    /**
     * RDS，Program Service信息发生变化
     * @param pi   PI码
     * @param freq 收音频率
     * @param ps   PS字符串，最多8个字符
     */
    void onRdsPsChanged(int pi, int freq, String ps);

    /**
     * RDS，Radio Text信息发生变化
     * @param pi   PI码
     * @param freq 收音频率
     * @param rt   RT字符串，最多64个字符
     */
    void onRdsRtChanged(int pi, int freq, String rt);

    /**
     * RDS，mask，包括TP, TA, PTY等
     * @param pi   PI码
     * @param freq 收音频率
     * @param pty  PTY码
     * @param tp   TP
     * @param ta   TA
     */
    void onRdsMaskChanged(int pi, int freq, int pty, int tp, int ta);

    /**
     * 频率调节旋钮
     * @param add 顺时针，一般为增加频率
     */
    void onTuneRotate(boolean add);
}
