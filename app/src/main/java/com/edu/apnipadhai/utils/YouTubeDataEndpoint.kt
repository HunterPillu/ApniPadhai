package com.edu.apnipadhai.utils

import com.edu.apnipadhai.model.VideoModel
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.youtube.YouTube
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.io.IOException

object YouTubeDataEndpoint {
    private const val APP_NAME = "MeriPadhai"
    private const val YOUTUBE_DATA_API_KEY = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE"
    @JvmStatic
    fun getVideoInfoFromYouTubeDataAPIs(videoId: String): Single<VideoModel> {
        val onSubscribe =
            SingleOnSubscribe { emitter: SingleEmitter<VideoModel> ->
                try {
                    val youTubeDataAPIEndpoint = buildYouTubeEndpoint()
                    val query =
                        buildVideosListQuery(youTubeDataAPIEndpoint, videoId)
                    val videoListResponse = query.execute()
                    if (videoListResponse.items.size != 1) throw RuntimeException("There should be exactly one video with the specified id")
                    val video =
                        videoListResponse.items[0]
                    val videoTitle = video.snippet.title
                    val url = video.snippet.thumbnails.medium.url
                    // Bitmap bitmap = NetworkUtils.getBitmapFromURL(url);
                    val channel = buildChannelsListQuery(
                        youTubeDataAPIEndpoint,
                        video.snippet.channelId
                    ).execute()
                    val channelTitle = channel.items[0].snippet.title
                    emitter.onSuccess(VideoModel(videoTitle, channelTitle, videoId, url))
                } catch (e: IOException) {
                    emitter.onError(e)
                }
            }
        return Single.create(onSubscribe)
    }

    @Throws(IOException::class)
    private fun buildVideosListQuery(
        youTubeDataAPIEndpoint: YouTube,
        videoId: String
    ): YouTube.Videos.List {
        return youTubeDataAPIEndpoint
            .videos()
            .list("snippet")
            .setFields("items(snippet(title,channelId,thumbnails(medium(url))))")
            .setId(videoId)
            .setKey(YOUTUBE_DATA_API_KEY)
    }

    @Throws(IOException::class)
    private fun buildChannelsListQuery(
        youTubeDataAPIEndpoint: YouTube,
        channelId: String
    ): YouTube.Channels.List {
        return youTubeDataAPIEndpoint
            .channels()
            .list("snippet")
            .setFields("items(snippet(title))")
            .setId(channelId)
            .setKey(YOUTUBE_DATA_API_KEY)
    }

    private fun buildYouTubeEndpoint(): YouTube {
        return YouTube.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory(), null)
            .setApplicationName(APP_NAME)
            .build()
    }
}