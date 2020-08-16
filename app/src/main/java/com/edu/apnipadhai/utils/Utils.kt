package com.edu.apnipadhai.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import com.edu.apnipadhai.BuildConfig
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Bookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun openFacebookIntent(context: Context) {
        val url: String = "https://www.facebook.com/Meri-padhai-101122954868149"
        val pageId: String = "101122954868149"
        return try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/$pageId"))
            intent.setPackage("com.facebook.katana")
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    fun openYouTube(context: Context) {
        val URL_YOUTUBE = "https://www.youtube.com/channel/UC1w-ro4_aFEhq2Quzf1Vz0w"
        val URL_YOUTUBE_INAPP = "vnd.youtube.com/channel/UC1w-ro4_aFEhq2Quzf1Vz0w"

        try {
            //here we try to open the link in app
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_YOUTUBE_INAPP)))
        } catch (e: Exception) {
            //the app isn't available: we open in browser`
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_YOUTUBE)))
        }
    }

    fun shareApp(context: Context) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${context.getString(R.string.share_app_text)} https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    fun shareVideoLink(context: Context, videoLink: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${context.getString(R.string.share_app_text)} https://youtu.be/${videoLink}"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    fun openInstagram(context: Context) {
        val uri = Uri.parse("https://www.instagram.com/_u/meri.padhai")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/meri.padhai")
                )
            )
        }
    }

    fun openTwitter(context: Context) {
        val twitterId = "1291309686738825219"
        try {
            // Get Twitter app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$twitterId"))
            intent.setPackage("com.twitter.android")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // If no Twitter app found, open on browser
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/PadhaiMeri")
                )
            )
        }
    }

    fun openMail(context: Context) {
        /* Create the Intent */
        val emailIntent: Intent = Intent(Intent.ACTION_SEND);
        val data = Uri.parse(
            "mailto:"
                    + "meripadhai80@gmail.com"
                    + "?subject=" + "Feedback from user[${FirebaseAuth.getInstance().currentUser?.uid}]"
                    + "&body=" + ""
        )
        emailIntent.data = data;
        context.startActivity(emailIntent);
    }

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

    @JvmStatic
    fun bookmarkVideo(context : Context,fKey : String) {
        val videoList = FirebaseFirestore.getInstance().collection(Const.TABLE_BOOKMARK)
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).collection(fKey)
        videoList.add(Bookmark(fKey)).addOnCompleteListener {
            Utils.showToast(
                context,
                context.getString(R.string.msg_bookmark_success)
            )
        }
    }
}