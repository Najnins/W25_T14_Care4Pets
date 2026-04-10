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

    private HealthRecordViewModel healthRecordViewModel;
    private int petId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_record);

        // Read petId — tells us which pet this record belongs to
        petId = getIntent().getIntExtra("pet_id", 0);

        healthRecordViewModel = new ViewModelProvider(this).get(HealthRecordViewModel.class);

        EditText etRecordTitle = findViewById(R.id.etRecordTitle);
        EditText etRecordDescription = findViewById(R.id.etRecordDescription);
        Button btnSave = findViewById(R.id.btn_save_health_record);

        btnSave.setOnClickListener(v -> {
            String title = etRecordTitle.getText() != null
                    ? etRecordTitle.getText().toString().trim() : "";
            String description = etRecordDescription.getText() != null
                    ? etRecordDescription.getText().toString().trim() : "";

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                Toast.makeText(this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            String today = new SimpleDateFormat("yyyy-MM-dd" , Locale.CANADA).format(new Date());
            // petId links this record to the correct pet
            HealthRecord record = new HealthRecord(title, description, "Health Notes", petId, today);
            healthRecordViewModel.insert(record);

            Toast.makeText(this, R.string.record_added, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}