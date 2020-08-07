package com.edu.apnipadhai.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class VideoInfo {
    @NonNull
    private final String videoTitle;
    @NonNull
    private final String channelTitle;
    private final String url;
    @NonNull
    private final Bitmap thumbnail;

    VideoInfo(@NonNull String videoTitle, @NonNull String channelTitle, @NonNull Bitmap thumbnail, String url) {
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.thumbnail = thumbnail;
        this.url = url;
    }



    @NonNull
    public String getVideoTitle() {
        return videoTitle;
    }

    public String getUrl() {
        return url;
    }

    @NonNull
    public String getChannelTitle() {
        return channelTitle;
    }

    @NonNull
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoInfo videoInfo = (VideoInfo) o;

        if (!videoTitle.equals(videoInfo.videoTitle)) return false;
        if (!channelTitle.equals(videoInfo.channelTitle)) return false;
        return thumbnail.equals(videoInfo.thumbnail);
    }

    @Override
    public int hashCode() {
        int result = videoTitle.hashCode();
        result = 31 * result + channelTitle.hashCode();
        result = 31 * result + thumbnail.hashCode();
        return result;
    }
}
