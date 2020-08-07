package com.edu.apnipadhai.model

open class Setting {
    var id: Int = 0
    var name: String = ""
    var url: String? = null
    var type: Int = 0

    constructor(id: Int, name: String, type: Int) {
        this.id = id
        this.name = name
        this.type = type

    }

    constructor(url: String, name: String, type: Int) {
        this.url = url
        this.name = name
        this.type = type

    }

    constructor()

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name
        )
    }
}