package com.covidbeads.app.assesment.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes

@IntDef(Toast.LENGTH_LONG, Toast.LENGTH_SHORT)
private annotation class ToastLength

fun shortToast(context: Context, @StringRes text: Int) {
    shortToast(context, context.getString(text))
}

fun shortToast(context: Context, text: String) {
    show(context, text, Toast.LENGTH_SHORT)
}

private fun makeToast(context: Context, text: String, @ToastLength length: Int): Toast {
    return Toast.makeText(context, text, length)
}

private fun show(context: Context, text: String, @ToastLength length: Int) {
    makeToast(context, text, length).show()
}