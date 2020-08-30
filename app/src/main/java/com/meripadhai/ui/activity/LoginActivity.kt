package com.meripadhai.ui.activity

import android.os.Bundle
import com.meripadhai.R
import com.meripadhai.ui.fragments.UserFragment
import kotlinx.android.synthetic.main.custom_toolbar.*


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)
        openFragment(UserFragment.newInstance(false))
        handleNavigation(ivBack)

    }


    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
