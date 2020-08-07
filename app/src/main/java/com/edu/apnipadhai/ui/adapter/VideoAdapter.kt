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

class VideoAdapter(
    context: Context,
    listener: ListItemClickListener<Int, VideoModel>,
    orientation: Boolean
) : RecyclerView.Adapter<VideoAdapter.RecordViewHolder>() {
    private val listener: ListItemClickListener<Int, VideoModel>
    private var list: ArrayList<VideoModel>
    private val glide: GlideRequests
    private val horizontal: Boolean
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
            if (horizontal) R.layout.item_video_horizontal else R.layout.item_video,
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
        //StringBuilder builder = new StringBuilder();
        //builder.append(vo.getChannel()).append(" ").append(vo.getViews()).append(" ").append(vo.getUploaded()).append(" ").append("months ago");
        holder.tvOther.text = vo.channel
        //holder.tvDuration.setText(vo.getDuration());
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.cvMain.setOnClickListener { v: View? ->
            listener.onItemClick(
                -1,
                vo
            )
        }
        glide.load(vo.thumbnailUrl).placeholder(R.drawable.logo).into(holder.ivThumbnail)

        /* holder.ivMore.setOnClickListener(v -> {
            menuListener.itemClick(vo, v);
        });*/
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val tvOther: AppCompatTextView = itemView.findViewById(R.id.tvOther)
        val ivThumbnail: AppCompatImageView = itemView.findViewById(R.id.ivThumbnail)
        val cvMain: View = itemView.findViewById(R.id.cvMain)

    }

    init {
        list = ArrayList()
        this.listener = listener
        glide = GlideApp.with(context)
        horizontal = orientation
    }
}