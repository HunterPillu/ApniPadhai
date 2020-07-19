package com.edu.apnipadhai.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.UserFragment


class LoginActivity : BaseActivity() {


    private  var toolbar: androidx.appcompat.widget.Toolbar? = null
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
