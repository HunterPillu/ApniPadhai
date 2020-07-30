package com.edu.apnipadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.Const.COURSE_HEADER
import com.edu.apnipadhai.utils.Const.COURSE_ITEM
import java.util.*

class EditCategoryAdapter(
    val context: Context,
    val listener: ListItemClickListener<Int, Course>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //private val listener: ListItemClickListener<Int, Course>
    private val list: MutableList<Course>

    // private val cardSelected: Int
    //private val cardUnselected: Int
    fun setList(list: List<Course>) {
        this.list.clear()
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
        return if (COURSE_HEADER == viewType) {
            val listItem =
                LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false)
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
        if (vo.courseType == COURSE_ITEM) {
            val vh =
                holder as ItemVH
            vh.tvName.text = vo.name
            holder.itemView.setOnClickListener {
                listener.onItemClick(
                    Const.TYPE_CLICKED,
                    vo
                )
            }
            vh.itemView.setOnLongClickListener {
                popupMenus(it, holder.adapterPosition)
                true
            }
        } else {
            /* HeaderVH vh = (HeaderVH) holder;
            vh.tvName.setText(vo.getName());*/
            holder.itemView.setOnClickListener {
                listener.onItemClick(
                    Const.TYPE_ADD,
                    vo
                )
            }
        }


    }

    private fun popupMenus(view: View, position: Int) {

        val popup = PopupMenu(context, view)
        popup.getMenuInflater().inflate(R.menu.add_edit_menu, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.edit -> {
                        listener.onItemClick(Const.TYPE_EDIT, list[position])
                    }
                    else -> {
                        listener.onItemClick(Const.TYPE_DELETE, list[position])
                    }
                }
                return true
            }
        })
        popup.show()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        // val cvMain: CardView = itemView.findViewById(R.id.cvMain)

    }

    class HeaderVH  //private AppCompatTextView tvName;
        (itemView: View) : RecyclerView.ViewHolder(itemView)


    init {
        list = ArrayList()
        //cardSelected = context.resources.getColor(R.color.colorPrimary)
        //cardUnselected = context.resources.getColor(R.color.card_color_category)
    }
}