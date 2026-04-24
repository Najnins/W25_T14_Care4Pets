package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.arad.care4pets.viewmodel.HealthRecordViewModel;
import com.arad.care4pets.viewmodel.PetViewModel;
import com.arad.care4pets.R;
import com.arad.care4pets.viewmodel.ReminderViewModel;
import com.arad.care4pets.data.model.Pet;

public class PetProfileActivity extends AppCompatActivity {

    // Currently displayed pet (updated via LiveData)
    private Pet pet;

    // ViewModel for pet-related operations (fetch, delete, etc.)
    private PetViewModel petViewModel;

    // UI elements for displaying pet info
    private TextView tvPetName, tvPetSpecies;

    // Card views for different pet info sections
    private View ageCard, weightCard, healthCard, recordsCard, upcomingCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for pet profile screen
        setContentView(R.layout.activity_pet_profile);

        // Get pet passed from previous activity
        Pet petFromIntent = getIntent().getParcelableExtra("pet");

        // If no pet was passed, show error and exit
        if (petFromIntent == null) {
            Toast.makeText(this, "Error: Pet not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModels for data handling
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        HealthRecordViewModel healthRecordViewModel =
                new ViewModelProvider(this).get(HealthRecordViewModel.class);
        ReminderViewModel reminderViewModel =
                new ViewModelProvider(this).get(ReminderViewModel.class);

        // Initialize UI components
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        ImageButton fabFavorite = findViewById(R.id.fabFavorite);
        ImageView ivPetImage = findViewById(R.id.ivPetImage);
        tvPetName = findViewById(R.id.tvPetName);
        tvPetSpecies = findViewById(R.id.tvPetSpecies);

        ageCard = findViewById(R.id.age_card);
        weightCard = findViewById(R.id.weight_card);
        recordsCard = findViewById(R.id.records_card);
        upcomingCard = findViewById(R.id.upcoming_card);
        healthCard = findViewById(R.id.health_card);

        // Buttons for additional actions
        Button btnViewHealthRecords = findViewById(R.id.btnViewHealthRecords);
        Button btnAddHealthRecord = findViewById(R.id.btnAddHealthRecord);
        Button btnCareInstructions = findViewById(R.id.btnCareInstructions);

        // Set default pet image (placeholder)
        ivPetImage.setImageResource(R.drawable.dog_placeholder);

        // Observe pet data from database using LiveData
        // UI will automatically update when data changes
        petViewModel.getPetById(petFromIntent.getId()).observe(this, updatedPet -> {
            if (updatedPet == null) {
                // Pet was deleted elsewhere → close this screen
                finish();
                return;
            }

            // Update local reference
            pet = updatedPet;

            // Update basic pet info
            tvPetName.setText(pet.getName());
            tvPetSpecies.setText(pet.getSpecies());

            // Populate Age card
            ((ImageView) ageCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_calendar);
            ((TextView) ageCard.findViewById(R.id.tvTitle)).setText(R.string.age);
            ((TextView) ageCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.years_format, pet.getAge()));

            // Populate Weight card
            ((ImageView) weightCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_weight);
            ((TextView) weightCard.findViewById(R.id.tvTitle)).setText(R.string.weight);
            ((TextView) weightCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.kg_format, pet.getWeight()));

            // Populate Health card
            ((ImageView) healthCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_heart);
            ((TextView) healthCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.percent_format, pet.getHealth()));
            ((TextView) healthCard.findViewById(R.id.tvTitle)).setText(R.string.health);
        });

        // Observe health records and update records count card
        healthRecordViewModel.getHealthRecordsForPet(petFromIntent.getId()).observe(this, records -> {
            ((ImageView) recordsCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_records);
            ((TextView) recordsCard.findViewById(R.id.tvValue)).setText(String.valueOf(records.size()));
            ((TextView) recordsCard.findViewById(R.id.tvTitle)).setText(R.string.records);
        });

        // Observe reminders and update upcoming reminders card
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            ((ImageView) upcomingCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_calendar);
            ((TextView) upcomingCard.findViewById(R.id.tvValue)).setText(String.valueOf(reminders.size()));
            ((TextView) upcomingCard.findViewById(R.id.tvTitle)).setText(R.string.upcoming);
        });

        // Back button simply closes this screen
        btnBack.setOnClickListener(v -> finish());

        // Placeholder favorite button (currently just shows a message)
        fabFavorite.setOnClickListener(v ->
                Toast.makeText(this, "Favorite clicked", Toast.LENGTH_SHORT).show());

        // Options menu (Edit/Delete pet)
        btnOptions.setOnClickListener(v -> {
            if (pet == null) return;

            new AlertDialog.Builder(this)
                    .setTitle(pet.getName())
                    .setItems(new String[]{"Edit Pet", "Delete Pet"}, (dialog, which) -> {
                        if (which == 0) {
                            // Navigate to edit screen with current pet data
                            Intent intent = new Intent(this, EditPetActivity.class);
                            intent.putExtra("pet", pet);
                            startActivity(intent);

                            // No manual refresh needed — LiveData handles UI updates
                        } else {
                            // Confirm deletion with warning dialog
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete " + pet.getName())
                                    .setMessage("This will permanently delete " + pet.getName() +
                                            " and all their health records. This cannot be undone.")
                                    .setPositiveButton("Delete", (d, w) -> {
                                        // Delete pet from database
                                        petViewModel.delete(pet);

                                        // Close screen after deletion
                                        finish();
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    })
                    .show();
        });

        // Navigate to health records list for this pet
        btnViewHealthRecords.setOnClickListener(v -> {
            if (pet == null) return;

            Intent intent = new Intent(this, HealthRecordsActivity.class);
            intent.putExtra("pet_id", pet.getId());
            intent.putExtra("pet_name", pet.getName());
            startActivity(intent);
        });

        // Navigate to add new health record screen
        btnAddHealthRecord.setOnClickListener(v -> {
            if (pet == null) return;

            Intent intent = new Intent(this, AddHealthRecordActivity.class);
            intent.putExtra("pet_id", pet.getId());
            startActivity(intent);
        });

        // Navigate to care instructions screen
        btnCareInstructions.setOnClickListener(v ->
                startActivity(new Intent(this, CareInstructionsActivity.class)));
    }
}