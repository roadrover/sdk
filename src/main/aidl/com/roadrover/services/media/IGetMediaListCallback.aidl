// IGetMediaListCallback.aidl
package com.roadrover.services.media;

import com.roadrover.services.media.StMusic;

// 获取媒体列表的回调

interface IGetMediaListCallback {

    /**
     * @param stAudios 进度数据
     * @param path 获取的目录
     */
    void onProgress(in List<StMusic> stAudios, String path);

    /**
     * @param stAudios 进度数据，APP需要在结尾的时候将 onProgress的数据和OnFinish的数据综合起来
     * @param path 获取的目录
     */
    void onFinish(in List<StMusic> stAudios, String path);
}
