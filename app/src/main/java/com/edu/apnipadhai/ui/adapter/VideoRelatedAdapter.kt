package com.edu.apnipadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.utils.GlideApp
import com.edu.apnipadhai.utils.GlideRequests
import java.util.*
import kotlin.collections.ArrayList

class VideoRelatedAdapter(
    context: Context,
    listener: ListItemClickListener<Int, VideoModel>
) : RecyclerView.Adapter<VideoRelatedAdapter.RecordViewHolder>() {
    private val listener: ListItemClickListener<Int, VideoModel>
    private var list: ArrayList<VideoModel>
    private val glide: GlideRequests
    fun updateList(list: ArrayList<VideoModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setList(
        list: ArrayList<VideoModel>,
        textView: AppCompatTextView,
        rvRecords: RecyclerView
    ) {
        updateList(list)
        if (list.size <= 0) {
            textView.visibility = View.VISIBLE
            rvRecords.visibility = View.GONE
        } else {
            textView.visibility = View.GONE
            rvRecords.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {
        val listItem = LayoutInflater.from(parent.context).inflate(
            R.layout.related_video,
            parent,
            false
        )

        return RecordViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int
    ) {
        val vo = list[position]
        holder.tvTitle.text = vo.name
        glide.load(vo.thumbnailUrl).placeholder(R.drawable.logo).into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            listener.onItemClick(-1,vo)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val ivThumbnail: AppCompatImageView = itemView.findViewById(R.id.ivThumbnail)
    }

    init {
        list = ArrayList()
        this.listener = listener
        glide = GlideApp.with(context)
    }
}