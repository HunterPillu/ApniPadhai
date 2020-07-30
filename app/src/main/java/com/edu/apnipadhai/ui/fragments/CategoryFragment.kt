package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.covidbeads.app.assesment.util.PrefUtil
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.ui.activity.VideoListActivity
import com.edu.apnipadhai.ui.adapter.CategoryAdapter
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_course_topic.*


class CategoryFragment : BaseFragment(), ListItemClickListener<Int, Category> {
    private lateinit var adapter: CategoryAdapter
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
        setRecyclerView()
        return layoutView
    }

    override fun onStart() {
        super.onStart()
        CustomLog.d("test_tag", "onStart")
    }

    override fun onResume() {
        super.onResume()
        CustomLog.d("test_tag", "onResume")
        fetchData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomLog.d("test_tag", "1111111111111111111111111")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        CustomLog.d("test_tag", "2222222222222222222222222")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        CustomLog.d("test_tag", "33333333333333333333333333333")
    }


    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setupToolbar()
         setRecyclerView()
         fetchSuppliers()
     }*/

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvSuppliers)
        adapter = CategoryAdapter(this)
        rvRecords?.adapter = adapter
        //swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        //swipeRefresh?.setOnRefreshListener(this)
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
                llCourseTopic.visibility = View.VISIBLE
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
