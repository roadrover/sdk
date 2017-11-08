package com.roadrover.services.avin;

import com.roadrover.services.avin.IAVInCallback;

interface IAVIn {
    /**
     * 打开AVIn
     * @param avId          IVIAVIn.Id
     * @param callback      回调接口
     * @param packageName   包名
     */
	void open(int avId, IAVInCallback callback, String packageName);

    /**
     * 关闭AVIn
     * @param avId  IVIAVIn.Id
     */
    void close(int avId);

    /**
     * 检查AVIn设备是否为打开状态
     * @param avId  IVIAVIn.Id
     */
    boolean isOpen(int avId);

    /**
     * 指定ID是否有效
     * @param id   由avId和subId 组合而成 VideoParam#makeId
     */
    boolean isParamAvailable(int id);

    /**
     * 获取指定ID的最小值
     * @param id   由avId和subId 组合而成 VideoParam#makeId
     */
    int getParamMinValue(int id);

    /**
     * 获取指定ID的最大值
     * @param id   由avId和subId 组合而成 VideoParam#makeId
     */
    int getParamMaxValue(int id);

    /**
     * 获取指定ID的默认值
     * @param id   由avId和subId 组合而成 VideoParam#makeId
     */
    int getParamDefaultValue(int id);

    /**
     * 获取指定ID的值
     * @param id   由avId和subId 组合而成 VideoParam#makeId
     */
    int getParam(int id);

    /**
     * 设置视频参数
     * @param id    由avId和subId 组合而成 VideoParam#makeId
     * @param value 参数的值
     */
    void setParam(int id, int value);

    /**
     * 获取视频信号
     * @param avId   IVIAVIn.Id
     */
    int getVideoSignal(int avId);

    /**
     * 是否允许播放视频
     */
    boolean isVideoPermit();

    /**
     * 反注册AVIn回调
     * @param callback  IAVInCallback回调对象
     */
    void unRegisterCallback(IAVInCallback callback);

    /**
     * 通知Service，应用即将要打开Android的摄像头
     * @param avId   IVIAVIn.Id
     */
    void setAndroidCameraOpenPrepared(int avId);

    /**
     * 通知Service，应用已经打开或者关闭了Android的摄像头
     * @param avId IVIAVIn.Id
     * @param isOpen true 打开状态，false 关闭状态
     */
    void setAndroidCameraOpen(int avId, boolean isOpen);

    /**
     * 获取AVIN源的插入状态，目前只支持导游麦克风插入状态
     * @param avId   IVIAVIn.Id
     */
    boolean getSourcePlugin(int avId);

    /**
     * 临时开始播放avId的声音，不作为媒体
     * @param avId AV ID
     * @param zone IVIMedia.Zone
     * @param mix 是否混音，
     *   true：混音，媒体降低音量，类似导航提示音
     *   false：不混音，暂停当前媒体，类似电话和声控提示音
     */
    void startTempSound(int avId, int zone, boolean mix);

    /**
     * 结束临时播放的avId声音
     * @param avId   IVIAVIn.Id
     */
    void endTempSound(int avId);

    /**
     * 注册AVIN callback，使得不打开avin也可以检测到一些消息
     * @param callback IAVInCallback回调对象
     */
    void registerCallback(IAVInCallback callback);

    /**
     * 控制TV
     * @param control 控制码，见{com.roadrover.sdk.avin.IVITV.Control}
     */
    boolean controlTV(int control);

    /**
     * 发送触摸坐标，范围{255， 255}
     * @param x X坐标
     * @param y Y坐标
     */
    boolean sendTouch(int x, int y);
}