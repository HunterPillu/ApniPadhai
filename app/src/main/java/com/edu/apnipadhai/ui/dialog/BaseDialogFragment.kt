package com.edu.apnipadhai.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlin.properties.Delegates

open class BaseDialogFragment : DialogFragment() {
    var layoutId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Do all the stuff to initialize your custom view
        return inflater.inflate(layoutId, container, false)
    }

}