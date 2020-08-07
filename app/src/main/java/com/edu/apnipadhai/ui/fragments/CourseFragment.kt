package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.PrefUtil
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.ui.activity.MainActivity
import com.edu.apnipadhai.ui.adapter.CourseAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class CourseFragment : BaseFragment() {
    private var isUpdate: Boolean = false
    var map: HashMap<Int, Course> = HashMap<Int, Course>()

    private lateinit var adapter: CourseAdapter
    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_course, container, false)
        setRecyclerView()
        //saveCategories()
        fetchData(0)
        layoutView?.findViewById<View>(R.id.bNext)?.setOnClickListener { goNext() }
        return layoutView
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_select_course))
    }

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvList)
        adapter = CourseAdapter(context!!)
        rvRecords?.adapter = adapter
        swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
    }

    fun goNext() {
        val courseId = adapter.selectedCourseId()
        if (courseId == -1) {
            Utils.showToast(context, getString(R.string.error_select_course))
            return
        }
        updateSelectedCourse(courseId)

        if (isUpdate) {
            onBackPressed()
        } else {
            val intent = Intent(activity, MainActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
        }
    }


    fun updateSelectedCourse(courseId: Int) {

        PrefUtil.saveCourseId(context!!, courseId)

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


        FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                val isParentHeader = (parentID == 0)
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false
                val currentCourseId = PrefUtil.getCourseId(context!!)
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
                        if (course.id == currentCourseId) {
                            course.selected = true
                        }
                        list.add(course)
                    }
                }
                if (!isParentHeader)
                    adapter.updateList(list)

            }
            .addOnFailureListener { exception ->
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false
                CustomLog.e(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    companion object {
        const val TAG = "CourseFragment"
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(isUpdate: Boolean): CourseFragment {
            return CourseFragment().apply {
                this.isUpdate = isUpdate
            }
        }
    }
}
