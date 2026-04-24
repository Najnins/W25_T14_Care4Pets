package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.arad.care4pets.R;
import com.arad.care4pets.utils.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set splash screen layout
        setContentView(R.layout.activity_splash);

        // Delay execution for 1.5 seconds (simulate loading / show branding)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Check if user is already logged in
            if (UserSessionManager.isLoggedIn(this)) {
                // If logged in, skip onboarding and login screens
                startActivity(new Intent(this, MenuActivity.class));
            } else {
                // If not logged in, show onboarding flow
                startActivity(new Intent(this, OnboardingActivity.class));
            }

            // Close splash activity so user can't return to it
            finish();

        }, 1500);
    }
}