package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.covidbeads.app.assesment.util.shortToast
import com.e.VideoFragment
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.ui.activity.MainActivity
import com.edu.apnipadhai.ui.adapter.CourseAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.FirebaseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class CourseFragment : BaseFragment(), ListItemClickListener<Category> {
    private lateinit var item: Category
    private var adapter: CourseAdapter? = null
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
        setUpToolbar()
        setRecyclerView()
        //saveCategories()
        fetchData()
        return layoutView
    }

    private fun setUpToolbar() {
        layoutView?.findViewById<AppCompatTextView>(R.id.tvTitle)?.text =
            getString(R.string.title_select_course)
        layoutView?.findViewById<View>(R.id.ivBack)?.setOnClickListener { onBackPressed() }
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setupToolbar()
         setRecyclerView()
         fetchSuppliers()
     }*/

    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvList)
        adapter = CourseAdapter(this)
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


    override fun onRefresh() {
        fetchData()
    }

    private fun fetchData() {
        CustomLog.e(TAG, "fetchData")

        if (!Connectivity.isConnected(context)) {
            swipeRefresh?.isRefreshing = false
            swipeRefresh?.isEnabled = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        swipeRefresh?.isEnabled = true
        swipeRefresh?.setRefreshing(true)


        FirebaseFirestore.getInstance().collection("cities")
            //.whereEqualTo("id", 0)
            .get()
            .addOnSuccessListener { documents ->
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false

                val list: ArrayList<Category?> = ArrayList()
                for (postSnapshot in documents) {
                    val user: Category? = postSnapshot.getValue(Category::class.java)
                    list.add(user)
                }
                adapter!!.setList(list)

                for (document in documents) {
                    CustomLog.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }



        FirebaseData.getCategories(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                CustomLog.e("Upload error:", error.message)


            }

            override fun onDataChange(snapshot: DataSnapshot) {
                swipeRefresh?.setRefreshing(false)
                swipeRefresh?.isEnabled = false
                val list: ArrayList<Category?> = ArrayList()
                for (postSnapshot in snapshot.getChildren()) {
                    val user: Category? = postSnapshot.getValue(Category::class.java)
                    list.add(user)
                }
                adapter!!.setList(list)
            }
        })
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

    override fun onItemClick(item: Category) {
        (activity!! as MainActivity).openFragment(
            VideoFragment.newInstance(item)
        )
    }
}
