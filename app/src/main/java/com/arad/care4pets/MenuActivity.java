package com.arad.care4pets;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Find all the CardViews in the menu
        CardView cardDashboard = findViewById(R.id.cardDashboard);
        CardView cardPets = findViewById(R.id.cardPets);
        CardView cardReminders = findViewById(R.id.cardReminders);
        CardView cardLogout = findViewById(R.id.cardLogout);

        // Set click listeners for each card
        cardDashboard.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, DashboardActivity.class));
        });

        cardPets.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, PetsListActivity.class));
        });

        cardReminders.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, RemindersActivity.class));
        });


        cardLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Log out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out", (dialog, which) ->{
                        UserSessionManager.logout(this);
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
