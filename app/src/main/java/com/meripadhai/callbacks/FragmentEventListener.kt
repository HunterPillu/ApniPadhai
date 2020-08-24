package com.meripadhai.callbacks

import com.meripadhai.model.User
import com.meripadhai.ui.fragments.BaseFragment

interface FragmentEventListener {
    fun updateToolbarTitle(title: String)
    fun openFragment(fragment: BaseFragment)
    fun onInviteOpponent(opponent : User)

}