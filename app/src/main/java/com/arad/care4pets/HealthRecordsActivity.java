package com.arad.care4pets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class HealthRecordsActivity extends AppCompatActivity {

    private HealthRecordViewModel healthRecordViewModel;
    private HealthRecordAdapter adapter;
    private int petId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);

        petId = getIntent().getIntExtra("pet_id", 0);
        String petName = getIntent().getStringExtra("pet_name");


        TextView tvTitle    = findViewById(R.id.tvHealthRecordsTitle);
        TextView tvSubtitle = findViewById(R.id.tvHealthRecordsSubtitle);

        if (petName != null && !petName.isEmpty()) {
            tvTitle.setText(petName + "'s Health Records");
            tvSubtitle.setText("Medical history for " + petName);
        }
        // If no petName, the XML default text ("Health Records" / subtitle) shows as-is

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        ExtendedFloatingActionButton fabAddHealthRecord = findViewById(R.id.fabAddHealthRecord);
        fabAddHealthRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddHealthRecordActivity.class);
            intent.putExtra("pet_id", petId);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.rvHealthRecords);
        adapter = new HealthRecordAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        healthRecordViewModel = new ViewModelProvider(this).get(HealthRecordViewModel.class);

        if (petId > 0) {
            healthRecordViewModel.getHealthRecordsForPet(petId)
                    .observe(this, records -> adapter.submitList(records));
        } else {
            healthRecordViewModel.getAllHealthRecords()
                    .observe(this, records -> adapter.submitList(records));
        }
    }
}