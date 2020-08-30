package com.meripadhai.callbacks

interface ListItemClickListener<EVENT, MODEL> {
    fun onItemClick(type: EVENT, item: MODEL)
}