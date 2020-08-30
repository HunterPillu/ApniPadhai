package com.meripadhai.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import com.meripadhai.R
import com.meripadhai.ui.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var navView: BottomNavigationView
    private lateinit var tvTitle: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setupNavigation()
    }

    private fun init() {
        tvTitle = findViewById(R.id.tvTitle)
        ivBack.setOnClickListener { onBackPressed() }
    }


    override fun onBackPressed() {

        if (R.id.home != navView.selectedItemId) {
            navView.selectedItemId = R.id.home
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
        fragment1 = DashboardFragment.newInstance()
        fragment2 = AffairsFragment.newInstance()
        fragment3 = BookamrksFragment.newInstance(false)
        fragment4 = SettingsFrag()
        active = fragment1

        supportFragmentManager.beginTransaction().add(R.id.container, fragment4, "4")
            .hide(fragment4).commit();
        supportFragmentManager.beginTransaction().add(R.id.container, fragment3, "3")
            .hide(fragment3).commit();
        supportFragmentManager.beginTransaction().add(R.id.container, fragment2, "2")
            .hide(fragment2).commit();
        supportFragmentManager.beginTransaction().add(R.id.container, fragment1, "1").commit();

        updateToolbarTitle(getString(R.string.home))

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    updateToolbarTitle(getString(R.string.home))
                    supportFragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1
                }

                R.id.current -> {
                    updateToolbarTitle(getString(R.string.current_affairs))
                    supportFragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2
                }
                R.id.bookmark -> {
                    updateToolbarTitle(getString(R.string.bookmarks))
                    supportFragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3
                }
                R.id.setting -> {
                    updateToolbarTitle(getString(R.string.setting))
                    supportFragmentManager.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4
                }
            }
            //active.initViews()
            true
        }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}