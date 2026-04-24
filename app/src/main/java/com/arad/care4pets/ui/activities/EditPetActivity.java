package com.arad.care4pets.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.arad.care4pets.viewmodel.PetViewModel;
import com.arad.care4pets.R;
import com.arad.care4pets.data.model.Pet;

public class EditPetActivity extends AppCompatActivity {

    // ViewModel for updating pet data
    private PetViewModel petViewModel;

    // The pet being edited
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reuse the "Add Pet" layout for editing
        setContentView(R.layout.activity_add_pet);

        // Get the pet passed from previous screen
        pet = getIntent().getParcelableExtra("pet");

        // If no pet was passed, show error and exit
        if (pet == null) {
            Toast.makeText(this, "Error: Pet not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        // Get references to input fields
        EditText etName    = findViewById(R.id.etPetName);
        EditText etSpecies = findViewById(R.id.etPetSpecies);
        EditText etAge     = findViewById(R.id.etPetAge);
        EditText etWeight  = findViewById(R.id.etPetWeight);
        EditText etNotes   = findViewById(R.id.etPetNotes);
        Button btnSave     = findViewById(R.id.btnSavePet);

        // Pre-fill input fields with existing pet data
        etName.setText(pet.getName());
        etSpecies.setText(pet.getSpecies());
        etAge.setText(String.valueOf(pet.getAge()));
        etWeight.setText(String.valueOf(pet.getWeight()));
        etNotes.setText(pet.getNotes());

        // Update button text to reflect edit mode
        btnSave.setText("Save Changes");

        // Handle save button click
        btnSave.setOnClickListener(v -> {
            // Extract user input
            String name       = etName.getText().toString().trim();
            String species    = etSpecies.getText().toString().trim();
            String ageText    = etAge.getText().toString().trim();
            String weightText = etWeight.getText().toString().trim();
            String notes      = etNotes.getText().toString().trim();

            // Validate required fields
            if (name.isEmpty() || species.isEmpty() || ageText.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update existing pet object (ID and userId remain unchanged)
            pet.setName(name);
            pet.setSpecies(species);
            pet.setAge(Integer.parseInt(ageText));
            pet.setWeight(Integer.parseInt(weightText));
            pet.setNotes(notes);

            // Save updated pet to database
            petViewModel.update(pet);

            // Notify user and close screen
            Toast.makeText(this, "Pet updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}