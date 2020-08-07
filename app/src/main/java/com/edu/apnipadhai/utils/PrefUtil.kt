package com.covidbeads.app.assesment.util

import android.content.Context
import com.edu.apnipadhai.model.User
import com.google.gson.Gson

object PrefUtil {
    private const val PREF_NAME = "Meri_Padhai"

    fun saveUser(context: Context, user: User) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("user", Gson().toJson(user))
        editor.apply()
    }

    fun saveCourseId(context: Context, courseId: Int) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt("course_id", courseId)
        editor.apply()
    }

    fun getCourseId(context: Context): Int {
        val sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val result = sPref.getInt("course_id", -1)
        return result

    }

    fun getUser(context: Context): User? {
        val sPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val s = sPref.getString("user", null)
        return if (null != s) {
            Gson().fromJson(s, User::class.java)
        } else null
    }
}