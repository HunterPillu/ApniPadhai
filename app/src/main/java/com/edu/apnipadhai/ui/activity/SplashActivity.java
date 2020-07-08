package com.edu.apnipadhai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.edu.apnipadhai.MainActivity;
import com.edu.apnipadhai.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "SplashActivity";
    boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        openNextScreen();
    }


    @Override
    public void onBackPressed() {
        isFinished = true;
        super.onBackPressed();
    }

    private void openNextScreen() {
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            if (!isFinished) {
                Intent intent;
                if (null != mAuth.getCurrentUser()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 2500);
    }
}
