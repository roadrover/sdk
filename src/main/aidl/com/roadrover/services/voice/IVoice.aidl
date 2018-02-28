// IVoice.aidl
package com.roadrover.services.voice;

import com.roadrover.services.voice.IVoiceCallback;

// 语音控制接口

interface IVoice {
    // 媒体接口
    void selectMediaItem(int index);
    void playMedia(String title, String singer);
    void play();
    void pause();
    void prev();
    void next();
    void favourMedia(boolean isFavour);

    /**
    * 设置播放模式
    * @param mode 对应 IVIVoice.MediaPlayMode 定义
    */
    void setPlayMode(int mode);
    void playRandom();

    // 声音控制
    void incVolume();
    void decVolume();
    void setVolume(int volume);
    void setMute(boolean isMute);

    // 收音机接口
    void setFreq(int freq);
    void startScan();
    void stopScan();

    /**
     * 设置FM 还是 Am
     * @param band 对应 IVIRadio.Band
     */
    void setBand(int band);

    // 应用操作接口
    /**
     * 打开应用
     * @param packageName 包名
     * @param className 类名，可以缺省
     */
    void openApp(String packageName, String className);
    void closeApp(String packageName);

    // 系统操作相关接口
    void openScreen();
    void closeScreen();

    // 打开/关闭语音app
    void openVoiceApp();
    void closeVoiceApp();

    // 以下两个接口，在语音播放声音的时候调用 startVoice，在语音停止播报的时候调用 endVoice
    void startVoice();
    void endVoice();

    // 注册语音回调
    void registerVoiceCallback(IVoiceCallback callback);
    void unregisterVoiceCallback(IVoiceCallback callback);
}
