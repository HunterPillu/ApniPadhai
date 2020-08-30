package com.meripadhai.ui.activity

import android.os.Bundle
import com.meripadhai.R
import com.meripadhai.ui.fragments.BookamrksFragment
import com.meripadhai.ui.fragments.CourseFragment
import com.meripadhai.ui.fragments.UserFragment
import com.meripadhai.ui.fragments.UserListFragment
import com.meripadhai.utils.Const
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
                openFragment(UserFragment.newInstance(true))
            }
            Const.SCREEN_COURSE -> {
                val isUpdate = intent.getBooleanExtra("is_update", false)
                openFragment(CourseFragment.newInstance(isUpdate))
            }
            Const.SCREEN_BOOKMARK -> {
                openFragment(BookamrksFragment.newInstance(true))
            }
            else -> {
            }
        }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
