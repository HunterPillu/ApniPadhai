package com.edu.apnipadhai.model

class VideoModel {
    // var id = 0
    var fKey: String? = null
    var name = ""
    var channel = "Channel Name"
    var views = "views"
    var uploaded = "2"
    var duration = "00:00"
    var categoryId: String = "1"
    var courseId: Int = 0
    var videoId: String? = null
    var thumbnailUrl: String? = null
    var active: Boolean = true


    constructor()

    constructor(
        name: String,
        channel: String,
        videoId: String,
        thumbnailUrl: String?
    ) {
        this.name = name
        this.channel = channel
        this.videoId = videoId
        this.thumbnailUrl = thumbnailUrl
    }
}