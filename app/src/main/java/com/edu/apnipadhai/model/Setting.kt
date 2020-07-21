package com.edu.apnipadhai.model

open class Setting {
    var id: Int = 0
    var name = ""

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    constructor()

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name
        )
    }
}