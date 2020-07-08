package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

open class BaseFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var TAG = "BaseFragment"
    var layoutView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun onBackPressed() {
        //Log.d(TAG, "total entry = " + activity!!.supportFragmentManager.backStackEntryCount)
        activity?.onBackPressed();
        /*if (activity!!.supportFragmentManager.backStackEntryCount > 1) {
            activity?.supportFragmentManager?.popBackStack()
        } else {
            activity?.finish();
        }*/
    }

    override fun onRefresh() {
        //to be implemented on child childFragment : Supplier, Chat fragment
    }
}
