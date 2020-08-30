package com.meripadhai.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meripadhai.R
import com.meripadhai.utils.Const
import com.meripadhai.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bootom_sheet_contact.view.*

class ContactDialog : BottomSheetDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bootom_sheet_contact, container, false)
        view.tv1.setOnClickListener { Utils.openMail(context!!) }
        view.tv1.text = getString(R.string.email_us, Const.EMAIL_OFFICIAL)
        view.tv2.text = getString(R.string.call,"Abhay", Const.PHONE_ABHAY)
        view.tv3.text = getString(R.string.call,"Ajit", Const.PHONE_AJIT)
        view.tv2.setOnClickListener { Utils.callFromDailer(context!!, Const.PHONE_ABHAY) }
        view.tv3.setOnClickListener { Utils.callFromDailer(context!!, Const.PHONE_AJIT) }
        return view

    }


}