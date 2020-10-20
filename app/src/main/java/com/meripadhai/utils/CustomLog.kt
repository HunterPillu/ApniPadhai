package com.meripadhai.utils

import android.util.Log
import com.meripadhai.firebase.FirebaseData

class CustomLog {

//    companion object {
//        fun d(tag: String, msg: String?) {
//            //Log.d(tag, "$msg")
//            //sendToCrashlytics(tag, msg)
//        }
//
//        fun w(tag: String, msg: String?) {
//            //Log.w(tag, "$msg")
//            //sendToCrashlytics(tag, msg)
//        }
//
//        fun i(tag: String, msg: String?) {
//            //Log.i(tag, "$msg")
//            //sendToCrashlytics(tag, msg)
//        }
//
//        fun e(tag: String, msg: String?) {
//            //Log.d(tag, "$msg")
//            //sendToCrashlytics(tag, msg)
//        }
//
//        fun e(tag: String, msg: Exception) {
//            //Log.d(tag, msg.localizedMessage)
//            //sendToCrashlytics(msg)
//        }
//
//        fun e(tag: String, msg: Throwable) {
//            //Log.d(tag, msg.localizedMessage)
//            //msg.printStackTrace()
//            //sendToCrashlytics(msg)
//        }
//
//        fun e(tag: String, error: String, msg: Throwable) {
//            //Log.d(tag, "$error ${msg.localizedMessage}")
//            //sendToCrashlytics(msg)
//        }
//
//        fun sendToCrashlytics(tag: String, msg: String?) {
//        }
//
//
//        fun sendToCrashlytics(msg: Throwable) {
//            /*FirebaseData.init().database.getReference("logs/${FirebaseData.myID}").push()
//                .setValue(msg.localizedMessage)*/
//            //FirebaseCrashlytics.getInstance().recordException(msg);
//        }
//    }
    companion object {
        fun d(tag: String, msg: String?) {
            Log.d(tag, "$msg")
            sendToCrashlytics(tag, msg)
        }

        fun w(tag: String, msg: String?) {
            Log.w(tag, "$msg")
            sendToCrashlytics(tag, msg)
        }

        fun i(tag: String, msg: String?) {
            Log.i(tag, "$msg")
            sendToCrashlytics(tag, msg)
        }

        fun e(tag: String, msg: String?) {
            Log.d(tag, "$msg")
            sendToCrashlytics(tag, msg)
        }

        fun e(tag: String, msg: Exception) {
            Log.d(tag, msg.localizedMessage)
            sendToCrashlytics(msg)
        }

        fun e(tag: String, msg: Throwable) {
            Log.d(tag, msg.localizedMessage)
            msg.printStackTrace()
            sendToCrashlytics(msg)
        }

        fun e(tag: String, error: String, msg: Throwable) {
            Log.d(tag, "$error ${msg.localizedMessage}")
            sendToCrashlytics(msg)
        }

        fun sendToCrashlytics(tag: String, msg: String?) {
        }


        fun sendToCrashlytics(msg: Throwable) {
//            FirebaseData.init().database.getReference("logs/${FirebaseData.myID}").push()
//                .setValue(msg.localizedMessage)
            //FirebaseCrashlytics.getInstance().recordException(msg);
        }
    }
}