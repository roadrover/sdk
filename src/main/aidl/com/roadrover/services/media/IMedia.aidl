// IMedia.aidl
package com.roadrover.services.media;

import com.roadrover.services.media.IGetMediaListCallback;
import com.roadrover.services.media.IMediaControlCallback;
import com.roadrover.services.media.IMediaInfoCallback;
import com.roadrover.services.media.IMediaScannerCallback;
import com.roadrover.services.media.IMusicControlCallback;

interface IMedia {

    /**
     * 打开媒体
     * @param type 媒体类型
     * @param callback 回调接口
     */
    void open(int mediaType, IMediaControlCallback callback, String packageName);

    /**
     * 关闭媒体
     * @param type 对应 IVIMedia.Type中的类型
     */
    void close(int mediaType);

    /**
     * 注册媒体扫描的回调
     * @param callback 媒体扫描回调接口对象
     */
    void registerScannerCallback(IMediaScannerCallback callback);

    /**
     * 注销媒体扫描回调
     * @param callback 媒体扫描回调接口对象
     */
    void unRegisterScannerCallback(IMediaScannerCallback callback);

    /**
     * 注册媒体数据监听的回调，主要在小屏或者主界面等希望获取到当前多媒体数据的应用使用
     * @param callback 媒体数据回调
     */
    void registerMediaInfoCallback(IMediaInfoCallback callback);

    /**
     * 注销媒体数据
     */
    void unRegisterMediaInfoCallback(IMediaInfoCallback callback);

    /**
     * 得到当前播放的媒体
     * @return IVIMedia.Type中的类型
     */
    int getActiveMedia();

    /**
     * 播放音频，视频
     * @param mediaType 媒体类型
     * @param name 播放名字信息
     * @param info 播放的信息，一般为歌手等
     * @param artWidth 图片(比如专辑封面)的宽度，单位像素
     * @param artHeight 图片的高度，单位像素
     * @param artPixels 图片的bytes，比如专辑封面
     * @param index 当前是第几首
     * @param totalCount 当前歌曲总数
     * @param popup 是否弹出小窗口显示媒体信息，一般上下曲的时候为true，其他为false
     */
    void setMediaInfo(int mediaType, String name, String info, int artWidth, int artHeight, in byte[] artPixels, int index, int totalCount, boolean popup);

    /**
     * 设置当前进度
     * @param playState 播放状态，参考IVIMedia.MediaState里面的定义
     * @param position 当前进度s
     * @param duration 总时间s
     */
    void setMediaState(int mediaType, int playState, int position, int duration);

    /**
     * 获取该目录下的所有媒体列表，包含子目录
     * @param path 该目录，以及子目录的媒体数据
     */
    void getAllMediaList(String type, String startWidtPath, IGetMediaListCallback callback);

    /**
     * 获取指定目录的媒体信息，不会获取子目录的媒体
     */
    void getAppointPathMediaList(String type, String path, IGetMediaListCallback callback);

    /**
    * 是否允许播放视频
    */
    boolean isVideoPermit();

    /**
    * 反注册MediaControlCallback，断开连接的时候调用
    */
    void unRegisterMediaControlCallback(IMediaControlCallback callback);

    /**
    * 是否允许播放媒体，在倒车、蓝牙等状态下不能恢复播放媒体
    */
    boolean canPlayMedia();

    /**
    * 控制当前媒体：播放
    */
    void play();

    /**
    * 控制当前媒体：暂停
    */
    void pause();

    /**
    * 控制当前媒体：播放和暂停的循环
    */
    void playPause();

    /**
    * 控制当前媒体：上一曲
    */
    void prev();

    /**
    * 控制当前媒体：下一曲
    */
    void next();

    /**
     * 拖动当前音乐的进度
     * param msec 单位毫秒
     */
    void seekTo(int msec);

    /**
    * 打开媒体UI（APK）
    */
    void launchApp(int mediaType);

    /**
    * 请求service发送当前媒体信息和播放状态消息
    */
    void requestMediaInfoAndStateEvent();

    /**
     * 打开媒体，双区媒体专用
     * @param mediaType 媒体类型
     * @param callback 回调接口
     * @param index 媒体索引号，双区系统从0开始，支持0到1
     * @param zone IVIMedia.Zone
     * @param stream Android的Stream，比如STREAM_MUSIC
     */
    void openMediaInZone(int mediaType, IMediaControlCallback callback, String packageName, int zone);

    /**
    *  得到双区媒体某个区域的媒体
    *  @param zone IVIMedia.Zone，只支持MASTER和SECONDARY
    *  @return IVIMedia.Type中的类型
    */
    int getMediaFromZone(int zone);

    /**
    * 设置媒体输出区
    * @param mediaType IVIMedia.Type
    * @param zone IVIMedia.Zone
    */
    void setMediaZone(int mediaType, int zone);

    /**
    * 得到媒体的输出区
    * @param mediaType IVIMedia.Type
    * @return IVIMedia.Zone
    */
    int getMediaZone(int mediaType);

    /**
    * 选择某个媒体输出区是Enable还是Disable，必须在open mediaType之后才起作用
    * @param mediaType IVIMedia.Type, 只支持MASTER和SECONDARY，
    * @param targetZone IVIMedia.Zone 目的Zone
    * @param enable true 打开，false 关闭
    */
    void switchMediaZone(int mediaType, int targetZone, boolean enable);

    /**
    * 判断某个媒体是否被打开
    * @param mediaType IVIMedia.Type
    * @return true 被打开
    */
    boolean isOpened(int mediaType);

    /**
     * 删除文件 </br>
     * 部分平台刚刚插入U盘时，删除文件，通过MultiFileObserver没办法监听到，所以增加接口，在删除文件时通知services </br>
     * 目前IMX6存在该问题，T3不存在 </br>
     * @param path 文件路径
     */
    void sendDeleteFileEvent(String path);

    /**
     * 文件写入完成 </br>
     * 部分平台刚刚插入U盘时，拷贝文件，通过MultiFileObserver没办法监听到，所以增加接口，在拷贝文件，写入文件时候通知Services </br>
     * @param path 文件路径
     */
    void sendWriteFinishedEvent(String path);

    /**
     * 注册语音控制音乐的回调接口 </br>
     * 与 IMediaCtonrlCallback 区别在于，该接口注册后，及时当前不在音频焦点状态，也可以获取到回调信息 </br>
     */
    void registerMusicControlCallback(IMusicControlCallback callback);

    /**
     * 注销语音控制音乐的回调接口
     */
    void unregisterMusicControlCallback(IMusicControlCallback callback);

    /**
     * 设置当前用于显示的媒体类型信息
     * 用于设置当前不能作为媒体源的媒体类型信息供三方使用，如图库等。
     */
    void setCurrentShownMediaType(int meidaType);
}
