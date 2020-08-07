package com.edu.apnipadhai.utils

import android.graphics.Bitmap

class VideoInfo internal constructor(
    val videoTitle: String,
    val channelTitle: String,
    val thumbnail: Bitmap,
    val url: String
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val videoInfo = o as VideoInfo
        if (videoTitle != videoInfo.videoTitle) return false
        return if (channelTitle != videoInfo.channelTitle) false else thumbnail == videoInfo.thumbnail
    }

    override fun hashCode(): Int {
        var result = videoTitle.hashCode()
        result = 31 * result + channelTitle.hashCode()
        result = 31 * result + thumbnail.hashCode()
        return result
    }

}