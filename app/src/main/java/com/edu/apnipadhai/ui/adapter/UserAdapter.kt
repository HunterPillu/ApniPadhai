package com.covidbeads.app.assesment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.utils.Const
import java.util.*

class UserAdapter(val context: Context, val listener: ListItemClickListener<Int, User>) :
    RecyclerView.Adapter<UserAdapter.RecordViewHolder>() {
    private var cDisabled: Int
    private var cOffline: Int
    private var cOnline: Int
    //private val TAG = this::class.java.simpleName

    private var list: List<User>

    fun updateList(list: List<User>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val listItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return RecordViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val vo = list[position]
        holder.tvName.text = vo.name
        holder.tvLastMsg.text =
            context.getString(if (vo.online) R.string.online else R.string.offline)
        holder.tvLastMsg.setTextColor(if (vo.online) cOnline else cOffline)
        holder.itemView.setOnClickListener { listener.onItemClick(Const.TYPE_CLICKED, vo) }
    }

    /*private fun getStatusImg(fetchStatus: Int): Int {
        return when (fetchStatus) {
            1 -> R.drawable.round_green
            2 -> R.drawable.round_red
            else -> R.drawable.round_grey
        }
    }*/

    private fun getStatusColor(fetchStatus: Int): Int {
        return when (fetchStatus) {
            1 -> cOnline
            2 -> cOffline
            else -> cDisabled
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)

        //val ivStatus: AppCompatImageView = itemView.findViewById(R.id.ivStatus)
        val tvLastMsg: AppCompatTextView = itemView.findViewById(R.id.tvLastMsg)

    }


    init {
        list = ArrayList()
        cOnline = context.resources.getColor(R.color.online)
        cOffline = context.resources.getColor(R.color.offline)
        cDisabled = context.resources.getColor(R.color.disabled)
    }
}