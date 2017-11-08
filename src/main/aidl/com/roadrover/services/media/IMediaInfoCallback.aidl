package com.roadrover.services.media;

// 媒体信息监听回调
interface IMediaInfoCallback {

    /**
     * 媒体发生改变，例：切歌，切视频，切收音
     * @param mediaType 对应Media.MediaType
     * @param name 名字
     * @param info 一般为歌手等附带信息
     * @param artWidth 图片(比如专辑封面)的宽度，单位像素
     * @param artHeight 图片的高度，单位像素
     * @param artPixels 图片的bytes，
     * @param index 第几首歌（-1 未知）
     * @param totalCount 总歌曲数 （-1 未知）
     * @param popup 是否需要弹出媒体信息通知栏
     */
    void onMediaChange(int mediaType, String name, String info, int artWidth, int artHeight, in byte[] artPixels, int index, int totalCount, boolean popup);

    /**
     * 媒体播放状态发生改变，例：音乐进度变化、播放暂停等
     * @param mediaType 对应Media.MediaType
     * @param progress 进度信息（单位秒）
     * @param duration 总进度（单位秒）
     */
    void onPlayStateChange(int mediaType, int playState, int position, int duration);

    /**
     * 媒体播放的输出区发生改变
     * @param mediaType 对应Media.MediaType
     * @param zone IVIMedia.Zone
     */
    void onMediaZoneChanged(int mediaType, int zone);
}
