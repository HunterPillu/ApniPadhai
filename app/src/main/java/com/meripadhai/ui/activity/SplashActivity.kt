package com.meripadhai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.meripadhai.BuildConfig
import com.meripadhai.R
import com.meripadhai.ui.onboardingscreen.feature.onboarding.OnBoardingActivity
import com.meripadhai.utils.Const.SHOW_INTRO_ALWAYS
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var isFinished = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        tvVersion.setText(BuildConfig.VERSION_NAME)
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
                    intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                } else {
                    intent = Intent(this@SplashActivity, LoginActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }, 2500)
    }
}