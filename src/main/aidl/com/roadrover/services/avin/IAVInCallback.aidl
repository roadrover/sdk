package com.roadrover.services.avin;

interface IAVInCallback {
    /**
     * 视频信号发生变化
     * @param avId   IVIAVIn.Id
     * @param signal IVIAVIn.Signal
     */
    void onVideoSignalChanged(int avId, int signal);
    /**
     * 视频是否允许观看
     * @param show  true允许观看，false不允许观看
     */
    void onVideoPermitChanged(boolean show);
    /**
     * 停止
     */
    void stop();
    /**
     * 恢复播放
     */
    void resume();
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
     * 选择指定下标播放
     * @param index 指定的下标
     */
    void select(int index);
    /**
     * 视频CVBS制式发生变化
     * @param avId     IVIAVIn.Id
     * @param cvbsType VideoParam.CvbsType
     */
    void onCvbsTypeChanged(int avId, int cvbsType);
    /**
     * AVIN源插入状态发生变化，比如导游麦克风
     * @param avId     IVIAVIn.Id
     * @param plugin   true插入，false拔出
     */
    void onSourcePluginChanged(int avId, boolean plugin);
}