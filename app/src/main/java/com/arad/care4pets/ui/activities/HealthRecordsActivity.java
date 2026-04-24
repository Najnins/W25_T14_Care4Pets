package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.ui.adapters.HealthRecordAdapter;
import com.arad.care4pets.viewmodel.HealthRecordViewModel;
import com.arad.care4pets.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class HealthRecordsActivity extends AppCompatActivity {

    // ViewModel for accessing health record data
    private HealthRecordViewModel healthRecordViewModel;

    // Adapter for displaying records in RecyclerView
    private HealthRecordAdapter adapter;

    // ID of the pet whose records are being viewed
    private int petId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for health records screen
        setContentView(R.layout.activity_health_records);

        // Get pet ID and name passed from previous activity
        petId = getIntent().getIntExtra("pet_id", 0);
        String petName = getIntent().getStringExtra("pet_name");

        // Initialize title and subtitle views
        TextView tvTitle    = findViewById(R.id.tvHealthRecordsTitle);
        TextView tvSubtitle = findViewById(R.id.tvHealthRecordsSubtitle);

        // If pet name is provided, customize UI text
        if (petName != null && !petName.isEmpty()) {
            tvTitle.setText(petName + "'s Health Records");
            tvSubtitle.setText("Medical history for " + petName);
        }
        // Otherwise, default XML text remains

        // Back button closes this activity
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Floating button to add a new health record
        ExtendedFloatingActionButton fabAddHealthRecord = findViewById(R.id.fabAddHealthRecord);
        fabAddHealthRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddHealthRecordActivity.class);

            // Pass pet ID so new record is linked correctly
            intent.putExtra("pet_id", petId);

            startActivity(intent);
        });

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvHealthRecords);
        adapter = new HealthRecordAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        healthRecordViewModel = new ViewModelProvider(this).get(HealthRecordViewModel.class);

        // Observe data based on whether a specific pet is selected
        if (petId > 0) {
            // Load records for a specific pet
            healthRecordViewModel.getHealthRecordsForPet(petId)
                    .observe(this, records -> adapter.submitList(records));
        } else {
            // Fallback: load all records (no filtering)
            healthRecordViewModel.getAllHealthRecords()
                    .observe(this, records -> adapter.submitList(records));
        }
    }
}