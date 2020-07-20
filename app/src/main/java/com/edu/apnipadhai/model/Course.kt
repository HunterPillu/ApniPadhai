package com.edu.apnipadhai.model

import com.edu.apnipadhai.utils.Const

class Course : Category() {
    var selected: Boolean = false
    var courseType: Int = Const.COURSE_ITEM

}