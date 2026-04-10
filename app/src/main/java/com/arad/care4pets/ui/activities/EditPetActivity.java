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

    private PetViewModel petViewModel;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet); // reuse the same layout

        pet = getIntent().getParcelableExtra("pet");
        if (pet == null) {
            Toast.makeText(this, "Error: Pet not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        EditText etName    = findViewById(R.id.etPetName);
        EditText etSpecies = findViewById(R.id.etPetSpecies);
        EditText etAge     = findViewById(R.id.etPetAge);
        EditText etWeight  = findViewById(R.id.etPetWeight);
        EditText etNotes   = findViewById(R.id.etPetNotes);
        Button btnSave     = findViewById(R.id.btnSavePet);

        // Pre-fill all fields with the current pet's data
        etName.setText(pet.getName());
        etSpecies.setText(pet.getSpecies());
        etAge.setText(String.valueOf(pet.getAge()));
        etWeight.setText(String.valueOf(pet.getWeight()));
        etNotes.setText(pet.getNotes());

        btnSave.setText("Save Changes");

        btnSave.setOnClickListener(v -> {
            String name       = etName.getText().toString().trim();
            String species    = etSpecies.getText().toString().trim();
            String ageText    = etAge.getText().toString().trim();
            String weightText = etWeight.getText().toString().trim();
            String notes      = etNotes.getText().toString().trim();

            if (name.isEmpty() || species.isEmpty() || ageText.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the existing pet object — keep its id and userId intact
            pet.setName(name);
            pet.setSpecies(species);
            pet.setAge(Integer.parseInt(ageText));
            pet.setWeight(Integer.parseInt(weightText));
            pet.setNotes(notes);

            petViewModel.update(pet);

            Toast.makeText(this, "Pet updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}