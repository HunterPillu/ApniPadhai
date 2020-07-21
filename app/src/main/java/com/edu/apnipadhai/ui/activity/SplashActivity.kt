package com.edu.apnipadhai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.edu.apnipadhai.R
import com.edu.apnipadhai.utils.Const.SHOW_INTRO_ALWAYS
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    var isFinished = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openNextScreen()
    }

    override fun onBackPressed() {
        isFinished = true
        super.onBackPressed()
    }

    private fun openNextScreen() {
        Handler().postDelayed({
            // This method will be executed once the timer is over
            if (!isFinished) {

                val intent: Intent
                if (FirebaseAuth.getInstance().currentUser != null) {
                    intent = Intent(this@SplashActivity, MainActivity::class.java)
                } else if (SHOW_INTRO_ALWAYS) {
                    intent = Intent(this@SplashActivity, IntroActivity::class.java)
                } else {
                    intent = Intent(this@SplashActivity, LoginActivity::class.java)
                }

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }, 2500)
    }
}