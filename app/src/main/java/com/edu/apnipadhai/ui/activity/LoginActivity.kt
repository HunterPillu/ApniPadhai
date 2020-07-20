package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.SettingsFrag
import com.edu.apnipadhai.ui.fragments.UserFragment


class LoginActivity : BaseActivity() {


    private  var toolbar: Toolbar? = null
    private  var ivBack: ImageView? = null
    private  var tvTitle: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        toolbar = findViewById(R.id.toolbar)
        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)

        openFragment(UserFragment())
    }

}
