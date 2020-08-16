package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.ui.activity.YouTubeActivity
import com.edu.apnipadhai.ui.adapter.VideoAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class VideoFragment : BaseFragment(), ListItemClickListener<Int, VideoModel>,
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private lateinit var item: Category
    private lateinit var adapter: VideoAdapter
    private lateinit var list1: ArrayList<VideoModel>
    private var searchString: String? = null
    private var lastResult: DocumentSnapshot? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var actionMode: ActionMode

    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvNoData: AppCompatTextView
    private lateinit var svSearchExpanded: SearchView
    private lateinit var ivSearch: AppCompatImageView
    private lateinit var ivBackSearch: AppCompatImageView
    private lateinit var rlToolBar: RelativeLayout
    private lateinit var rlSearch: RelativeLayout
    private lateinit var ivBack: AppCompatImageView
    private lateinit var rvRecords: RecyclerView
    private lateinit var pb_progress: ProgressBar

    private var final_listItem = 1
    private var currentItems = 0
    private var totalItems = 0
    private var scrolloutItems = 0
    private var isScrolling = false
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_videos, container, false)
        init()

//        btn_srch?.setOnClickListener {
//
//            val itemId = "${item.id}"
//            FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS).whereEqualTo(Const.FIELD_CATEGORY_ID,itemId)
//                .whereEqualTo("name","CLASS 6th || MATHS || WHOLE NUMBERS || SOLVED QUESTION OF 3 TO 8 || FOR ALL BOARDS || NCERT")
//                .get().addOnSuccessListener { documents ->
//
//                    val list1 = ArrayList<VideoModel>()
//                    for (postSnapshot in documents) {
//
//                        val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
//                        //course.fKey = postSnapshot.id
//                        list1.add(model)
//                    }
//
//                    adapter.setList(list1)
//                }
//                .addOnFailureListener { exception ->
//                    CustomLog.e(
//                        TAG,
//                        "Error getting documents: ${exception.localizedMessage}"
//                    )
//                }
//        }

        list1 = ArrayList<VideoModel>()
        setRecyclerView()
        fetchData()
        return layoutView
    }

    private fun init() {
        rvRecords = layoutView!!.findViewById(R.id.rvSuppliers)
        tvTitle = layoutView!!.findViewById(R.id.tvTitle)
        tvNoData = layoutView!!.findViewById(R.id.tvNoData)
        svSearchExpanded = layoutView!!.findViewById(R.id.svSearchExpanded)
        ivBackSearch = layoutView!!.findViewById(R.id.ivBackSearch)
        ivSearch = layoutView!!.findViewById(R.id.ivSearch)
        ivBack = layoutView!!.findViewById(R.id.ivBack)
        rlSearch = layoutView!!.findViewById(R.id.rlSearch)
        rlToolBar = layoutView!!.findViewById(R.id.rlToolBar)
        pb_progress = layoutView!!.findViewById(R.id.pb_progress)
        svSearchExpanded.visibility = View.VISIBLE
        ivSearch.visibility = View.VISIBLE
        svSearchExpanded.setOnQueryTextListener(this)
        svSearchExpanded.setOnCloseListener(this)

        ivBack.setOnClickListener {
            activity!!.finish()
        }

        ivSearch.setOnClickListener {
            isSearching = true
            rlToolBar.visibility = View.GONE
            rlSearch.visibility = View.VISIBLE
            svSearchExpanded.onActionViewExpanded()
        }

        ivBackSearch.setOnClickListener {
            isSearching = false
            rlToolBar.visibility = View.VISIBLE
            rlSearch.visibility = View.GONE
            svSearchExpanded.onActionViewCollapsed()
            adapter.setList(list1, tvNoData, rvRecords)
        }
    }

    fun fireStorePagination() {
        val query = if (lastResult == null) {
            val first = FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
                .whereEqualTo(Const.FIELD_CATEGORY_ID, "${item.id}")
                .limit(Const.LIMIT)
            first
        } else {
            val first = FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
                .whereEqualTo(Const.FIELD_CATEGORY_ID, "${item.id}")
                .startAfter(lastResult!!).limit(Const.LIMIT)
            first
        }
        query.get().addOnSuccessListener { documentSnapshots ->
            if (documentSnapshots.size() > 0) {
                for (postSnapshot in documentSnapshots) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    list1.add(model)
                    Log.e("videoModel__", model.name)
                }
                pb_progress.visibility = View.GONE
                adapter.setList(list1, tvNoData, rvRecords)
                isScrolling = false
                Log.e("----------", "----------")
                lastResult = documentSnapshots.documents.get(documentSnapshots.size() - 1)
            } else {
                final_listItem = 0
                pb_progress.visibility = View.GONE
            }
        }
    }

    override fun onRefresh() {
        fetchData()
        rlToolBar.visibility = View.VISIBLE
        rlSearch.visibility = View.GONE
    }

    private fun fetchData() {
        if (!Connectivity.isConnected(context!!)) {
            swipeRefresh.isRefreshing = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        isSearching = false
        lastResult = null
        currentItems = 0
        totalItems = 0
        scrolloutItems = 0
        final_listItem = 1
        list1.clear()
        swipeRefresh.setRefreshing(true)

        val itemId = "${item.id}"
        CustomLog.d(TAG, "crse : itemId = $itemId")
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            .whereEqualTo("categoryId", itemId).limit(Const.LIMIT)
            .get()
            .addOnSuccessListener { documents ->
                swipeRefresh.setRefreshing(false)

                if (documents.size() > 0) {
                    for (postSnapshot in documents) {
                        val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                        list1.add(model)
                    }
                    lastResult = documents.documents.get(documents.size() - 1)
                    adapter.setList(list1, tvNoData, rvRecords)
                    isScrolling = false
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.e(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    private fun setRecyclerView() {
        adapter = VideoAdapter(context!!, this, false)
        val layoutManager = LinearLayoutManager(context)
        rvRecords.layoutManager = layoutManager
        rvRecords?.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh?.setOnRefreshListener(this)

        rvRecords.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isSearching) {
                    if (list1.size >= Const.LIMIT) {
                        currentItems = layoutManager.childCount
                        totalItems = layoutManager.getItemCount()
                        scrolloutItems = layoutManager.findFirstVisibleItemPosition()
                        Log.e("items_count_", "$currentItems  $totalItems  $scrolloutItems")
                    }

                    if (!isScrolling && currentItems + scrolloutItems >= totalItems) {
                        if (final_listItem > 0) {
                            if (!Connectivity.isConnected(context!!)) {
                                swipeRefresh.isRefreshing = false
                                shortToast(getString(R.string.no_internet_connection))
                                return
                            }
                            isScrolling = true
                            pb_progress.visibility = View.VISIBLE
                            fireStorePagination()
                        }
                    }
                }
            }
        })
    }

    override fun onItemClick(type: Int, item: VideoModel) {
        val intent = Intent(context, YouTubeActivity::class.java)
        intent.putExtra(Const.VIDEO_MODEL, item)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        tvTitle.text = item.name
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

    private fun searchResult(query: String?) {
        if (!query.isNullOrEmpty() || !query.isNullOrBlank()) {
            searchString = query
            val listSearch: ArrayList<VideoModel> = ArrayList()
            for (i in 0 until list1.size) {
                if (list1[i].name.toLowerCase(Locale.ROOT)
                        .contains(query.toLowerCase(Locale.ROOT).trim())
                ) {
                    listSearch.add(list1[i])
                }
            }
            adapter.setList(listSearch, tvNoData, rvRecords)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchResult(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onClose(): Boolean {
        return true
    }
}
