package com.arad.care4pets.ui.activities;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.ui.adapters.CareInstructionsAdapter;
import com.arad.care4pets.viewmodel.CareInstructionsViewModel;
import com.arad.care4pets.R;
import com.arad.care4pets.data.model.CareInstruction;

public class CareInstructionsActivity extends AppCompatActivity {

    // Adapter for displaying instructions in RecyclerView
    private CareInstructionsAdapter adapter;

    // ViewModel for managing care instruction data
    private CareInstructionsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for care instructions screen
        setContentView(R.layout.activity_care_instructions);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvCareInstructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CareInstructionsAdapter();
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel and observe data changes
        viewModel = new ViewModelProvider(this).get(CareInstructionsViewModel.class);
        viewModel.getInstructions().observe(this,
                instructions -> adapter.setInstructions(instructions));

        // Handle edit and delete actions from adapter
        adapter.setListener(new CareInstructionsAdapter.OnInstructionListener() {

            @Override
            public void onEditClick(CareInstruction instruction) {
                // Create input field pre-filled with current instruction text
                EditText input = new EditText(CareInstructionsActivity.this);
                input.setText(instruction.getInstruction());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setSelection(input.getText().length()); // move cursor to end

                // Show dialog for editing instruction
                new AlertDialog.Builder(CareInstructionsActivity.this)
                        .setTitle("Edit Instruction")
                        .setView(input)
                        .setPositiveButton("Save", (dialog, which) -> {
                            String updated = input.getText().toString().trim();

                            // Prevent empty updates
                            if (updated.isEmpty()) {
                                Toast.makeText(CareInstructionsActivity.this,
                                        "Instruction cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Update instruction and persist changes
                            instruction.setInstruction(updated);
                            viewModel.update(instruction);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onDeleteClick(CareInstruction instruction) {
                // Confirm deletion before removing instruction
                new AlertDialog.Builder(CareInstructionsActivity.this)
                        .setTitle("Delete Instruction")
                        .setMessage("Delete \"" + instruction.getInstruction() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                viewModel.delete(instruction))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        // Input field and button for adding new instructions
        EditText etInstruction = findViewById(R.id.etInstruction);
        Button btnAdd          = findViewById(R.id.btnAddInstruction);

        // Handle adding new instruction
        btnAdd.setOnClickListener(v -> {
            String text = etInstruction.getText().toString().trim();

            // Validate input
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.fill_out_form, Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert new instruction into database
            viewModel.insert(new CareInstruction(text));

            // Clear input field after adding
            etInstruction.setText("");
        });

        // Back button closes the activity
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}