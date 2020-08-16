package com.edu.apnipadhai.ui.fragments

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.FragmentEventListener
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.ui.activity.CommonActivity
import com.edu.apnipadhai.utils.Const
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var TAG = "BaseFragment"
    var layoutView: View? = null
    private var listener: FragmentEventListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FragmentEventListener
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    open fun onBackPressed() {
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

    fun openScreen(type: Int) {
        startActivity(Intent(context!!, CommonActivity::class.java).apply {
            putExtra(Const.EXTRA_TYPE, type)
        })
    }

    fun sendGameInvite(item: User) {
        listener?.onInviteOpponent(item)
    }

}
