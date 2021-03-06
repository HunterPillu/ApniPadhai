package com.meripadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.covidbeads.app.assesment.util.shortToast
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.VideoModel
import com.meripadhai.ui.activity.YouTubeActivity
import com.meripadhai.ui.adapter.AffairAdapter
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class AffairsFragment : BaseFragment(), ListItemClickListener<Int, VideoModel>,
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private lateinit var adapter: AffairAdapter
    private lateinit var list1: ArrayList<VideoModel>
    private var searchString: String? = null
    private var lastResult: DocumentSnapshot? = null
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var actionMode: ActionMode

    private lateinit var tvTitle: AppCompatTextView

    //private lateinit var tvNoData: AppCompatTextView
    private lateinit var svSearchExpanded: SearchView
    private lateinit var ivSearch: AppCompatImageView
    private lateinit var ivBackSearch: AppCompatImageView
    private lateinit var rlToolBar: View
    private lateinit var rlSearch: RelativeLayout
    private lateinit var ivBack: AppCompatImageView
    private lateinit var rvRecords: RecyclerView
    private lateinit var pb_progress: ProgressBar

    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_videos, container, false)
        rlToolBar = layoutView!!.findViewById(R.id.appbar)
        rlToolBar.visibility = View.GONE
        Handler().postDelayed({ init() }, 50)

        return layoutView
    }

    private fun init() {
        rvRecords = layoutView!!.findViewById(R.id.rvSuppliers)
        tvTitle = layoutView!!.findViewById(R.id.tvTitle)
        svSearchExpanded = layoutView!!.findViewById(R.id.svSearchExpanded)
        ivBackSearch = layoutView!!.findViewById(R.id.ivBackSearch)
        ivSearch = layoutView!!.findViewById(R.id.ivSearch)
        ivBack = layoutView!!.findViewById(R.id.ivBack)
        rlSearch = layoutView!!.findViewById(R.id.rlSearch)

        pb_progress = layoutView!!.findViewById(R.id.pb_progress)
        svSearchExpanded.visibility = View.VISIBLE
        ivSearch.visibility = View.VISIBLE
        svSearchExpanded.setOnQueryTextListener(this)
        svSearchExpanded.setOnCloseListener(this)

        list1 = ArrayList<VideoModel>()
        setRecyclerView()
        fetchData()

        ivBack.setOnClickListener {
            onBackPressed()
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
            adapter.updateList(list1)
            showHideEmptyState(list1.size > 0)
        }
    }

    fun fireStorePagination() {
        val query = if (lastResult == null) {
            val first = FirebaseFirestore.getInstance().collection(Const.TABLE_AFFAIRS)
                //.whereEqualTo(Const.FIELD_CATEGORY_ID, "${item.id}")
                .limit(Const.LIMIT)
            first
        } else {
            val first = FirebaseFirestore.getInstance().collection(Const.TABLE_AFFAIRS)
                //.whereEqualTo(Const.FIELD_CATEGORY_ID, "${item.id}")
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
                adapter.updateList(list1)
                Log.e("----------", "----------")
                lastResult = documentSnapshots.documents.get(documentSnapshots.size() - 1)
            } else {
                pb_progress.visibility = View.GONE
                showHideEmptyState(list1.size > 0)
            }
        }
    }

    override fun onRefresh() {
        fetchData()
        // rlToolBar.visibility = View.VISIBLE
        //rlSearch.visibility = View.GONE
    }

    private fun fetchData() {
        if (!Connectivity.isConnected(context!!)) {
            swipeRefresh.isRefreshing = false
            shortToast(context!!,getString(R.string.no_internet_connection))
            return
        }
        isSearching = false
        lastResult = null
        list1.clear()
        swipeRefresh.isRefreshing = true

        // val itemId = "${item.id}"
        // CustomLog.d(TAG, "crse : itemId = $itemId")
        FirebaseFirestore.getInstance().collection(Const.TABLE_AFFAIRS)
            //.whereEqualTo("categoryId", itemId).limit(Const.LIMIT)
            .get()
            .addOnSuccessListener { documents ->
                swipeRefresh.isRefreshing = false

                if (documents.size() > 0) {
                    for (postSnapshot in documents) {
                        val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                        list1.add(model)
                    }
                    lastResult = documents.documents.get(documents.size() - 1)
                    adapter.updateList(list1)
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.e(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    private fun setRecyclerView() {
        adapter = AffairAdapter(context!!, this, false)
        val layoutManager = LinearLayoutManager(context)
        rvRecords.layoutManager = layoutManager
        rvRecords.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this)

    }

    private fun onLoadMore() {
        if (!isSearching && pb_progress.visibility != View.VISIBLE) {
            if (!Connectivity.isConnected(context!!)) {
                return
            }
            pb_progress.visibility = View.VISIBLE
            fireStorePagination()
        }
    }

    override fun onItemClick(type: Int, item: VideoModel) {
        when (type) {

            Const.TYPE_PAGINATION -> {
                onLoadMore()
            }
            Const.TYPE_CLICKED -> {
                val intent = Intent(context, YouTubeActivity::class.java)
                intent.putExtra(Const.VIDEO_MODEL, Gson().toJson(item))
                startActivity(intent)
            }
        }
    }

    private var viewStub: ViewStub? = null

    private fun showHideEmptyState(dataExits: Boolean) {
        if (dataExits) {
            if (null != viewStub) {
                rvRecords.visibility = View.VISIBLE
                viewStub?.visibility = View.GONE
                viewStub?.findViewById<LottieAnimationView>(R.id.img)?.pauseAnimation()
            }
        } else {
            if (null == viewStub) {
                viewStub = layoutView?.findViewById<ViewStub>(R.id.viewStub)!!
                viewStub?.inflate()
            }
            rvRecords.visibility = View.GONE
            viewStub?.visibility = View.VISIBLE
            viewStub?.findViewById<LottieAnimationView>(R.id.img)?.playAnimation()
        }
    }

    companion object {
        val TAG = AffairsFragment::class.java.simpleName
        fun newInstance(): AffairsFragment {
            val fragment = AffairsFragment()
            // fragment.item = item
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
            adapter.updateList(listSearch)
            showHideEmptyState(listSearch.size > 0)
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
