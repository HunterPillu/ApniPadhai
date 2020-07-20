package com.edu.apnipadhai.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.e.CategoryFragment
import com.e.VideoFragment
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.ui.activity.BaseActivity
import com.edu.apnipadhai.ui.activity.MainActivity
import com.edu.apnipadhai.ui.adapter.CourseAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class CourseFragment : BaseFragment(), ListItemClickListener<Course> {
    var map: HashMap<Int, Course> = HashMap<Int, Course>()

    private lateinit var adapter: CourseAdapter
    private var swipeRefresh: SwipeRefreshLayout? = null
    private lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_course, container, false)
        setUpToolbar()
        setRecyclerView()
        //saveCategories()
        fetchData(0)
        layoutView?.findViewById<View>(R.id.bNext)?.setOnClickListener { goNext() }
        return layoutView
    }

    private fun setUpToolbar() {

        mainActivity.tvTitle.text = getString(R.string.title_select_course)
        mainActivity.ivBack.setOnClickListener{ onBackPressed()}

//        layoutView?.findViewById<AppCompatTextView>(R.id.tvTitle)?.text =
//            getString(R.string.title_select_course)
//        layoutView?.findViewById<View>(R.id.ivBack)?.setOnClickListener { onBackPressed() }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setupToolbar()
         setRecyclerView()
         fetchSuppliers()
     }*/

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvList)
        adapter = CourseAdapter(context, this)
        rvRecords?.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        //swipeRefresh?.setOnRefreshListener(this)
    }

    fun saveCategories() {
        val mFirestore = FirebaseFirestore.getInstance();

        // Get a reference to the restaurants collection

        // Get a reference to the restaurants collection
        val catList = mFirestore.collection("category")

        // Add a new document to the restaurants collection
        catList.add(Category(1, "Grades", 0).toMap())
        catList.add(Category(2, "Exam Preparation", 0).toMap())
        catList.add(Category(3, "6th", 1).toMap())
        catList.add(Category(4, "7th", 1).toMap())
        catList.add(Category(5, "8th", 1).toMap())
        catList.add(Category(6, "9th", 1).toMap())
        catList.add(Category(7, "10th", 1).toMap())
        catList.add(Category(8, "11th Science", 1).toMap())
        catList.add(Category(9, "12th Science", 1).toMap())
        catList.add(Category(10, "11th Commerce", 1).toMap())
        catList.add(Category(11, "12th Commerce", 1).toMap())
        catList.add(Category(12, "UPSC", 2).toMap())
        catList.add(Category(13, "SSC", 2).toMap())
        catList.add(Category(14, "State Level", 2).toMap())
        catList.add(Category(15, "Physics", 8).toMap())
        catList.add(Category(16, "Chemistry", 8).toMap())
        catList.add(Category(17, "Biology", 8).toMap())
        catList.add(Category(18, "English", 8).toMap())
        catList.add(Category(19, "Maths", 8).toMap())
        catList.add(Category(20, "English", 8).toMap())

    }

    fun goNext() {
        val courseId = adapter.selectedCourseId()
        if (courseId == -1) {
            Utils.showToast(context,getString(R.string.error_select_course))
            return
        }
        updateSelectedCourse(courseId)
        (activity as BaseActivity).openFragment(CategoryFragment.newInstance())
    }


    fun updateSelectedCourse(courseId: Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["courseId"] = courseId
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid).set(map, SetOptions.merge())

    }

    private fun fetchData(parentID: Int?) {
        CustomLog.e(TAG, "fetchData")

        if (!Connectivity.isConnected(context)) {
            swipeRefresh?.isRefreshing = false
            swipeRefresh?.isEnabled = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        swipeRefresh?.isEnabled = true
        swipeRefresh?.setRefreshing(true)


        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                val isParentHeader = (parentID == 0)
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false

                val list: ArrayList<Course> = ArrayList()

                if (!isParentHeader) {
                    list.add(map[parentID]!!)
                }
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)

                    if (isParentHeader) {
                        course.courseType = Const.COURSE_HEADER
                        map[course.id] = course
                        fetchData(course.id)
                    } else {
                        list.add(course)
                    }
                }
                if (!isParentHeader)
                    adapter.setList(list)

            }
            .addOnFailureListener { exception ->
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    companion object {
        const val TAG = "CourseFragment"
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): CourseFragment {
            return CourseFragment()
        }
    }

    override fun onItemClick(item: Course) {
        (activity!! as MainActivity).openFragment(
            VideoFragment.newInstance(item)
        )
    }
}
