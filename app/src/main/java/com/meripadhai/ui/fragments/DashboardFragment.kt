package com.meripadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.covidbeads.app.assesment.util.PrefUtil
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.Course
import com.meripadhai.model.Dashboard
import com.meripadhai.model.VideoModel
import com.meripadhai.ui.activity.VideoListActivity
import com.meripadhai.ui.adapter.DashboardAdapter
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class DashboardFragment : BaseFragment(), ListItemClickListener<Int, Dashboard> {
    private lateinit var adapter: DashboardAdapter

    //private lateinit var videoAdapter: VideoAdapter
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

        layoutView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        canBeCalledOnResume = false
        Handler().postDelayed({ initView() }, 50)
        return layoutView
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
                view?.findViewById<AppCompatTextView>(R.id.tvNoData)
                    ?.setText(R.string.no_data_for_course)
                view?.findViewById<LottieAnimationView>(R.id.img)?.playAnimation()
            }
            rvRecords.visibility = View.GONE
            viewStub?.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (canBeCalledOnResume) {
            fetchRecentVideos()
            //fetchRecentVideos()
        } else {
            canBeCalledOnResume = true
        }
    }

    //var isLoaded = false
    fun initView() {
        /* if (isLoaded) {
             return
         }
         isLoaded = true*/
        /*layoutView?.findViewById<View>(R.id.cvMD)
            ?.setOnClickListener { openScreen(Const.SCREEN_USER_INVITE) }*/
        setCourseList()
        //setRecentList()
        fetchRecentVideos()
        //fetchRecentVideos()
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setupToolbar()
         setRecyclerView()
         fetchSuppliers()
     }*/

    private lateinit var rvRecords: RecyclerView
    private fun setCourseList() {
        rvRecords = layoutView!!.findViewById<RecyclerView>(R.id.rvList)
        adapter = DashboardAdapter(context!!, this)
        rvRecords.adapter = adapter
        //swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
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
                adapter.clearList()
                if (documents.isEmpty) {
                    showHideEmptyState(false)
                    return@addOnSuccessListener
                } else {
                    showHideEmptyState(true)
                }

                val list1 = ArrayList<VideoModel>()
                for (postSnapshot in documents) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    list1.add(model)
                }

                val course = Course()
                course.courseType = Const.COURSE_HEADER
                course.name = getString(R.string.recent_upload)

                adapter.addItem(Dashboard().apply {
                    this.course = course
                    videoList = list1
                })

                // if recent data is available only then call fetch subject wise data
                fetchData()
            }
            .addOnFailureListener { exception ->
                CustomLog.e(
                    VideoFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }


    override fun onRefresh() {
        fetchRecentVideos()
    }

    fun fetchData() {
        val parentID = PrefUtil.getCourseId(context!!)
        CustomLog.d(TAG, "crse : courseId : $parentID")
        FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                //fetch all course
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                    //course.fKey = postSnapshot.id

                    //now fetch first 5 videos for this course
                    val dashboard = Dashboard()
                    dashboard.course = course
                    fetchData(dashboard)
                }
            }
    }

    fun fetchData(dashboard: Dashboard) {
        /*if (course.videoCount == 0) {
            return
        }*/
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            .whereEqualTo(Const.FIELD_CATEGORY_ID, "${dashboard.course?.id}")
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->

                val list = ArrayList<VideoModel>()
                for (postSnapshot in documents) {
                    val video: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    //course.fKey = postSnapshot.id
                    list.add(video)
                }

                //combine course and videolist , then attach to recyclerView
                if (list.size > 0) {
                    dashboard.videoList = list
                    adapter.addItem(dashboard)
                }
            }
    }


    companion object {
        internal val TAG = DashboardFragment::class.java.simpleName
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onItemClick(type: Int, item: Dashboard) {
        when (type) {
            Const.TYPE_CLICKED_2 -> {
                val intent = Intent(context!!, VideoListActivity::class.java).apply {
                    putExtra("item", Gson().toJson(item.course))
                }
                startActivity(intent)
            }
            else -> {
                //do nothing
            }
        }

    }
}
