package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.ui.fragments.VideoFragment
import com.edu.apnipadhai.utils.CustomLog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_toolbar.*


class VideoListActivity : BaseActivity() {
    internal val TAG = VideoListActivity::class.java.simpleName
    public var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.visibility = View.GONE

        val item = intent.getStringExtra("item")!!
        CustomLog.d(TAG, " crse  item : $item")
        openFragment(VideoFragment.newInstance(Gson().fromJson(item, Category::class.java)))
        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
