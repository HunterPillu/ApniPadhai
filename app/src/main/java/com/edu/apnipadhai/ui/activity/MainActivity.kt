package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.e.CategoryFragment
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.ui.fragments.SettingsFrag
import com.edu.apnipadhai.ui.fragments.UserFragment
import com.edu.apnipadhai.utils.FirebaseData
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    private lateinit var navView: BottomNavigationView
    private lateinit var tvTitle: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseData.init()
        init()
        setupNavigation()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        findViewById<View>(R.id.ivBack).setOnClickListener { onBackPressed() }
    }


    override fun onBackPressed() {

        if (R.id.home != navView.selectedItemId) {
            navView.setSelectedItemId(R.id.home)
            return
        }
        super.onBackPressed()
    }


    private lateinit var fragment1: BaseFragment
    private lateinit var fragment2: BaseFragment
    private lateinit var fragment3: BaseFragment
    private lateinit var fragment4: BaseFragment
    private lateinit var active: BaseFragment

    private fun setupNavigation() {
        fragment1 = CategoryFragment.newInstance()
        fragment2 = UserFragment()
        fragment3 = fragment2
        fragment4 = SettingsFrag()
        active = fragment1
        supportFragmentManager.beginTransaction().add(R.id.container, fragment4, "4")
            .hide(fragment4).commit();
        //supportFragmentManager.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        supportFragmentManager.beginTransaction().add(R.id.container, fragment2, "3")
            .hide(fragment2).commit();
        supportFragmentManager.beginTransaction().add(R.id.container, fragment1, "1").commit();

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                }
                R.id.news -> {
                    Toast.makeText(this, "Photos selected", Toast.LENGTH_SHORT).show()
                }
                R.id.current -> {
                    Toast.makeText(this, "More selected", Toast.LENGTH_SHORT).show()
                }
                R.id.setting -> {
                    supportFragmentManager.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4
                }
            }

            true
        }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}