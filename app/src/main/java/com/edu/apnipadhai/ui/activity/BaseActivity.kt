package com.edu.apnipadhai.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.utils.CustomLog
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private val TAG = "BaseActivity"

    override fun onBackPressed() {
        val c = supportFragmentManager.backStackEntryCount
        CustomLog.d(TAG, "total entry = $c")
        if (c > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun showSnackbar(stringRes: Int) {
        Snackbar.make(findViewById(R.id.root)!!, stringRes, Snackbar.LENGTH_LONG).show()
    }

    private fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    fun openFragment(fragment: BaseFragment) {
        //this.fragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment!!, Integer.toString(getFragmentCount()))
            .addToBackStack(null).commit()
    }

    private fun getFragmentAt(index: Int): BaseFragment? {
        return (if (getFragmentCount() > 0) supportFragmentManager.findFragmentByTag(
            Integer.toString(
                index
            )
        ) as BaseFragment? else null)
    }

    fun getCurrentFragment(): BaseFragment? {
        return getFragmentAt(getFragmentCount() - 1)
    }
}