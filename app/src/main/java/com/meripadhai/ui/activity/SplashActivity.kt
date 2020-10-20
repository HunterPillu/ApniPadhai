package com.meripadhai.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.covidbeads.app.assesment.util.shortToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.meripadhai.BuildConfig
import com.meripadhai.R
import com.meripadhai.model.Reference
import com.meripadhai.ui.dialog.ErrorDialog
import com.meripadhai.ui.dialog.UpgradeDialog
import com.meripadhai.ui.onboardingscreen.feature.onboarding.OnBoardingActivity
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.Const.SHOW_INTRO_ALWAYS
import com.meripadhai.utils.CustomLog
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var isFinished = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        tvVersion.setText(BuildConfig.VERSION_NAME)
        fetchReferenceData()

    }

    private fun fetchReferenceData() {
        if (!Connectivity.isConnected(this)) {
            shortToast(this, R.string.no_internet_connection)
            onBackPressed()
            return
        }
        FirebaseFirestore.getInstance().collection(Const.TABLE_COUNT)
            .get()
            .addOnSuccessListener { documents ->
                try {
                    for (postSnapshot in documents) {
                        CustomLog.e("splash",postSnapshot.toString())
                        val ref = postSnapshot.toObject(Reference::class.java)
                        if (ref.appVersion > BuildConfig.VERSION_CODE ) {
                            showUpgradeDialog(ref)
                        } else if ( ref.error) {
                            showErrorDialog(ref)
                        } else {
                            openNextScreen()
                        }
                        break
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .addOnFailureListener { exception ->
                onBackPressed()
            }
    }

    private fun showErrorDialog(errorData: Reference) {
        val bottomSheetFragment = ErrorDialog.newInstance(errorData)
        bottomSheetFragment.setCancelable(false)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun showUpgradeDialog(forceUpdate: Reference) {
        val bottomSheetFragment = UpgradeDialog.newInstance(forceUpdate)
        bottomSheetFragment.setCancelable(false)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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
        }, 1000)
    }
}