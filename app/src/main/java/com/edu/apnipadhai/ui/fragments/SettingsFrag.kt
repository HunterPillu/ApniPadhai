package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.ui.adapter.SettingsAdapter
import com.edu.apnipadhai.utils.Const


class SettingsFrag : BaseFragment() {

    private lateinit var adapter: SettingsAdapter

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

        list.add(Setting(R.drawable.bookmark, getString(R.string.bookmrk), Const.COURSE_ITEM))
        list.add(
            Setting(
                R.drawable.notification,
                getString(R.string.notification),
                Const.COURSE_ITEM
            )
        )

        list.add(Setting(R.drawable.share, getString(R.string.shareapp), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.touch, getString(R.string.Subscribe), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.phone, getString(R.string.contact), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.document, getString(R.string.TC), Const.COURSE_HEADER))
        list.add(Setting(R.drawable.exit, getString(R.string.signout), Const.COURSE_HEADER))
        val rvRecords = layoutView?.findViewById<RecyclerView>(R.id.rvSetting)
        adapter = SettingsAdapter(list)
        rvRecords?.adapter = adapter
    }
}