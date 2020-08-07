package com.edu.apnipadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.Const.COURSE_HEADER
import com.edu.apnipadhai.utils.Const.COURSE_ITEM
import com.edu.apnipadhai.utils.Const.ITEM_3
import com.edu.apnipadhai.utils.Const.SETTING_USER
import com.edu.apnipadhai.utils.GlideApp
import com.edu.apnipadhai.utils.Utils
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SettingsAdapter(
    val context: Context,
    private val list: ArrayList<Setting>,
    private val listener: ListItemClickListener<Int, Setting>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SETTING_USER ->
                SettingHeaderProfile(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_setting_3, parent, false)
                )

            ITEM_3 ->
                SettingSocial(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_social, parent, false)
                )

            COURSE_ITEM ->
                SettingItems(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_setting, parent, false)
                )

            COURSE_HEADER ->
                SettingBottom(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_setting_2, parent, false)
                )
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            COURSE_ITEM ->
                onBindHeaderItems(holder, holder.adapterPosition)
            SETTING_USER ->
                onBindHeaderProfile(holder, holder.adapterPosition)
            ITEM_3 -> {
                //do Nothing
            }
            else ->
                onBindBottom(holder, holder.adapterPosition)
        }
        holder.itemView.setOnClickListener { v: View? ->
            listener.onItemClick(Const.TYPE_CLICKED, list[position])
        }

    }

    private fun onBindHeaderProfile(holder: RecyclerView.ViewHolder, position: Int) {
        val headerRow = holder as SettingHeaderProfile
        headerRow.tvUserName.text = list[position].name
        GlideApp.with(headerRow.civProfile.context)
            .load(
                FirebaseStorage.getInstance()
                    .getReference("userPhoto/" + list[position].url)
            )
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(headerRow.civProfile)
    }

    fun setUser(name: String, url: String?) {
        list[0].name = name
        list[0].url = url
        notifyItemChanged(0)
    }

    private fun onBindHeaderItems(holder: RecyclerView.ViewHolder, position: Int) {
        val headerRow = holder as SettingItems
        headerRow.tvName.text = list[position].name
        holder.ivIcon.setImageResource(list[position].id)
    }

    private fun onBindBottom(holder: RecyclerView.ViewHolder, position: Int) {
        val headerRow = holder as SettingBottom
        headerRow.tvName.text = list[position].name
        holder.ivIcon.setImageResource(list[position].id)
    }

    class SettingHeaderProfile(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: AppCompatTextView = itemView.findViewById(R.id.tvUserName)
        val civProfile: CircleImageView = itemView.findViewById(R.id.civProfile)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
    }

    class SettingItems(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val ivIcon: AppCompatImageView = itemView.findViewById(R.id.ivIcon)
    }

    class SettingBottom(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val ivIcon: AppCompatImageView = itemView.findViewById(R.id.ivIcon)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
    }

    class SettingSocial(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv1: View = itemView.findViewById(R.id.iv1)
        val iv2: View = itemView.findViewById(R.id.iv2)
        val iv3: View = itemView.findViewById(R.id.iv3)
        val iv4: View = itemView.findViewById(R.id.iv4)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            iv1.setOnClickListener { Utils.openFacebookIntent(itemView.context) }
            iv2.setOnClickListener { Utils.openYouTube(itemView.context) }
            iv3.setOnClickListener { Utils.openInstagram(itemView.context) }
            iv4.setOnClickListener { Utils.openTwitter(itemView.context) }
        }
    }
}