package com.arad.care4pets.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.arad.care4pets.viewmodel.HealthRecordViewModel;
import com.arad.care4pets.R;
import com.arad.care4pets.data.model.HealthRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddHealthRecordActivity extends AppCompatActivity {

    // ViewModel for inserting health records into database
    private HealthRecordViewModel healthRecordViewModel;

    // ID of the pet this record belongs to
    private int petId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for adding a health record
        setContentView(R.layout.activity_add_health_record);

        // Get petId from previous activity (used to link record to a pet)
        petId = getIntent().getIntExtra("pet_id", 0);

        // Initialize the ViewModel
        healthRecordViewModel = new ViewModelProvider(this).get(HealthRecordViewModel.class);

        // Get references to input fields
        EditText etRecordTitle = findViewById(R.id.etRecordTitle);
        EditText etRecordDescription = findViewById(R.id.etRecordDescription);
        Button btnSave = findViewById(R.id.btn_save_health_record);

        // Handle save button click
        btnSave.setOnClickListener(v -> {
            // Safely extract user input
            String title = etRecordTitle.getText() != null
                    ? etRecordTitle.getText().toString().trim() : "";

            String description = etRecordDescription.getText() != null
                    ? etRecordDescription.getText().toString().trim() : "";

            // Validate required fields
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                Toast.makeText(this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate today's date in YYYY-MM-DD format
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
                    .format(new Date());

            // Create new health record object
            // petId links this record to the correct pet
            HealthRecord record = new HealthRecord(
                    title,
                    description,
                    "Health Notes",
                    petId,
                    today
            );

            // Insert record into database
            healthRecordViewModel.insert(record);

            // Notify user and close screen
            Toast.makeText(this, R.string.record_added, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}