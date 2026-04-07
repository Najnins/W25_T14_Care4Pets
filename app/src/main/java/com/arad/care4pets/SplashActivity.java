package com.arad.care4pets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (UserSessionManager.isLoggedIn(this)) {
                // Already logged in — skip onboarding and login entirely
                startActivity(new Intent(this, MenuActivity.class));
            } else {
                // First launch or logged out — show onboarding
                startActivity(new Intent(this, OnboardingActivity.class));
            }
            finish();
        }, 1500);
    }
}