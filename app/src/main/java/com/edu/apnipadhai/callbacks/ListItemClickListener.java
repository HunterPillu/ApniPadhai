package com.edu.apnipadhai.callbacks;

public interface ListItemClickListener<EVENT, MODEL> {
    void onItemClick(EVENT type, MODEL item);
}
