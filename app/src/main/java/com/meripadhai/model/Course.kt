package com.meripadhai.model

import com.meripadhai.utils.Const

class Course : Category() {
    var selected: Boolean = false
    var courseType: Int = Const.COURSE_ITEM
    var fKey: String? = null

}