package com.meripadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.covidbeads.app.assesment.util.PrefUtil
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.Category
import com.meripadhai.model.Course
import com.meripadhai.model.VideoModel
import com.meripadhai.ui.activity.VideoListActivity
import com.meripadhai.ui.activity.YouTubeActivity
import com.meripadhai.ui.adapter.CategoryAdapter
import com.meripadhai.ui.adapter.VideoAdapter
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : BaseFragment(), ListItemClickListener<Int, Category> {
    private lateinit var adapter: CategoryAdapter
    private lateinit var videoAdapter: VideoAdapter
    private var canBeCalledOnResume = true
    // private var swipeRefresh: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_category, container, false)
        canBeCalledOnResume = false
        Handler().postDelayed({ initView() }, 50)
        return layoutView
    }

    override fun onResume() {
        super.onResume()
        if (canBeCalledOnResume) {
            fetchData()
            fetchRecentVideos()
        } else {
            canBeCalledOnResume = true
        }
    }

    fun initView() {
        layoutView?.findViewById<View>(R.id.cvMD)
            ?.setOnClickListener { openScreen(Const.SCREEN_USER_INVITE) }
        setCourseList()
        setRecentList()
        fetchData()
        fetchRecentVideos()
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setupToolbar()
         setRecyclerView()
         fetchSuppliers()
     }*/

    private fun setCourseList() {
        val view = layoutView?.findViewById<View>(R.id.ll2)!!
        val rvRecords: RecyclerView? = view.findViewById<RecyclerView>(R.id.rvList)
        adapter = CategoryAdapter(this)
        rvRecords?.adapter = adapter
        //swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        //swipeRefresh?.setOnRefreshListener(this)
    }

    private fun setRecentList() {
        val view = layoutView?.findViewById<View>(R.id.ll1)!!
        val header = view.findViewById<AppCompatTextView>(R.id.tvHeader)
        header.setText(R.string.recent_upload)
        val params: LinearLayoutCompat.LayoutParams =
            header.layoutParams as LinearLayoutCompat.LayoutParams
        val margin = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            12f,
            context!!.resources.displayMetrics
        ).toInt())

        params.setMargins(
            margin, margin, 0, 0
        )
        header.layoutParams = params
        val rv: RecyclerView = view.findViewById<RecyclerView>(R.id.rvList)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(rv)
        videoAdapter = VideoAdapter(
            context!!,
            object : ListItemClickListener<Int, VideoModel> {
                override fun onItemClick(type: Int, item: VideoModel) {
                    val intent = Intent(context, YouTubeActivity::class.java)
                    intent.putExtra(Const.VIDEO_MODEL, Gson().toJson(item))
                    startActivity(intent)
                }
            },
            2
        )
        videoAdapter.pagingEnabled = false
        rv.adapter = videoAdapter

        //swipeRefresh = layoutView?.findViewById<View> (R.id.swipeRefresh) as SwipeRefreshLayout
        //swipeRefresh?.setOnRefreshListener(this)
    }

    private fun fetchRecentVideos() {
        val courseId = PrefUtil.getCourseId(context!!)
        if (!Connectivity.isConnected(context!!)) {
            return
        }

        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            .whereEqualTo("courseId", courseId)
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val list1 = ArrayList<VideoModel>()

                for (postSnapshot in documents) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    list1.add(model)
                }

                videoAdapter.updateList(list1)
                ll1.visibility = if (documents.size() > 0) View.VISIBLE else View.GONE
            }
            .addOnFailureListener { exception ->
                CustomLog.e(
                    VideoFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }


    override fun onRefresh() {
        fetchData()
    }

    fun fetchData() {
        val parentID = PrefUtil.getCourseId(context!!)
        CustomLog.d(TAG, "crse : courseId : $parentID")
        FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                val list1 = ArrayList<Course>()

                /* val course = Course().apply {
                     id = -1
                     name = ""
                     courseType = Const.COURSE_HEADER
                     parentId = parentID
                 }
                 list1.add(course)*/
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                    //course.fKey = postSnapshot.id
                    list1.add(course)
                }

                adapter.setList(list1)
                ll2.visibility = View.VISIBLE
                // ll1.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                CustomLog.w(
                    TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }


    companion object {
        internal val TAG = CategoryFragment::class.java.simpleName
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): CategoryFragment {
            return CategoryFragment()
        }
    }

    override fun onItemClick(type: Int, item: Category) {
        //openFragment(VideoFragment.newInstance(item))
        val intent = Intent(context!!, VideoListActivity::class.java).apply {
            putExtra("item", Gson().toJson(item))
        }
        startActivity(intent)
    }
}
