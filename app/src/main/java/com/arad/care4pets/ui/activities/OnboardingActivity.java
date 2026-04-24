package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.arad.care4pets.ui.fragments.OnboardingAdapter;
import com.arad.care4pets.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {

    // UI components for onboarding flow
    private ViewPager2 viewPager;
    private Button btnNext;
    private Button btnSkip;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set onboarding layout
        setContentView(R.layout.activity_onboarding);

        // Initialize UI elements
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        tabLayout = findViewById(R.id.tabLayout);

        // Ensure Skip button stays on top and clickable
        // (fix for overlapping UI or rendering issues)
        btnSkip.bringToFront();
        btnSkip.invalidate();
        btnSkip.requestLayout();

        // Set adapter for onboarding pages (fragments)
        OnboardingAdapter adapter = new OnboardingAdapter(this);
        viewPager.setAdapter(adapter);

        // Connect TabLayout (dots indicator) with ViewPager
        // No labels/icons needed, just visual indicators
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // intentionally left empty
                }).attach();

        // Handle "Next" button click
        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();

            // If not on last page, move to next page
            if (current < 2) {
                viewPager.setCurrentItem(current + 1);
            } else {
                // If on last page, proceed to login
                navigateToLogin();
            }
        });

        // Skip button directly navigates to login
        btnSkip.setOnClickListener(v -> navigateToLogin());

        // Listen for page changes to update UI dynamically
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // If last onboarding screen
                if (position == 2) {
                    // Change button text and style
                    btnNext.setText("Get Started");
                    btnNext.setBackground(getDrawable(R.drawable.bg_button_onboarding3));

                    // Hide skip button (no longer needed)
                    btnSkip.setVisibility(Button.INVISIBLE);
                } else {
                    // Reset button text
                    btnNext.setText("Next");

                    // Change background based on page
                    if (position == 0) {
                        btnNext.setBackground(getDrawable(R.drawable.bg_button_onboarding1));
                    } else {
                        btnNext.setBackground(getDrawable(R.drawable.bg_button_onboarding2));
                    }

                    // Ensure skip button is visible again
                    btnSkip.setVisibility(Button.VISIBLE);
                }
            }
        });
    }

    // Navigate to Login screen and mark onboarding as completed
    private void navigateToLogin() {
        // Save flag so onboarding doesn't show again
        getSharedPreferences("care4pets_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("first_time", false)
                .apply();

        // Launch LoginActivity
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);

        // Close onboarding activity
        finish();
    }
}
