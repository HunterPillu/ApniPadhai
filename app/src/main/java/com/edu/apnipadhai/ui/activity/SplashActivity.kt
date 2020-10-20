package com.edu.apnipadhai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.BuildConfig
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Count
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    var isFinished = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fetchVersion()
    }

    override fun onBackPressed() {
        isFinished = true
        super.onBackPressed()
    }

    private fun fetchVersion() {
        if (!Connectivity.isConnected(this)) {
            shortToast(this,R.string.no_internet_connection)
            onBackPressed()
            return
        }
        FirebaseFirestore.getInstance().collection(Const.TABLE_COUNT)
            .get()
            .addOnSuccessListener { documents ->
                for (postSnapshot in documents) {
                    val count = postSnapshot.toObject(Count::class.java)
                    if (count.adminAppVersion > BuildConfig.VERSION_CODE) {
                        shortToast(this,R.string.use_latest_version)
                        onBackPressed()
                    }
                    break
                }
                openNextScreen()
            }
            .addOnFailureListener { exception ->
                onBackPressed()
            }
    }

    private fun openNextScreen() {
        Handler().postDelayed({
            // This method will be executed once the timer is over
            if (!isFinished) {
                val intent: Intent
                if (FirebaseAuth.getInstance().currentUser != null) {
                    intent = Intent(this@SplashActivity, AdminActivity::class.java)
                } else {
                    intent = Intent(this@SplashActivity, LoginActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }, 1000)
    }
}