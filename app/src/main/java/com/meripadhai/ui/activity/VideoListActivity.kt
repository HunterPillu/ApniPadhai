package com.meripadhai.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.meripadhai.R
import com.meripadhai.model.Category
import com.meripadhai.ui.fragments.VideoFragment
import com.meripadhai.utils.CustomLog
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
