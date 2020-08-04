package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.widget.ImageViewCompat
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
import java.util.*
import kotlin.collections.ArrayList


class VideoFragment : BaseFragment(), ListItemClickListener<Int, VideoModel>,
    MenuClickListener<VideoModel, View>, SearchView.OnQueryTextListener,
    SearchView.OnCloseListener {
    private lateinit var item: Category
    private lateinit var adapter: VideoAdapter
    private lateinit var list1: ArrayList<VideoModel>
    private  var searchString: String? = null
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
        svSearchExpanded.visibility = View.VISIBLE
        ivSearch.visibility = View.VISIBLE
        svSearchExpanded.setOnQueryTextListener(this)
        svSearchExpanded.setOnCloseListener(this)

        ivBack.setOnClickListener {
            activity!!.finish()
        }

        ivSearch.setOnClickListener {
            rlToolBar.visibility = View.GONE
            rlSearch.visibility = View.VISIBLE
            svSearchExpanded.onActionViewExpanded()
        }

        ivBackSearch.setOnClickListener {
            rlToolBar.visibility = View.VISIBLE
            rlSearch.visibility = View.GONE
            svSearchExpanded.onActionViewCollapsed()
            adapter.setList(list1,tvNoData,rvRecords)
        }
//        if (!this.svSearchExpanded.isIconified()) {
//            svSearchExpanded.onActionViewCollapsed()
//            if (adapter.itemCount == 0)
//                adapter.setList(list1, tvNoData, rvRecords)
//        } else
    }

    private fun setRecyclerView() {
        adapter = VideoAdapter(context!!, this, this)
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
    }

    override fun itemClick(item: VideoModel, obj: View) {
        popupMenus(obj)
    }

    override fun onRefresh() {
        fetchData()
        rlToolBar.visibility = View.VISIBLE
        rlSearch.visibility = View.GONE
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
                list1 = ArrayList<VideoModel>()

                for (postSnapshot in documents) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    //course.fKey = postSnapshot.id
                    list1.add(model)
                }

                adapter.setList(list1,tvNoData,rvRecords)
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

    private fun searchResult(query: String?)
    {
        if (!query.isNullOrEmpty() || !query.isNullOrBlank()) {
            searchString = query
            val listSearch: ArrayList<VideoModel?> = ArrayList()
            for (i in 0 until list1.size) {
                if (list1[i].name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT).trim())) {
                    listSearch.add(list1[i])
                }
            }
            adapter.setList(listSearch,tvNoData,rvRecords)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchResult(query)
        return true }

    override fun onQueryTextChange(newText: String?): Boolean { return true }

    override fun onClose(): Boolean { return true }
}
