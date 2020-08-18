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
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.GlideApp
import com.edu.apnipadhai.utils.GlideRequests

class VideoRelatedAdapter(
    context: Context,
    listener: ListItemClickListener<Int, VideoModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val listItem = LayoutInflater.from(parent.context).inflate(
                R.layout.item_related_video_header,
                parent,
                false
            )
            return HeaderViewHolder(listItem)
        } else {
            val listItem = LayoutInflater.from(parent.context).inflate(
                R.layout.related_video,
                parent,
                false
            )

            return RecordViewHolder(listItem)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val vo = list[position]
        if (position == 0) {
            val vh = holder as HeaderViewHolder
            vh.tvTitle.text = vo.name
            vh.tvOther.text = vo.channel
            holder.ivShare.setOnClickListener {
                listener.onItemClick(Const.TYPE_CLICKED_2, vo)
            }
            holder.ivBookmark.setOnClickListener {
                listener.onItemClick(Const.TYPE_BOOKMARK, vo)
            }
        } else {
            val vh = holder as RecordViewHolder
            vh.tvTitle.text = vo.name
            vh.tvOther.text = vo.channel
            glide.load(vo.thumbnailUrl).placeholder(R.drawable.placeholder).into(vh.ivThumbnail)

            holder.itemView.setOnClickListener {
                listener.onItemClick(Const.TYPE_CLICKED, vo)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val tvOther: AppCompatTextView = itemView.findViewById(R.id.tvOther)
        val ivThumbnail: AppCompatImageView = itemView.findViewById(R.id.ivThumbnail)
    }

    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val ivShare = itemView.findViewById<View>(R.id.tvShare)
        val ivBookmark = itemView.findViewById<View>(R.id.tvBookmark)
        val tvTitle = itemView.findViewById<AppCompatTextView>(R.id.tvTitle)
        val tvOther = itemView.findViewById<AppCompatTextView>(R.id.tvOther)
    }

    init {
        list = ArrayList()
        this.listener = listener
        glide = GlideApp.with(context)
    }
}