package com.edu.apnipadhai.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Model
import com.edu.apnipadhai.utils.CustomLog

abstract class BaseAdapter<E : Model> : RecyclerView.Adapter<BaseHolder>() {
    internal val TAG = BaseAdapter::class.java.simpleName

    @kotlin.jvm.JvmField
    var listener: ListItemClickListener<Int, E>? = null

    @kotlin.jvm.JvmField
    var list: ArrayList<E>? = null


    fun updateList(list: ArrayList<E>?) {
        if (null != list && !list.isEmpty()) {
            this.list = list
        } else {
            this.list = ArrayList<E>()
            this.list?.add(Model() as E)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list!![position].type
    }

    /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
         if (viewType == Const.VT_EMPTY) {
             val listItem =
                 LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
             return BaseHolder(listItem)
         }
         throw IllegalArgumentException()
     }*/

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        CustomLog.d(TAG, "onBindViewHolder")
    }


    override fun getItemCount(): Int {
        return list!!.size
    }
}