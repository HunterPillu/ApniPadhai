package com.edu.apnipadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.utils.Const
import java.util.*

class CourseAdapter(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list: MutableList<Course>
    private var selectedPosition = -1
    private val cardSelected: Int
    private val cardUnselected: Int
    private val textSecondry: Int


    fun updateList(list: List<Course>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].courseType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (Const.COURSE_HEADER == viewType) {
            val listItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_course_header, parent, false)
            HeaderVH(listItem)
        } else {
            val listItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_course, parent, false)
            ItemVH(listItem)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val vo = list[position]
        if (vo.courseType == Const.COURSE_ITEM) {
            val vh =
                holder as ItemVH

            vh.tvName.text = vo.name
            vh.tvCount.text = "${vo.videoCount}"
            if (vo.selected) {
                vh.cvMain.setCardBackgroundColor(cardSelected)
                selectedPosition = holder.adapterPosition
                vh.tvName.setTextColor(cardUnselected)
                vh.tvCount.setTextColor(cardUnselected)
                vh.tvCount.compoundDrawables[0]?.setTint(cardUnselected)
            } else {
                vh.cvMain.setCardBackgroundColor(cardUnselected)
                vh.tvName.setTextColor(textSecondry)
                vh.tvCount.setTextColor(textSecondry)
                vh.tvCount.compoundDrawables[0]?.setTint(cardSelected)
            }
            // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
            vh.cvMain.setOnClickListener { v: View? ->
                updatePreviousItem(
                    holder.adapterPosition
                )
            }
        } else {
            val vh =
                holder as HeaderVH
            vh.tvName.text = vo.name
        }
    }

    private fun updatePreviousItem(pos: Int) {
        if (selectedPosition == -1) {
            list[pos].selected = true
            selectedPosition = pos
            notifyItemChanged(selectedPosition)
        } else if (pos == selectedPosition) {
            selectedPosition = -1
            list[pos].selected = false
            notifyItemChanged(pos)
        } else {
            list[selectedPosition].selected = false
            notifyItemChanged(selectedPosition)
            list[pos].selected = true
            selectedPosition = pos
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun selectedCourseId(): Int {
        return if (selectedPosition == -1) {
            selectedPosition
        } else list[selectedPosition].id
    }

    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val tvCount: AppCompatTextView = itemView.findViewById(R.id.tvCount)

        val cvMain: CardView = itemView.findViewById(R.id.cvMain)

    }

    class HeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)

        init {
            //StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setFullSpan(true);
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            //itemView.setLayoutParams(layoutParams);
        }
    }

    init {
        list = ArrayList()
        cardSelected = context.resources.getColor(R.color.colorPrimary)
        cardUnselected = context.resources.getColor(R.color.card_color_category)
        textSecondry = context.resources.getColor(R.color.text_secondary)
    }
}