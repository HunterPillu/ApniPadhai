package com.edu.apnipadhai.model

class Video {
    var id: String = ""
    var name = ""
    var channel = "Channel Name"
    var views = "views"
    var uploaded = "2"
    var duration = "00:00"
    var categoryId: String = "1"
    var videoId: String? = null
    var active: Boolean = true


    constructor()

    constructor(
        name: String,
        channel: String,
        duration: String,
        categoryId: String,
        videoId: String?
    ) {
        this.name = name
        this.channel = channel
        this.duration = duration
        this.categoryId = categoryId
        this.videoId = videoId
    }
}