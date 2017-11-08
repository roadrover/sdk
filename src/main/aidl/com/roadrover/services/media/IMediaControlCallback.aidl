// IMediaControlCallback.aidl
package com.roadrover.services.media;

interface IMediaControlCallback {
    /**
    * 挂起媒体
    */
    void suspend();

    /**
    * 恢复媒体
    */
    void resume();

    void pause();
    void play();

    /**
    * 退出媒体应用
    */
    void quitApp();

    /**
    * 停止媒体
    */
    void stop();

    /**
    * 设置媒体音量，1最大，0最小
    */
    void setVolume(float volume);
    void next();
    void prev();

    /**
     * 选择第几首
    */
    void select(int index);

    /**
    * 设置播放模式
    * @param mode 对应 IVIVoice.MediaPlayMode 定义
    */
    void setPlayMode(int mode);

    /**
    * 随便听听
    */
    void playRandom();

    /**
    * 播放指定媒体，音乐或者视频
    * @param title 音乐或者视频名
    * @param singer 歌手名
    */
    void filter(String title, String singer);

    /**
    * 收藏当前播放的媒体项
    */
    void setFavour(boolean isFavour);

    /**
    * 视频是否被允许显示，比如手刹控制
    */
    void onVideoPermitChanged(boolean show);
}
