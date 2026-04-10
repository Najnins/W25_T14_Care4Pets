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

    private Pet pet;
    private PetViewModel petViewModel;
    private TextView tvPetName, tvPetSpecies;
    private View ageCard, weightCard, healthCard, recordsCard, upcomingCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        Pet petFromIntent = getIntent().getParcelableExtra("pet");
        if (petFromIntent == null) {
            Toast.makeText(this, "Error: Pet not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        HealthRecordViewModel healthRecordViewModel =
                new ViewModelProvider(this).get(HealthRecordViewModel.class);
        ReminderViewModel reminderViewModel =
                new ViewModelProvider(this).get(ReminderViewModel.class);

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

        Button btnViewHealthRecords = findViewById(R.id.btnViewHealthRecords);
        Button btnAddHealthRecord = findViewById(R.id.btnAddHealthRecord);
        Button btnCareInstructions = findViewById(R.id.btnCareInstructions);

        ivPetImage.setImageResource(R.drawable.dog_placeholder);

        // Pet here automatically and the UI refreshes without any extra work.
        petViewModel.getPetById(petFromIntent.getId()).observe(this, updatedPet -> {
            if (updatedPet == null) {
                // Pet was deleted — go back to the list
                finish();
                return;
            }


            pet = updatedPet;


            tvPetName.setText(pet.getName());
            tvPetSpecies.setText(pet.getSpecies());

            ((ImageView) ageCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_calendar);
            ((TextView)  ageCard.findViewById(R.id.tvTitle)).setText(R.string.age);
            ((TextView)  ageCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.years_format, pet.getAge()));

            ((ImageView) weightCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_weight);
            ((TextView)  weightCard.findViewById(R.id.tvTitle)).setText(R.string.weight);
            ((TextView)  weightCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.kg_format, pet.getWeight()));

            ((ImageView) healthCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_heart);
            ((TextView)  healthCard.findViewById(R.id.tvValue)).setText(
                    getString(R.string.percent_format, pet.getHealth()));
            ((TextView)  healthCard.findViewById(R.id.tvTitle)).setText(R.string.health);
        });

        healthRecordViewModel.getHealthRecordsForPet(petFromIntent.getId()).observe(this, records -> {
            ((ImageView) recordsCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_records);
            ((TextView)  recordsCard.findViewById(R.id.tvValue)).setText(String.valueOf(records.size()));
            ((TextView)  recordsCard.findViewById(R.id.tvTitle)).setText(R.string.records);
        });

        reminderViewModel.getAllReminders().observe(this, reminders -> {
            ((ImageView) upcomingCard.findViewById(R.id.ivIcon)).setImageResource(R.drawable.ic_calendar);
            ((TextView)  upcomingCard.findViewById(R.id.tvValue)).setText(String.valueOf(reminders.size()));
            ((TextView)  upcomingCard.findViewById(R.id.tvTitle)).setText(R.string.upcoming);
        });

        btnBack.setOnClickListener(v -> finish());
        fabFavorite.setOnClickListener(v ->
                Toast.makeText(this, "Favorite clicked", Toast.LENGTH_SHORT).show());

        btnOptions.setOnClickListener(v -> {
            if (pet == null) return;
            new AlertDialog.Builder(this)
                    .setTitle(pet.getName())
                    .setItems(new String[]{"Edit Pet", "Delete Pet"}, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(this, EditPetActivity.class);
                            intent.putExtra("pet", pet);
                            startActivity(intent);
                            // No need to handle result — the LiveData observer
                            // above will automatically refresh the UI when Room
                            // emits the updated pet after EditPetActivity saves
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete " + pet.getName())
                                    .setMessage("This will permanently delete " + pet.getName() +
                                            " and all their health records. This cannot be undone.")
                                    .setPositiveButton("Delete", (d, w) -> {
                                        petViewModel.delete(pet);
                                        finish();
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    })
                    .show();
        });

        btnViewHealthRecords.setOnClickListener(v -> {
            if (pet == null) return;
            Intent intent = new Intent(this, HealthRecordsActivity.class);
            intent.putExtra("pet_id", pet.getId());
            intent.putExtra("pet_name", pet.getName());
            startActivity(intent);
        });

        btnAddHealthRecord.setOnClickListener(v -> {
            if (pet == null) return;
            Intent intent = new Intent(this, AddHealthRecordActivity.class);
            intent.putExtra("pet_id", pet.getId());
            startActivity(intent);
        });

        btnCareInstructions.setOnClickListener(v ->
                startActivity(new Intent(this, CareInstructionsActivity.class)));
    }
}