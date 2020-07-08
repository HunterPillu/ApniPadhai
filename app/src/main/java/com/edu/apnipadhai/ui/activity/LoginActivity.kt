package com.edu.apnipadhai.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.edu.apnipadhai.BuildConfig
import com.edu.apnipadhai.MainActivity
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.utils.FirebaseData
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class LoginActivity : AppCompatActivity() {
    // private var startButton: Button? = null
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setLogo(R.drawable.logo)
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                    .setAvailableProviders(
                        Arrays.asList(
                            AuthUI.IdpConfig.PhoneBuilder().build()
                            // ,AuthUI.IdpConfig.EmailBuilder().build()
                            //, AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                    ).build(),
                RC_SIGN_IN
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                val user = User()
                user.mobile = FirebaseAuth.getInstance().currentUser?.phoneNumber
                FirebaseData.updateUserData(user)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled)
                    return
                }

                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection)
                    return
                }

                if (response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error)
                    return
                }
            }

            showSnackbar(R.string.unknown_sign_in_response)
        }
    }

    private fun showSnackbar(stringRes: Int) {
        Snackbar.make(findViewById(R.id.root)!!, stringRes, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG: String = "LoginActivity"
    }

}
