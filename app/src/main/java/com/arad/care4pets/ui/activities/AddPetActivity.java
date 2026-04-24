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

public class AddPetActivity extends AppCompatActivity {

    // ViewModel for inserting new pets into database
    private PetViewModel petViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for adding a new pet
        setContentView(R.layout.activity_add_pet);

        // Initialize ViewModel
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        // Get references to input fields
        EditText etName    = findViewById(R.id.etPetName);
        EditText etSpecies = findViewById(R.id.etPetSpecies);
        EditText etAge     = findViewById(R.id.etPetAge);
        EditText etWeight  = findViewById(R.id.etPetWeight);
        EditText etNotes   = findViewById(R.id.etPetNotes);
        Button btnSave     = findViewById(R.id.btnSavePet);

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

            // Convert age and weight to integers
            int age    = Integer.parseInt(ageText);
            int weight = Integer.parseInt(weightText);

            // Create new Pet object
            // Health defaults to 100 for new pets
            Pet pet = new Pet(name, species, age, notes, weight, 100);

            // Insert pet into database
            // ViewModel handles assigning userId internally
            petViewModel.insert(pet);

            // Notify user and close screen
            Toast.makeText(this, "Pet added", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}