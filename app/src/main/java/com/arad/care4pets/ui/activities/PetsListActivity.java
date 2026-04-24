package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.ui.adapters.PetAdapter;
import com.arad.care4pets.viewmodel.PetViewModel;
import com.arad.care4pets.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PetsListActivity extends AppCompatActivity {

    // Adapter for displaying pets in RecyclerView
    private PetAdapter adapter;

    // ViewModel to handle pet data (from database/repository)
    private PetViewModel petViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for pets list screen
        setContentView(R.layout.activity_pets_list);

        // Initialize UI components
        RecyclerView recycler = findViewById(R.id.rvPets);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddPet);
        ImageButton btnBack = findViewById(R.id.btnBackMenu);
        Button btnCareInstructions = findViewById(R.id.btnCareInstructions);

        // Initialize adapter with empty list and click handler
        // When a pet is clicked, navigate to its profile screen
        adapter = new PetAdapter(
                new ArrayList<>(),
                pet -> {
                    Intent intent = new Intent(PetsListActivity.this, PetProfileActivity.class);
                    intent.putExtra("pet", pet);
                    startActivity(intent);
                }
        );

        // Set layout manager (vertical scrolling list)
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Attach adapter to RecyclerView
        recycler.setAdapter(adapter);

        // Initialize ViewModel
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        // Observe pet list from database
        // Automatically updates UI when data changes
        petViewModel.getAllPets().observe(this, pets -> adapter.setPets(pets));

        // Floating button to add a new pet
        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(PetsListActivity.this, AddPetActivity.class))
        );

        // Back button returns to previous screen
        btnBack.setOnClickListener(v -> finish());

        // Navigate to care instructions screen
        btnCareInstructions.setOnClickListener(v -> {
            Intent intent = new Intent(this, CareInstructionsActivity.class);
            startActivity(intent);
        });
    }
}
