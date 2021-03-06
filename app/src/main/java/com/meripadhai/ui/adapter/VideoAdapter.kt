package com.meripadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.VideoModel
import com.meripadhai.utils.Const
import com.meripadhai.utils.GlideApp
import com.meripadhai.utils.GlideRequests
import com.meripadhai.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore

class VideoAdapter(
    context: Context,
    listener: ListItemClickListener<Int, VideoModel>,
    orientation: Int
) : RecyclerView.Adapter<VideoAdapter.RecordViewHolder>() {
    private val context: Context
    private val listener: ListItemClickListener<Int, VideoModel>
    private var list: ArrayList<VideoModel>
    private val glide: GlideRequests
    private val layoutType: Int
    var pagingEnabled = false

    fun updateList(list: ArrayList<VideoModel>) {
        this.list = list
        notifyDataSetChanged()
    }


    fun setList(list: ArrayList<VideoModel>) {
        updateList(list)
    }

    override fun onViewAttachedToWindow(holder: RecordViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (pagingEnabled && list.size - 1 == holder.absoluteAdapterPosition) {
            listener.onItemClick(Const.TYPE_PAGINATION, list[0])//no use of list[0]
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {
        val listItem = LayoutInflater.from(parent.context).inflate(
            getLayoutRes(layoutType),
            //if (horizontal) R.layout.item_video_horizontal else R.layout.item_video,
            parent,
            false
        )

        return RecordViewHolder(listItem)
    }

    private fun getLayoutRes(layoutType: Int): Int {
        when (layoutType) {
            1 -> {
                return R.layout.item_video
            }
            2 -> {
                return R.layout.item_video_horizontal
            }
            else -> {
                return R.layout.dashboard_video
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int
    ) {
        val vo = list[position]
        holder.tvTitle.text = vo.name
        holder.tvOther.text = vo.channel
        holder.cvMain.setOnClickListener { v: View? ->
            listener.onItemClick(
                Const.TYPE_CLICKED,
                vo
            )
        }
        glide.load(vo.thumbnailUrl).placeholder(R.drawable.placeholder).into(holder.ivThumbnail)

        holder.ivBookmark?.setOnClickListener {
            listener.onItemClick(Const.TYPE_BOOKMARK, list[holder.adapterPosition])
        }
    }

    /* private fun popupMenus(view: View, position: Int) {
         val popup = PopupMenu(context, view)
         popup.menuInflater.inflate(R.menu.add_edit_menu, popup.getMenu())
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
     }*/

    override fun getItemCount(): Int {
        return list.size
    }

    class RecordViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        val tvOther: AppCompatTextView = itemView.findViewById(R.id.tvOther)
        val ivThumbnail: AppCompatImageView = itemView.findViewById(R.id.ivThumbnail)
        val cvMain: View = itemView.findViewById(R.id.cvMain)
        val ivBookmark: AppCompatImageView? = itemView.findViewById(R.id.ivBookmark)

    }

    init {
        list = ArrayList()
        this.listener = listener
        glide = GlideApp.with(context)
        this.context = context
        this.layoutType = orientation
    }


    private fun showDeleteDialog(pos: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.msg_delete_title)
        builder.setMessage(R.string.msg_delete)
            .setPositiveButton(R.string.yes) { dialog, id ->
                val fKey = list[pos].fKey ?: return@setPositiveButton
                list.removeAt(pos)
                notifyItemRemoved(pos)
                FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
                    .document(fKey).delete().addOnCompleteListener {
                        Utils.showToast(
                            context,
                            context.getString(R.string.msg_delete_success)
                        )
                    }
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}