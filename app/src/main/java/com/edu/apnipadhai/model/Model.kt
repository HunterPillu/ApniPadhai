package com.edu.apnipadhai.model

import com.edu.apnipadhai.utils.Const

open class Model {
    var type: Int = Const.VT_EMPTY


    fun isEmptyView(): Boolean {
        return type == Const.VT_EMPTY
    }
}