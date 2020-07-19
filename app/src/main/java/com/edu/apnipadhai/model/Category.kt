package com.edu.apnipadhai.model

class Category {
    var id: Long = 0
    var name = ""
    var parentId: Long = 0
    var videoCount = 0
    var active: Boolean = true
    var selected: Boolean = false

    constructor(id: Long, name: String, parentId: Long) {
        this.id = id
        this.name = name
        this.parentId = parentId
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "videoCount" to videoCount,
            "active" to active,
            "selected" to selected
        )
    }
}