// IAudio.aidl
package com.roadrover.services.audio;
import com.roadrover.services.audio.IAudioCallback;

interface IAudio {
    /**
     * 注册IAudioCallback的回调对象
     * @param callback 回调对象
     */
	void registerCallback(IAudioCallback callback);
    /**
     * 反注册IAudioCallback的回调对象
     * @param callback 回调对象
     */
	void unRegisterCallback(IAudioCallback callback);

    /**
     * 指定参数是否有效
     * @param  id AudioParam.Id
     * @return true 有效，false 无效
     */
    boolean isParamAvailable(int id);
    /**
     * 获取指定参数的最小值
     * @param  id AudioParam.Id
     * @return 指定参数的最小值
     */
    int getParamMinValue(int id);
    /**
     * 获取指定参数的最大值
     * @param  id AudioParam.Id
     * @return 指定参数的最大值
     */
    int getParamMaxValue(int id);
    /**
     * 获取指定参数的默认值
     * @param  id AudioParam.Id
     * @return 指定参数的默认值
     */
    int getParamDefaultValue(int id);
    /**
     * 获取指定参数
     * @param  id AudioParam.Id
     * @return 指定参数
     */
    int getParam(int id);
    /**
     * 设置指定参数的值
     * @param  id   AudioParam.Id
     * @param value 指定参数的值
     */
    void setParam(int id, int value);
    /**
     * 获取十段EQ增益
     * @param  eqMode EQ模式的值
     * @return 十段EQ增益
     */
    int[] getEqGains(int eqMode);

    /**
     * 开发者接口，判断前置音频通道增益是否有效
     * @param  channel IVIAudio.Channel
     * @return true 有效，false无效
     */
    boolean isBuildInPreVolumeAvailable(int channel);
    /**
     * 开发者接口，获取前置音频通道增益的最小值
     * @param  channel IVIAudio.Channel
     * @return 前置音频通道增益的最小值
     */
    float getBuildInPreVolumeMinValue(int channel);
    /**
     * 开发者接口，获取前置音频通道增益的最大值
     * @param  channel IVIAudio.Channel
     * @return 前置音频通道增益的最大值
     */
    float getBuildInPreVolumeMaxValue(int channel);
    /**
     * 开发者接口，获取前置音频通道增益的默认值
     * @param  channel IVIAudio.Channel
     * @return 前置音频通道增益的默认值
     */
    float getBuildInPreVolumeDefaultValue(int channel);
    /**
     * 开发者接口，获取前置音频通道增益的值
     * @param  channel IVIAudio.Channel
     * @return 前置音频通道增益的值
     */
    float getBuildInPreVolumeValue(int channel);
    /**
     * 开发者接口，设置前置音频通道增益的值
     * @param channel IVIAudio.Channel
     * @param value 前置音频通道增益的值
     */
    void setBuildInPreVolumeValue(int channel, float value);
    /**
     * 开发者接口，重置所有前置音频通道增益的值
     */
    void resetBuildInPreVolumeValue();

    /**
     * 开发者接口，判断辅助音频通道增益是否有效
     * @param  channel IVIAudio.Channel
     * @return true 有效，false无效
     */
    boolean isSecondaryBuildInPreVolumeAvailable(int channel);
    /**
     * 开发者接口，获取辅助音频通道增益的最小值
     * @param  channel IVIAudio.Channel
     * @return 辅助音频通道增益的最小值
     */
    float getSecondaryBuildInPreVolumeMinValue(int channel);
    /**
     * 开发者接口，获取辅助音频通道增益的最大值
     * @param  channel IVIAudio.Channel
     * @return 辅助音频通道增益的最大值
     */
    float getSecondaryBuildInPreVolumeMaxValue(int channel);
    /**
     * 开发者接口，获取辅助音频通道增益的默认值
     * @param  channel IVIAudio.Channel
     * @return 辅助音频通道增益的默认值
     */
    float getSecondaryBuildInPreVolumeDefaultValue(int channel);
    /**
     * 开发者接口，获取辅助音频通道增益的值
     * @param  channel IVIAudio.Channel
     * @return 辅助音频通道增益的值
     */
    float getSecondaryBuildInPreVolumeValue(int channel);
    /**
     * 开发者接口，设置辅助音频通道增益的值
     * @param channel IVIAudio.Channel
     * @param value 辅助音频通道增益的值
     */
    void setSecondaryBuildInPreVolumeValue(int channel, float value);
    /**
     * 开发者接口，重置所有辅助音频通道增益的值
     */
    void resetSecondaryBuildInPreVolumeValue();

    /**
     * 开发者接口，判断主音频设备是否有效
     * @return true 有效，false无效
     */
    boolean isMasterAudioDeviceAvailable();
    /**
     * 开发者接口，获取音量增益的最小值
     * @return 音量增益的最小值
     */
    float getMasterVolumeGainMinValue();
    /**
     * 开发者接口，获取音量增益的最大值
     * @return 音量增益的最大值
     */
    float getMasterVolumeGainMaxValue();
    /**
     * 开发者接口，获取音量增益的默认值
     * @param volume 音量值
     * @return 音量增益的默认值
     */
    float getMasterVolumeGainDefaultValue(int volume);
    /**
     * 开发者接口，获取音量增益的值
     * @param volume 音量值
     * @return 音量增益的值
     */
    float getMasterVolumeGainValue(int volume);
    /**
     * 开发者接口，设置音量增益的值
     * @param volume 音量值
     * @param value 音量增益的值
     */
    void setMasterVolumeGainValue(int volume, float value);
    /**
     * 开发者接口，重置所有音量增益的值
     */
    void resetMasterVolumeGainValue();

    /**
     * 开发者接口，判断辅助音频设备是否有效
     * @return true 有效，false无效
     */
    boolean isSecondaryAudioDeviceAvailable();
    /**
     * 开发者接口，获取辅助音量增益的最小值
     * @return 辅助音量增益的最小值
     */
    float getSecondaryVolumeGainMinValue();
    /**
     * 开发者接口，获取辅助音量增益的最大值
     * @return 辅助音量增益的最大值
     */
    float getSecondaryVolumeGainMaxValue();
    /**
     * 开发者接口，获取辅助音量增益的默认值
     * @param volume 音量值
     * @return 辅助音量增益的默认值
     */
    float getSecondaryVolumeGainDefaultValue(int volume);
    /**
     * 开发者接口，获取辅助音量增益的值
     * @param  volume 音量值
     * @return 辅助音量增益的值
     */
    float getSecondaryVolumeGainValue(int volume);
    /**
     * 开发者接口，设置辅助音音量增益的值
     * @param volume 音量值
     * @param value 辅助音量增益的值
     */
    void setSecondaryVolumeGainValue(int volume, float value);
    /**
     * 开发者接口，重置所有辅助音量增益的值
     */
    void resetSecondaryVolumeGainValue();

    /**
     * 显示音量条
     */
    void showVolumeBar();

    /**
     * 隐藏音量条
     */
    void hideVolumeBar();

    /**
     *  如果音量条是显示的，则隐藏，反之显示
     */
    void toggleVolumeBar();

    /**
     * 显示次通道（后排或者乘客区）的音量条
     */
    void showSecondaryVolumeBar();

    /**
     * 获取当前激活的音量ID
     */
    int getActiveVolumeId();

    /**
     * 获取前置最小音量
     */
    int getMasterVolumeMin();

    /**
     * 获取前置最大音量
     */
    int getMasterVolumeMax();

    /**
     * 获取辅助最小音量
     */
    int getSecondaryVolumeMin();

    /**
     * 获取辅助最大音量
     */
    int getSecondaryVolumeMax();

    /**
     * 请求内部先静音，隔一段时候反静音
     * @param muteDurationMs 静音后反静音时间ms
     */
    void requestInternalShortMute(int muteDurationMs);

    /**
    * 在收音机、AV、TV、AUX、BC05的A2DP等场景下，有了导航提示音，此时的媒体音量应该下降，
    * 调用此函数来设置媒体音量的百分比
    * @param percent 100最大，0没有
    */
    void setAnalogMediaVolumePercent(int percent);

    /**
    * 获取当前主音频通道
    * @return IVIAudio.Channel
    */
    int getMasterAudioChannel();

    /**
    * 设置音频DSP的参数，专家音效畅友使用
    */
    void setChipParam(int chipId, int paramId, double v0, double v1, double v2, double v3);

    /**
    * 添加专家音效配置文件，将机器内部的音效文件替换成pathName文件内的内容
    * @param effect AudioParam.ExpertAudioEffect
    * @param pathName 音效文件路径全名
    * @param apply 是否立即起作用
    */
    void addExpertAudioEffect(int effect, String pathName, boolean apply);

    /**
    * 获取当前支持的专家音效场景列表
    * @return 数组内部存放 AudioParam.ExpertAudioEffect
    */
    int[] getAvailableExpertAudioEffects();

    /**
    * 获取专家音效文件名
    * @param effect AudioParam.ExpertAudioEffect
    * @return 文件路径全名
    */
    String getExpertAudioEffectFile(int effect);
}
