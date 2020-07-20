package com.edu.apnipadhai.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edu.apnipadhai.R
import com.edu.apnipadhai.ui.activity.MainActivity

class SettingsFrag : BaseFragment() {

    private lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        setUpToolbar()
        layoutView = inflater.inflate(R.layout.fragment_settings, container, false)

        return layoutView
    }

    private fun setUpToolbar() {
        mainActivity.tvTitle.text = getString(R.string.setting)
        mainActivity.ivBack.setOnClickListener{ onBackPressed()}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}