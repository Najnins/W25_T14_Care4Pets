package com.arad.care4pets;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CareInstructionsActivity extends AppCompatActivity {

    private CareInstructionsAdapter adapter;
    private CareInstructionsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_instructions);

        RecyclerView recyclerView = findViewById(R.id.rvCareInstructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CareInstructionsAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CareInstructionsViewModel.class);

        // Always show only the logged-in user's instructions
        viewModel.getInstructions().observe(this,
                instructions -> adapter.setInstructions(instructions));

        EditText etInstruction = findViewById(R.id.etInstruction);
        Button btnAdd          = findViewById(R.id.btnAddInstruction);

        btnAdd.setOnClickListener(v -> {
            String text = etInstruction.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.fill_out_form, Toast.LENGTH_SHORT).show();
                return;
            }
            // No petId — care instructions are user-scoped only
            // ViewModel stamps userId automatically
            viewModel.insert(new CareInstruction(text));
            etInstruction.setText("");
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}