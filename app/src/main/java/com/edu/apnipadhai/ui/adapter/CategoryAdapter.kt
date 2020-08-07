package com.edu.apnipadhai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.ui.adapter.CategoryAdapter
import java.util.*

class CategoryAdapter(listener: ListItemClickListener<Int, Category>) :
    RecyclerView.Adapter<CategoryAdapter.RecordViewHolder>() {
    private val listener: ListItemClickListener<Int, Category>
    private var list: List<Category>

    fun setList(list: List<Category>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {
        val listItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return RecordViewHolder(listItem)
    }

    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int
    ) {
        val vo = list[position]
        holder.tvName.text = vo.name
        holder.tvCount.text = "${vo.videoCount}"
        // holder.ivStatus.setImageResource(getStatusImg(vo.fetchStatus()));
        holder.cvMain.setOnClickListener { listener.onItemClick(
                -1,
                vo
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val tvCount: AppCompatTextView = itemView.findViewById(R.id.tvCount)
        val cvMain: View = itemView.findViewById(R.id.cvMain)

    }

    companion object {
        private val TAG = CategoryAdapter::class.java.simpleName
    }

    init {
        list = ArrayList()
        this.listener = listener
    }
}