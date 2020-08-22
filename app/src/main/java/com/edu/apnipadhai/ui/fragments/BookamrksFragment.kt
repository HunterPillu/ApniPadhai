package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Video
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.ui.activity.YouTubeActivity
import com.edu.apnipadhai.ui.adapter.BookmarksAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class BookamrksFragment : BaseFragment(), ListItemClickListener<Int, VideoModel> {
    //private lateinit var item: Category
    private lateinit var adapter: BookmarksAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var tvNoData: AppCompatTextView

    private lateinit var ivSearch: AppCompatImageView
    private lateinit var rvRecords: RecyclerView

    val videoIdsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_videos, container, false)
        layoutView!!.findViewById<View>(R.id.appbar).visibility = View.GONE

        Handler().postDelayed({ init() }, 50)

        return layoutView
    }

    private fun init() {
        rvRecords = layoutView!!.findViewById(R.id.rvSuppliers)
        tvNoData = layoutView!!.findViewById(R.id.tvNoData)

        ivSearch = layoutView!!.findViewById(R.id.ivSearch)
        ivSearch.visibility = View.GONE

        setRecyclerView()
        fetchData()
    }


    override fun onRefresh() {
        fetchData()
        // rlToolBar.visibility = View.VISIBLE
        //rlSearch.visibility = View.GONE
    }

    private fun fetchData() {
        if (!Connectivity.isConnected(context!!)) {
            swipeRefresh.isRefreshing = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }

        videoIdsList.clear()
        swipeRefresh.setRefreshing(true)

        FirebaseFirestore.getInstance().collection(Const.TABLE_BOOKMARK)
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .collection(Const.SUB_BOOKMARK_VIDEOS)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    for (postSnapshot in documents) {
                        val model: Video = postSnapshot.toObject(Video::class.java)
                        videoIdsList.add(model.videoId.toString())
                    }
                    loadVideos(0, 10)
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.e(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }

    fun loadVideos(startPos: Int, endPos: Int) {
        //whereIn query can only take 10 ids per query : so handling that
        var toPos = endPos
        if (endPos > videoIdsList.size) {
            toPos = videoIdsList.size
        } else {
            loadVideos(endPos, endPos + 10)
        }
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            .whereIn(Const.FIELD_FKEY, videoIdsList.subList(startPos, toPos))
            .get()
            .addOnSuccessListener { documents ->
                swipeRefresh.setRefreshing(false)
                val list1 = ArrayList<VideoModel>()
                if (documents.size() > 0) {
                    for (postSnapshot in documents) {
                        val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                        list1.add(model)
                    }
                    if (startPos == 0) {
                        adapter.setList(list1, tvNoData, rvRecords)
                    } else {
                        adapter.addList(list1)
                    }
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.e(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    private fun setRecyclerView() {
        adapter = BookmarksAdapter(context!!, this, false)
        val layoutManager = LinearLayoutManager(context)
        rvRecords.layoutManager = layoutManager
        rvRecords.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this)

    }

    override fun onItemClick(type: Int, item: VideoModel) {
        val intent = Intent(context, YouTubeActivity::class.java)
        intent.putExtra(Const.VIDEO_MODEL, Gson().toJson(item))
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        // tvTitle.text = getString(R.string.current_affairs)
        updateToolbarTitle(getString(R.string.bookmarks))
    }

    companion object {
        val TAG = BookamrksFragment::class.java.simpleName
        fun newInstance(): BookamrksFragment {
            val fragment = BookamrksFragment()
            // fragment.item = item
            return fragment
        }
    }
}
