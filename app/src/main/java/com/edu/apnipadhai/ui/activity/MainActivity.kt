package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e.CategoryFragment
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.FirebaseData

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseData.init()
        // FirebaseData.saveCategory()
        //FirebaseData.saveDummyVideos()

        openFragment(CategoryFragment.newInstance())
    }

    override fun onBackPressed() {
        val c = supportFragmentManager.backStackEntryCount
        CustomLog.d(TAG, "total entry = $c")
        if (c > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }


    private fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    fun openFragment(fragment: BaseFragment?) {
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