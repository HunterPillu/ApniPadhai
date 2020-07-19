package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e.CategoryFragment
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.BaseFragment
import com.edu.apnipadhai.ui.fragments.CourseFragment
import com.edu.apnipadhai.utils.Const.COURSE_SELECT
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.FirebaseData

class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseData.init()
        // FirebaseData.saveCategory()
        //FirebaseData.saveDummyVideos()

        openFragment(CourseFragment.newInstance())
       /* if(intent.getBooleanExtra(COURSE_SELECT,false)){
            openFragment(CourseFragment.newInstance())
        }else {
            openFragment(CategoryFragment.newInstance())
        }*/
    }





}