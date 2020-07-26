package com.edu.apnipadhai.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edu.apnipadhai.callbacks.FragmentEventListener

open class BaseFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var TAG = "BaseFragment"
    var layoutView: View? = null
    private var listener: FragmentEventListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FragmentEventListener
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
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

    fun openFragment(fragment: BaseFragment) {
        listener?.openFragment(fragment)
    }

    fun updateToolbarTitle(title: String) {
        listener?.updateToolbarTitle(title)
    }
}
