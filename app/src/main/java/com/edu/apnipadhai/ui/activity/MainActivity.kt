package com.edu.apnipadhai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.ui.fragments.CategoryFragment
import com.edu.apnipadhai.ui.fragments.SettingsFrag
import com.edu.apnipadhai.ui.fragments.UserFragment
import com.edu.apnipadhai.utils.FirebaseData
import com.edu.apnipadhai.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.custom_toolbar.*


class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName
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
        ivBack.setOnClickListener { onBackPressed() }
        ivOption.visibility = View.VISIBLE
        ivOption.setOnClickListener { openAdminScreen() }
    }

    private fun openAdminScreen() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
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

    //private lateinit var fragment3: BaseFragment
    private lateinit var fragment3: BaseFragment
    private lateinit var active: BaseFragment

    private fun setupNavigation() {
        fragment1 = CategoryFragment.newInstance()
        fragment2 = UserFragment()
        //fragment3 = fragment2
        fragment3 = SettingsFrag()
        active = fragment1

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
               /* R.id.news -> {
                    Toast.makeText(this, "Photos selected", Toast.LENGTH_SHORT).show()
                }*/
                R.id.current -> {
                    Utils.showToast(this, getString(R.string.work_in_progress))
                }
                R.id.setting -> {
                    updateToolbarTitle(getString(R.string.setting))
                    supportFragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3
                }
            }
            true
        }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}