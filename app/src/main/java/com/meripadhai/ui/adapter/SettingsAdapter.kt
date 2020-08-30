package com.meripadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.Setting
import com.meripadhai.utils.Const
import com.meripadhai.utils.Const.COURSE_ITEM
import com.meripadhai.utils.Const.ITEM_3
import com.meripadhai.utils.Const.SETTING_USER
import com.meripadhai.utils.GlideApp
import com.meripadhai.utils.Utils
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SettingsAdapter(
    val context: Context,
    private val list: ArrayList<Setting>,
    private val listener: ListItemClickListener<Int, Setting>
) : RecyclerView.Adapter<SettingsAdapter.BaseItem>() {

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItem {
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

            else ->//COURSE_HEADER ->
                SettingBottom(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_setting_2, parent, false)
                )

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseItem, position: Int) {
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
        holder.vMain.setOnClickListener { v: View? ->
            listener.onItemClick(Const.TYPE_CLICKED, list[position])
        }

    }

    private fun onBindHeaderProfile(holder: BaseItem, position: Int) {
        val headerRow = holder as SettingHeaderProfile
        headerRow.tvUserName.text = list[position].name
        GlideApp.with(headerRow.civProfile.context)
            .load(
                FirebaseStorage.getInstance()
                    .getReference("userPhoto/" + list[position].url)
            )
            .error(R.drawable.ic_user_24)
            .placeholder(R.drawable.ic_user_24)
            .into(headerRow.civProfile)
    }

    fun setUser(name: String, url: String?) {
        list[0].name = name
        list[0].url = url
        notifyItemChanged(0)
    }

    private fun onBindHeaderItems(holder: BaseItem, position: Int) {
        val headerRow = holder as SettingItems
        headerRow.tvName.text = list[position].name
        holder.ivIcon.setImageResource(list[position].id)
    }

    private fun onBindBottom(holder: BaseItem, position: Int) {
        val headerRow = holder as SettingBottom
        headerRow.tvName.text = list[position].name
        holder.ivIcon.setImageResource(list[position].id)
    }

    class SettingHeaderProfile(itemView: View) : BaseItem(itemView) {
        val tvUserName: AppCompatTextView = itemView.findViewById(R.id.tvUserName)
        val civProfile: CircleImageView = itemView.findViewById(R.id.civProfile)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
    }

    class SettingItems(itemView: View) : BaseItem(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val ivIcon: AppCompatImageView = itemView.findViewById(R.id.ivIcon)
    }

    class SettingBottom(itemView: View) : BaseItem(itemView) {
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tvName)
        val ivIcon: AppCompatImageView = itemView.findViewById(R.id.ivIcon)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
    }

    class SettingSocial(itemView: View) : BaseItem(itemView) {
        private val iv1: View = itemView.findViewById(R.id.iv1)
        private val iv2: View = itemView.findViewById(R.id.iv2)
        private val iv3: View = itemView.findViewById(R.id.iv3)
        private val iv4: View = itemView.findViewById(R.id.iv4)

        init {
            (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            iv1.setOnClickListener { Utils.openFacebookIntent(itemView.context) }
            iv2.setOnClickListener { Utils.openYouTube(itemView.context) }
            iv3.setOnClickListener { Utils.openInstagram(itemView.context) }
            iv4.setOnClickListener { Utils.openTwitter(itemView.context) }
        }
    }

    open class BaseItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vMain: View = itemView.findViewById(R.id.vMain)
    }
}