package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.ui.activity.VideoListActivity
import com.edu.apnipadhai.ui.adapter.CategoryAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.FirebaseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_course_topic.*


class CategoryFragment : BaseFragment(), ListItemClickListener<Category> {
    private lateinit var item: Category
    private var adapter: CategoryAdapter? = null
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
        fetchData()
        return layoutView
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

    private fun fetchData() {
        CustomLog.e(TAG, "fetchSuppliers")

        if (!Connectivity.isConnected(context)) {
            //swipeRefresh!!.isRefreshing = false
            shortToast(getString(R.string.no_internet_connection))
            return
        }
        //swipeRefresh?.setRefreshing(true)

        FirebaseData.getCategories(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                CustomLog.e("Upload error:", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val list: ArrayList<Category?> = ArrayList()
                for (postSnapshot in snapshot.getChildren()) {
                    val user: Category? = postSnapshot.getValue(Category::class.java)
                    list.add(user)
                }
                adapter!!.setList(list)
                llCourseTopic.visibility = View.VISIBLE
            }
        })
    }

    companion object {
        val TAG = "CategoryFragment"
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): CategoryFragment {
            return CategoryFragment()
        }
    }

    override fun onItemClick(item: Category) {
        //openFragment(VideoFragment.newInstance(item))
        val intent = Intent(context!!, VideoListActivity::class.java).apply {
            putExtra("item", Gson().toJson(item))
        }

        startActivity(intent)

    }
}
