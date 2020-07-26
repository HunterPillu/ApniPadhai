package com.edu.apnipadhai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.ui.adapter.SettingsAdapter.ChildHolder
import com.edu.apnipadhai.utils.Const.COURSE_ITEM
import java.util.*

class SettingsAdapter(private val list: ArrayList<Setting>, private val listener : ListItemClickListener<Setting>) : RecyclerView.Adapter<ChildHolder>() {
    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        return if (viewType == COURSE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_setting, parent, false)
            ChildHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_setting_2, parent, false)
            HeaderHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChildHolder, position: Int) {
        val setting = list[position]
        holder.tvName.text = setting.name
        holder.ivIcon.setImageResource(setting.id)

        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.itemView.setOnClickListener { v: View? ->
            listener.onItemClick(setting)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    open class ChildHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvName: AppCompatTextView
        var ivIcon: AppCompatImageView

        init {
            tvName = itemView.findViewById(R.id.tvName)
            ivIcon = itemView.findViewById(R.id.ivIcon)
        }
    }

    open class HeaderHolder(itemView: View) : ChildHolder(itemView) {
        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
    }

}