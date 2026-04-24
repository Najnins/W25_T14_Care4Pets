package com.arad.care4pets.ui.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.arad.care4pets.R;

public class HealthRecordDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for health record details screen
        setContentView(R.layout.activity_health_record_details);

        // Get reference to back button
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Close activity when back button is pressed
        btnBack.setOnClickListener(v -> finish());
    }
}