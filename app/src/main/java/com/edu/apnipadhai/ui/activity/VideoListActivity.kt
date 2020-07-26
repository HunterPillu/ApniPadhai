package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.ui.fragments.VideoFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_toolbar.*


class VideoListActivity : BaseActivity() {



    // private var ivBack: ImageView? = null
   // private lateinit var tvTitle: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        // toolbar = findViewById(R.id.toolbar)
        //ivBack = findViewById(R.id.ivBack)
       // tvTitle = findViewById(R.id.tvTitle)

        val item = intent.getStringExtra("item")!!
        openFragment(VideoFragment.newInstance(Gson().fromJson(item, Category::class.java)))
        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

    /* companion object {
         fun openActivity(item: Category) {
             Intent()
         }
     }*/

}
