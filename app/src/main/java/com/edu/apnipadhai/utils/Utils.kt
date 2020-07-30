package com.edu.apnipadhai.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.util.SharedPreferencesUtils
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


    fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getUniqueValue(): String? {
        val ft = SimpleDateFormat("yyyyMMddhhmmssSSS")
        return ft.format(Date()) + (Math.random() * 10).toInt()
    }

    fun size2String(filesize: Long): String? {
        val unit = 1024
        if (filesize < unit) {
            return String.format("%d bytes", filesize)
        }
        val exp =
            (Math.log(filesize.toDouble()) / Math.log(unit.toDouble())).toInt()
        return String.format(
            "%.0f %sbytes",
            filesize / Math.pow(unit.toDouble(), exp.toDouble()),
            "KMGTPE"[exp - 1]
        )
    }

    fun getRootPath(): String? {
        val sdPath: String
        val ext1 = Environment.getExternalStorageState()
        sdPath = if (ext1 == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            Environment.MEDIA_UNMOUNTED
        }
        return sdPath
    }

    fun isPermissionGranted(
        activity: Activity,
        permission: String
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.v("DirectTalk9", "Permission is granted")
                true
            } else {
                Log.v("DirectTalk9", "Permission is revoked")
                ActivityCompat.requestPermissions(activity, arrayOf(permission), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("DirectTalk9", "Permission is granted")
            true
        }
    }
}