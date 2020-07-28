package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edu.apnipadhai.R
import kotlinx.android.synthetic.main.fragment_admin.*


class AdminFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_admin, container, false)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bAddCategory.setOnClickListener { openFragment(EditCategoryFragment.newInstance()) }
        bAddVideos.setOnClickListener { openFragment(AddVideoFragment.newInstance()) }
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_admin))
    }

    companion object {
        internal val TAG = AdminFragment::class.java.simpleName

        fun newInstance(): AdminFragment {
            return AdminFragment()
        }
    }
}