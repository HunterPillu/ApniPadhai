package com.meripadhai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.meripadhai.R
import com.meripadhai.model.Course

class SpinnerAdapter(
    cntxt: Context,
    private val items: List<Course>
) : ArrayAdapter<Course>(cntxt, R.layout.support_simple_spinner_dropdown_item, items) {
    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val v = super.getView(position, convertView, parent) as TextView

        //v.setTextColor(Color.BLACK);
        v.text = items[position].name
        return v
    }

    override fun getItem(position: Int): Course {
        return items[position]
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var v = convertView
        if (v == null) {
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.support_simple_spinner_dropdown_item, null)
        }
        val lbl = v!!.findViewById<View>(android.R.id.text1) as TextView
        // lbl.setTextColor(Color.BLACK);
        lbl.text = items[position].name
        return v
    }

}