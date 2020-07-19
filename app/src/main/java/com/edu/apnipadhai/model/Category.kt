package com.edu.apnipadhai.model

open class Category {
    var id: Int = 0
    var name = ""
    var parentId: Int = 0
    var videoCount = 0
    var active: Boolean = true

    constructor(id: Int, name: String, parentId: Int) {
        this.id = id
        this.name = name
        this.parentId = parentId
    }

    constructor()

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "parentId" to parentId,
            "videoCount" to videoCount,
            "active" to active
        )
    }
}