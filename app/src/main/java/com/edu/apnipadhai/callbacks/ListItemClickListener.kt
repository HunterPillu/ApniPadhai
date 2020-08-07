package com.edu.apnipadhai.callbacks

interface ListItemClickListener<EVENT, MODEL> {
    fun onItemClick(type: EVENT, item: MODEL)
}