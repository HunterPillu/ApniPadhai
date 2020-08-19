package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BookamrksFragment
import com.edu.apnipadhai.ui.fragments.CourseFragment
import com.edu.apnipadhai.ui.fragments.UserFragment
import com.edu.apnipadhai.ui.fragments.UserListFragment
import com.edu.apnipadhai.utils.Const
import kotlinx.android.synthetic.main.custom_toolbar.*


class CommonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        handleNavigation(ivBack)
        openRequiredFragment()
    }

    private fun openRequiredFragment() {
        val type = intent.getIntExtra(Const.EXTRA_TYPE, -1)
        when (type) {
            Const.SCREEN_USER_INVITE -> {
                openFragment(UserListFragment())
            }
            Const.SCREEN_USER -> {
                openFragment(UserFragment())
            }
            Const.SCREEN_COURSE -> {
                val isUpdate = intent.getBooleanExtra("is_update", false)
                openFragment(CourseFragment.newInstance(isUpdate))
            }
            Const.SCREEN_BOOKMARK -> {
                openFragment(BookamrksFragment())
            }
            else -> {
            }
        }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
