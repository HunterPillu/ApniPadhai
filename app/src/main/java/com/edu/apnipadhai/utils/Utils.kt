package com.edu.apnipadhai.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getMillis(dateString: String?): Long {
        @SuppressLint("SimpleDateFormat") val sdf =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return try {                                              // "2020-06-25T06:28:04.000Z"
            val date = sdf.parse(dateString) //"2019-06-17T20:27:23.706Z");
            date.time
        } catch (pe: Exception) {
            Date().time
        }
    }

    fun getSizeInMB(size: Long): String {
        //5848639
        var size = size
        size = size / 100000
        val result = size / 10.toFloat()
        return result.toString() + "MB"
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getBatteryOptimizerExemptionIntent(packageName: String): Intent {
        val intent =
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:$packageName")
        return intent
    }

    fun canHandleIntent(
        batteryExemptionIntent: Intent,
        packageManager: PackageManager?
    ): Boolean {
        return if (null != packageManager) batteryExemptionIntent.resolveActivity(packageManager) != null else false
    }
}