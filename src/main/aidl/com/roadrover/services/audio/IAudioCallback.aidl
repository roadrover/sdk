package com.roadrover.services.audio;

interface IAudioCallback {
    /**
    * 音量值被改变
    * @param id 比如 AudioParam.Id.VOLUME_MASTER, AudioParam.Id.VOLUME_BLUETOOTH
    * @param value 具体的音量值
    */
    void onVolumeChanged(int id, int value);

    /**
    * 静音状态被改变
    * @param mute 是否被静音
    * @param source 静音动作的来源
    */
    void onMuteChanged(boolean mute, int source);

    /**
     * 音量条的显示和隐藏, id 为AudioParam.Id.NONE时为隐藏
     * 只有SystemUI才关心该方法，其他应用不需要关心
     */
    void onVolumeBar(int id, int value, int maxValue);

    /**
    * 次通道静音状态被改变
    * @param mute 是否被静音
    */
    void onSecondaryMuteChanged(boolean mute);
}