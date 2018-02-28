// IMediaControlCallback.aidl
package com.roadrover.services.media;

/**
 * 该接口音乐app使用，语音控制时，及时当前焦点不在音乐，音乐也可以收到该接口回调
 */
interface IMusicControlCallback {

    /**
     * 设置播放模式 </br>
     * @param mode 对应 IVIVoice.MediaPlayMode 定义
     */
    void setPlayMode(int mode);

    /**
     * 随便听听 </br>
     */
    void playRandom();

    /**
     * 播放指定媒体，音乐或者视频 </br>
     * @param title 音乐或者视频名
     * @param singer 歌手名
     */
    void filter(String title, String singer);
}
