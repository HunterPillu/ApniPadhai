package com.meripadhai.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.meripadhai.R
import com.meripadhai.model.Reference
import com.meripadhai.ui.activity.LoginActivity
import com.meripadhai.ui.activity.MainActivity
import com.meripadhai.ui.onboardingscreen.feature.onboarding.OnBoardingActivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.Utils
import kotlinx.android.synthetic.main.bootom_sheet_contact.view.*
import kotlinx.android.synthetic.main.bootom_sheet_contact.view.tvTitle
import kotlinx.android.synthetic.main.bootom_sheet_upgrade.view.*

class ErrorDialog : BottomSheetDialogFragment() {

    private lateinit var forceUpdate: Reference

    companion object {
        internal val TAG = ErrorDialog::class.java.simpleName

        fun newInstance(forceUpdate: Reference): ErrorDialog {
            val frag = ErrorDialog()
            frag.forceUpdate = forceUpdate
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bootom_sheet_upgrade, container, false)
        view.tvTitle.text = forceUpdate.errorTitle
        view.tvDesc.text = forceUpdate.errorDesc
        view.b1.visibility = View.GONE
        view.b2.text = getString(R.string.exitApp)
        view.b2.setOnClickListener {
            activity?.finish()
        }

        return view

    }

    private fun openNextScreen() {
        val intent: Intent = when {
            FirebaseAuth.getInstance().currentUser != null -> {
                Intent(context, MainActivity::class.java)
            }
            Const.SHOW_INTRO_ALWAYS -> {
                Intent(context, OnBoardingActivity::class.java)
            }
            else -> {
                Intent(context, LoginActivity::class.java)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


}