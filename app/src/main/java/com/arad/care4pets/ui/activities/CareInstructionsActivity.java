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
        viewModel.getInstructions().observe(this,
                instructions -> adapter.setInstructions(instructions));

        // Wire up edit and delete from the adapter's options button
        adapter.setListener(new CareInstructionsAdapter.OnInstructionListener() {

            @Override
            public void onEditClick(CareInstruction instruction) {
                // Show a dialog with a pre-filled EditText
                EditText input = new EditText(CareInstructionsActivity.this);
                input.setText(instruction.getInstruction());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setSelection(input.getText().length()); // cursor at end

                new AlertDialog.Builder(CareInstructionsActivity.this)
                        .setTitle("Edit Instruction")
                        .setView(input)
                        .setPositiveButton("Save", (dialog, which) -> {
                            String updated = input.getText().toString().trim();
                            if (updated.isEmpty()) {
                                Toast.makeText(CareInstructionsActivity.this,
                                        "Instruction cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            instruction.setInstruction(updated);
                            viewModel.update(instruction);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onDeleteClick(CareInstruction instruction) {
                new AlertDialog.Builder(CareInstructionsActivity.this)
                        .setTitle("Delete Instruction")
                        .setMessage("Delete \"" + instruction.getInstruction() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                viewModel.delete(instruction))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        EditText etInstruction = findViewById(R.id.etInstruction);
        Button btnAdd          = findViewById(R.id.btnAddInstruction);

        btnAdd.setOnClickListener(v -> {
            String text = etInstruction.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.fill_out_form, Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.insert(new CareInstruction(text));
            etInstruction.setText("");
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}