package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.CourseFragment
import com.edu.apnipadhai.ui.fragments.SettingsFrag
import com.edu.apnipadhai.utils.FirebaseData
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    private lateinit var navView : BottomNavigationView
    public lateinit var tvTitle : AppCompatTextView
    public lateinit var ivBack : AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseData.init()
        init()
        setupNavigation()
        openFragment(CourseFragment.newInstance())
//        openFragment(CourseFragment.newInstance())

    }

    private fun init()
    {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
    }


    private fun setupNavigation() {
        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(CourseFragment.newInstance())
                    true
                }
                R.id.news -> {
                    Toast.makeText(this, "Photos selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.current -> {
                    Toast.makeText(this, "More selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.setting -> {
                            openFragment(SettingsFrag())
                    true
                }
                else -> true
            }
        }
    }

}