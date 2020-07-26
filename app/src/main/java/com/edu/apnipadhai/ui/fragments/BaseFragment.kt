package com.edu.apnipadhai.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.FragmentEventListener
import com.edu.apnipadhai.utils.Const
import com.google.firebase.auth.FirebaseAuth

open class BaseFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var TAG = "BaseFragment"
    var layoutView: View? = null
    private lateinit var listener: FragmentEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FragmentEventListener
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
        listener.openFragment(fragment)
    }

    fun updateToolbarTitle(title: String) {
        listener.updateToolbarTitle(title)
    }

    fun signoutDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.signout)
        builder.setMessage(R.string.signout_msg)
            .setPositiveButton(R.string.yes) { dialog, id ->
                FirebaseAuth.getInstance().signOut()
                activity!!.finish()
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    open fun getYouTubeIntent(): Intent? {
        var intent: Intent
        val url = Const.YOUTUBE_URL
        try {
            // get the Twitter app if possible
            val pm = context!!.packageManager
            pm.getPackageInfo(Const.YOUTUBE_PACKAGE, 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage(Const.YOUTUBE_PACKAGE)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        }
        return intent
    }

}
