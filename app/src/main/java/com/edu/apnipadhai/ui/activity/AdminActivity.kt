package com.edu.apnipadhai.ui.activity

import android.os.Bundle
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.fragments.AdminFragment
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.item_video.tvTitle


class AdminActivity : BaseActivity() {


    /* private var toolbar: Toolbar? = null
     private var ivBack: ImageView? = null
     private var tvTitle: AppCompatTextView? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)

        /* toolbar = findViewById(R.id.toolbar)
         ivBack = findViewById(R.id.ivBack)
         tvTitle = findViewById(R.id.tvTitle)*/
        ivBack.setOnClickListener { onBackPressed() }
        openFragment(AdminFragment.newInstance())
    }

    override fun updateToolbarTitle(title: String) {
        tvTitle.text = title
    }

}
