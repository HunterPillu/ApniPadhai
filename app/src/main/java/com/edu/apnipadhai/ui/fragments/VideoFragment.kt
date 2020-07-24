package com.e

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.Video
import com.edu.apnipadhai.ui.activity.YouTubeActivity
import com.edu.apnipadhai.ui.adapter.VideoAdapter
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.FirebaseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class VideoFragment : BaseFragment(), ListItemClickListener<Video> {
    private lateinit var item: Category
    private var adapter: VideoAdapter? = null
    private var swipeRefresh: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_videos, container, false)
        setRecyclerView()
        fetchData()
        return layoutView
    }

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvSuppliers)
        adapter = VideoAdapter(this, VideoAdapter.OnChildItemClickListener { video, view ->
            popupMenus(view)
        })
        rvRecords?.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh?.setOnRefreshListener(this)
    }

    private fun popupMenus(view: View) {
        val popup = PopupMenu(context, view)
        popup.getMenuInflater().inflate(R.menu.video_menu, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                Toast.makeText(
                    context, item.getTitle(),
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        })
        popup.show()
    }


    override fun onRefresh() {
        fetchData()
    }

    private fun fetchData() {
        CustomLog.d(TAG, "fetchData")

        if (!Connectivity.isConnected(context)) {
            swipeRefresh!!.isRefreshing = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        swipeRefresh?.setRefreshing(true)

        FirebaseData.getVideos(item.id, object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                CustomLog.e(TAG, "database errot : ${error.message}")

                swipeRefresh?.setRefreshing(false)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                swipeRefresh?.setRefreshing(false)
                val list: ArrayList<Video?> = ArrayList()
                for (postSnapshot in snapshot.getChildren()) {
                    val user: Video? = postSnapshot.getValue(Video::class.java)
                    list.add(user)
                }
                adapter!!.setList(list)
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "VideoFragment"
        fun newInstance(item: Category): VideoFragment {
            val fragment = VideoFragment()
            fragment.item = item
            return fragment
        }

    }

    override fun onItemClick(item: Video) {
        val intent = Intent(context, YouTubeActivity::class.java)
        intent.putExtra("videoId", item.videoId)
        startActivity(intent)
        /* (activity!! as MainActivity).openFragment(
             newInstance(
                 item.id,
                 item.name,
                 item.videoCount,
             )
         )*/
    }
}
