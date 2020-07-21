package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.ui.activity.MainActivity
import com.edu.apnipadhai.ui.adapter.CourseAdapter
import com.edu.apnipadhai.ui.adapter.SettingsAdapter


class SettingsFrag : BaseFragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: SettingsAdapter
    var rvSettings: RecyclerView? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_settings, container, false)
        setRecyclerView()
        return layoutView
    }

//    private fun setUpToolbar() {
//        mainActivity.tvTitle.text = getString(R.string.setting)
//        mainActivity.ivBack.setOnClickListener { onBackPressed() }
//    }

    private fun setRecyclerView() {
        val list = ArrayList<Setting>()


        list.add(Setting(R.drawable.bookmark,getString(R.string.bookmrk)))
        list.add(Setting(R.drawable.notification,getString(R.string.notification)))
        list.add(Setting(R.drawable.share,getString(R.string.shareapp)))
        list.add(Setting(R.drawable.touch,getString(R.string.Subscribe)))
        list.add(Setting(R.drawable.phone,getString(R.string.contact)))
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvSetting)
        adapter = SettingsAdapter(list)
        rvRecords?.adapter = adapter
    }


}