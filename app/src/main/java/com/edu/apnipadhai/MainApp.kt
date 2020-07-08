package com.edu.apnipadhai

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics


class MainApp : Application() {

    override fun onCreate() {
        CustomLog.e("MAIN", "Application onCreate")
        super.onCreate()
        //Allowing Strict mode policy for Nougat support

        //Allowing Strict mode policy for Nougat support
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        instance = this
        FirebaseApp.initializeApp(this)
        initAnalytics()
    }

    fun initAnalytics() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        //return firebaseAnalytics as FirebaseAnalytics
    }

    override fun onTerminate() {
        CustomLog.e("MAIN", "Application onTerminate")
        super.onTerminate()

    }

    companion object {
        var firebaseAnalytics: FirebaseAnalytics? = null
        private lateinit var instance: MainApp

        @Synchronized
        fun getInstance(): MainApp = instance
    }
}