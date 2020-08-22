package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.UserFragment
import kotlinx.android.synthetic.main.custom_toolbar.*


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)
        openFragment(UserFragment())
        handleNavigation(ivBack)

    }


    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
