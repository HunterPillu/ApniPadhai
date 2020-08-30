package com.meripadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.covidbeads.app.assesment.util.shortToast
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.Video
import com.meripadhai.model.VideoModel
import com.meripadhai.ui.activity.YouTubeActivity
import com.meripadhai.ui.adapter.BookmarksAdapter
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class BookamrksFragment : BaseFragment(), ListItemClickListener<Int, VideoModel> {
    private lateinit var adapter: BookmarksAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var ivSearch: AppCompatImageView
    private lateinit var rvRecords: RecyclerView
    private var canUpdateTitle = true

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
        if (canUpdateTitle) {
            updateToolbarTitle(getString(R.string.bookmarks))
        }
        rvRecords = layoutView!!.findViewById(R.id.rvSuppliers)

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
            shortToast(context!!, getString(R.string.no_internet_connection))
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
                } else {
                    showHideEmptyState(false)
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
                        adapter.updateList(list1)
                    } else {
                        adapter.addList(list1)
                    }
                }
            }

    }

    private var viewStub: ViewStub? = null
    private fun showHideEmptyState(dataExits: Boolean) {
        if (dataExits) {
            if (null != viewStub) {
                rvRecords.visibility = View.VISIBLE
                viewStub?.visibility = View.GONE
                //viewStub?.findViewById<LottieAnimationView>(R.id.img)?.pauseAnimation()
            }
        } else {
            if (null == viewStub) {
                viewStub = layoutView?.findViewById(R.id.viewStub)!!
                val view = viewStub?.inflate()
                view?.findViewById<LottieAnimationView>(R.id.img)?.playAnimation()
            }
            rvRecords.visibility = View.GONE
            viewStub?.visibility = View.VISIBLE
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


    companion object {
        val TAG = BookamrksFragment::class.java.simpleName
        fun newInstance(canUpdateTitle: Boolean): BookamrksFragment {
            val fragment = BookamrksFragment()
            fragment.canUpdateTitle = canUpdateTitle
            return fragment
        }
    }
}
