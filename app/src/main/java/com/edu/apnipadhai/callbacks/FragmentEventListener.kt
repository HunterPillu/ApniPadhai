package com.edu.apnipadhai.callbacks

import com.edu.apnipadhai.ui.fragments.BaseFragment

interface FragmentEventListener {
    fun updateToolbarTitle(title: String)
    fun openFragment(fragment: BaseFragment)
}