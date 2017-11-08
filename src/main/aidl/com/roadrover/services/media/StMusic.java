package com.roadrover.services.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by y on 2017/2/24.
 * 音乐的结构体类，该类主要用作id3信息传递
 */

public class StMusic implements Parcelable {

    /** 音乐文件路径 */
    public String mPath = "";
    /** track信息 */
    public String mTrack = "";
    /** 专辑信息 */
    public String mAlbum = "";
    /** 歌手名字 */
    public String mArtist = "";
    /** 歌曲名字 */
    public String mName = "";
    /** 歌曲时长 */
    public long mDuration = 0;

    protected StMusic(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<StMusic> CREATOR = new Creator<StMusic>() {
        @Override
        public StMusic createFromParcel(Parcel in) {
            return new StMusic(in);
        }

        @Override
        public StMusic[] newArray(int size) {
            return new StMusic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPath);
        parcel.writeString(mTrack);
        parcel.writeString(mAlbum);
        parcel.writeString(mArtist);
        parcel.writeString(mName);
        parcel.writeLong(mDuration);
    }

    public void readFromParcel(Parcel source) {
        if (null != source) {
            mPath = source.readString();
            mTrack = source.readString();
            mAlbum = source.readString();
            mArtist = source.readString();
            mName = source.readString();
            mDuration = source.readLong();
//            Logcat.d("mPath:" + mPath);
        }
    }

    /**
     * 创建一个音乐的数据传输对象
     * @param path 路径
     * @param track 音轨信息
     * @param album 歌手
     * @param artist 歌曲
     * @param name 名字
     * @param duration 时长
     * @return
     */
    public static StMusic createStMusic(@NonNull String path, @NonNull String track,
                                        @NonNull String album, @NonNull String artist,
                                        @NonNull String name, long duration) {
        StMusic stMusic = CREATOR.createFromParcel(null);
        stMusic.mPath = path;
        stMusic.mTrack = track;
        stMusic.mAlbum = album;
        stMusic.mArtist = artist;
        stMusic.mName = name;
        stMusic.mDuration = duration;
        return stMusic;
    }
}
