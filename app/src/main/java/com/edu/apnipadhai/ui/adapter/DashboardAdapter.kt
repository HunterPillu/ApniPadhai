package com.edu.apnipadhai.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Dashboard
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.ui.activity.YouTubeActivity
import com.edu.apnipadhai.utils.Const
import com.google.gson.Gson
import java.util.*

class DashboardAdapter(context: Context, listener: ListItemClickListener<Int, Dashboard>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val context: Context
    private val listener: ListItemClickListener<Int, Dashboard>
    private var list: MutableList<Dashboard>

    fun setList(list: MutableList<Dashboard>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].course!!.courseType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            Const.COURSE_HEADER -> {
                val listItem =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_dashboard_recent, parent, false)
                return RecentViewHolder(listItem)
            }
            else -> {
                val listItem =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_dashboard, parent, false)
                return RecordViewHolder(listItem)
            }
        }


    }

    override fun onBindViewHolder(
        vh: RecyclerView.ViewHolder,
        position: Int
    ) {

        val vo = list[position]
        when (vo.course?.courseType) {
            Const.COURSE_ITEM -> {
                val holder = vh as RecordViewHolder
                holder.tvHeader.text = vo.course?.name
                holder.tvCount.text = "(${vo.course!!.videoCount})"
                //holder.tvCount.visibility = if (vo.course!!.videoCount > 0) View.VISIBLE else View.GONE
                holder.tvSeeAll.visibility =
                    if (vo.course!!.videoCount > Const.VIDEO_THRESHOLD) View.VISIBLE else View.GONE

                /*if (null != holder.rvList.adapter) {
                        holder.videoAdapter.notifyDataSetChanged()
                    } else {
                        holder.rvList.adapter = holder.videoAdapter
                        holder.videoAdapter.updateList(vo.videoList)
                    }*/

                holder.rvList.adapter = holder.videoAdapter
                holder.videoAdapter.updateList(vo.videoList)

                holder.tvSeeAll.setOnClickListener {
                    listener.onItemClick(
                        Const.TYPE_CLICKED_2,
                        vo
                    )
                }
            }
            Const.COURSE_HEADER -> {
                val holder = vh as RecentViewHolder
                holder.rvList.adapter = holder.videoAdapter
                holder.videoAdapter.updateList(vo.videoList)
            }
        }
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(dashboard: Dashboard) {
        list.add(dashboard)
        notifyItemInserted(list.size - 1)
    }


    open class RecentViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {
        //val tvHeader: AppCompatTextView = itemView.findViewById(R.id.tvHeader)
        //val tvCount: AppCompatTextView = itemView.findViewById(R.id.tvCount)
        //val tvSeeAll: AppCompatTextView = itemView.findViewById(R.id.tvSeeAll)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)
        val videoAdapter: VideoAdapter

        init {
            //val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
            // snapHelper.attachToRecyclerView(rvList)
            videoAdapter = VideoAdapter(
                itemView.context,
                object : ListItemClickListener<Int, VideoModel> {
                    override fun onItemClick(type: Int, item: VideoModel) {
                        if (Const.TYPE_CLICKED != type) {
                            return
                        }
                        val intent = Intent(itemView.context, YouTubeActivity::class.java)
                        intent.putExtra(Const.VIDEO_MODEL, Gson().toJson(item))
                        itemView.context.startActivity(intent)
                    }
                },
                2
            )
            videoAdapter.pagingEnabled = false
        }
    }

    open class RecordViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {
        val tvHeader: AppCompatTextView = itemView.findViewById(R.id.tvHeader)
        val tvCount: AppCompatTextView = itemView.findViewById(R.id.tvCount)
        val tvSeeAll: AppCompatTextView = itemView.findViewById(R.id.tvSeeAll)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)
        val videoAdapter: VideoAdapter

        init {
            //val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
            // snapHelper.attachToRecyclerView(rvList)
            videoAdapter = VideoAdapter(
                itemView.context,
                object : ListItemClickListener<Int, VideoModel> {
                    override fun onItemClick(type: Int, item: VideoModel) {
                        if (Const.TYPE_CLICKED != type) {
                            return
                        }
                        val intent = Intent(itemView.context, YouTubeActivity::class.java)
                        intent.putExtra(Const.VIDEO_MODEL, Gson().toJson(item))
                        itemView.context.startActivity(intent)
                    }
                },
                3
            )
            videoAdapter.pagingEnabled = false
        }
    }

    companion object {
        private val TAG = DashboardAdapter::class.java.simpleName
    }

    init {
        list = ArrayList()
        this.listener = listener
        this.context = context
    }
}