package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.ui.adapter.EditCategoryAdapter
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_category.*

class EditCategoryFragment : BaseFragment(), ListItemClickListener<Int, Course> {

    private lateinit var adapter: EditCategoryAdapter
    private var list: ArrayList<Course> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_edit_category, container, false)
        //saveCategories()
        //fetchData(0)
        layoutView?.findViewById<View>(R.id.bNext)?.setOnClickListener { }
        setRecyclerView()

        return layoutView
    }

    @ExperimentalStdlibApi
    override fun onBackPressed() {
        if (list.isEmpty()) {
            super.onBackPressed()
        } else {
            list.removeLast()
            updatePath()
        }
    }

    private fun updatePath() {
        if (list.isEmpty()) {
            tvPath.text = "${getString(R.string.title_select_course)} >> "
        }
    }

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvList)
        adapter = EditCategoryAdapter(context!!, this)
        rvRecords?.adapter = adapter
    }

    fun getCategory() {
        var parentID = 0
        if (!list.isEmpty()) {
            parentID = list.last().id
        }

        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                val list1 = ArrayList<Course>()

                val course = Course().apply {
                    id = -1
                    name = ""
                    courseType = Const.COURSE_HEADER
                    parentId = parentID
                }
                list1.add(course)
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                    course.fKey = postSnapshot.id
                    list1.add(course)
                }

                adapter.setList(list1)

            }
            .addOnFailureListener { exception ->
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }


    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.add_Category))
        getCategory()
    }


    fun deleteCourse(item: Course) {
        FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
            .document(item.fKey!!).delete().addOnCompleteListener { getCategory() }

    }

    override fun onItemClick(type: Int, item: Course) {
        when (type) {
            Const.TYPE_CLICKED -> {
                if (list.size > 1) {
                    return
                }
                list.add(item)
                getCategory()
            }
            Const.TYPE_ADD, Const.TYPE_EDIT -> {
                openFragment(UpdateCategoryFragment.newInstance(item))
            }
            Const.TYPE_DELETE -> {
                deleteCourse(item)
            }
        }
    }

    companion object {
        internal val TAG = EditCategoryFragment::class.java.simpleName

        fun newInstance(): EditCategoryFragment {
            return EditCategoryFragment()
        }
    }


}
