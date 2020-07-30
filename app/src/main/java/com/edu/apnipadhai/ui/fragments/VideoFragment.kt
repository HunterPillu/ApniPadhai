package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.callbacks.MenuClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.ui.activity.YouTubeActivity
import com.edu.apnipadhai.ui.adapter.VideoAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore

class VideoFragment : BaseFragment(), ListItemClickListener<Int, VideoModel>,
    MenuClickListener<VideoModel, View> {
    private lateinit var item: Category
    private lateinit var adapter: VideoAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout


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
        adapter = VideoAdapter(context!!,this, this)
        rvRecords?.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh?.setOnRefreshListener(this)
    }

    private fun popupMenus(view: View) {

        val popup = PopupMenu(context, view)
        popup.getMenuInflater().inflate(R.menu.video_menu, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return true
            }
        })
        popup.show()
    }

    override fun onItemClick(type: Int, item: VideoModel) {
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

    override fun itemClick(item: VideoModel, obj: View) {
        popupMenus(obj)
    }

    override fun onRefresh() {
        fetchData()
    }

    private fun fetchData() {
        //val parentID = PrefUtil.getCourseId(context!!)

        if (!Connectivity.isConnected(context)) {
            swipeRefresh.isRefreshing = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        swipeRefresh.setRefreshing(true)

        val itemId = "${item.id}"
        CustomLog.d(TAG, "crse : itemId = $itemId")
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            .whereEqualTo("categoryId", itemId)//"${item.id}")
            .get()
            .addOnSuccessListener { documents ->
                swipeRefresh.setRefreshing(false)
                val list1 = ArrayList<VideoModel>()
                /* val course = Course().apply {
                     id = -1
                     name = ""
                     courseType = Const.COURSE_HEADER
                     parentId = parentID
                 }
                 list1.add(course)*/
                for (postSnapshot in documents) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    //course.fKey = postSnapshot.id
                    list1.add(model)
                }

                adapter.setList(list1)
            }
            .addOnFailureListener { exception ->
                CustomLog.e(
                    TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }

    /* private fun fetchData2() {
         CustomLog.d(TAG, "fetchData")

         if (!Connectivity.isConnected(context)) {
             swipeRefresh.isRefreshing = false
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
                 val list: ArrayList<VideoModel?> = ArrayList()
                 for (postSnapshot in snapshot.getChildren()) {
                     val model: VideoModel? = postSnapshot.getValue(VideoModel::class.java)
                     list.add(model)
                 }
                 adapter!!.setList(list)
             }
         })
     }*/

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(item.name)
    }

    companion object {
        const val TAG = "VideoFragment"
        fun newInstance(item: Category): VideoFragment {
            val fragment = VideoFragment()
            fragment.item = item
            return fragment
        }

    }
}
