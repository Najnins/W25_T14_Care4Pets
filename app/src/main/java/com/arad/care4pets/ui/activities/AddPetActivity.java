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

    private PetViewModel petViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        EditText etName    = findViewById(R.id.etPetName);
        EditText etSpecies = findViewById(R.id.etPetSpecies);
        EditText etAge     = findViewById(R.id.etPetAge);
        EditText etWeight  = findViewById(R.id.etPetWeight);
        EditText etNotes   = findViewById(R.id.etPetNotes);
        Button btnSave     = findViewById(R.id.btnSavePet);

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

            int age    = Integer.parseInt(ageText);
            int weight = Integer.parseInt(weightText);

            // health defaults to 100 for a new pet
            Pet pet = new Pet(name, species, age, notes, weight, 100);
            petViewModel.insert(pet); // ViewModel stamps userId automatically

            Toast.makeText(this, "Pet added", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}