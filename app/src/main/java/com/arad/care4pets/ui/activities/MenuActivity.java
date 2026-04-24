package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.arad.care4pets.R;
import com.arad.care4pets.utils.UserSessionManager;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for the menu screen
        setContentView(R.layout.activity_menu);

        // Get references to all menu cards
        CardView cardDashboard = findViewById(R.id.cardDashboard);
        CardView cardPets = findViewById(R.id.cardPets);
        CardView cardReminders = findViewById(R.id.cardReminders);
        CardView cardLogout = findViewById(R.id.cardLogout);

        // Navigate to the Dashboard screen when dashboard card is clicked
        cardDashboard.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, DashboardActivity.class));
        });

        // Navigate to the Pets list screen
        cardPets.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, PetsListActivity.class));
        });

        // Navigate to the Reminders screen
        cardReminders.setOnClickListener(v -> {
            startActivity(new Intent(MenuActivity.this, RemindersActivity.class));
        });

        // Handle logout action with confirmation dialog
        cardLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    // Dialog title
                    .setTitle("Log out")
                    // Confirmation message
                    .setMessage("Are you sure you want to log out?")
                    // If user confirms logout
                    .setPositiveButton("Log Out", (dialog, which) -> {
                        // Clear saved user session
                        UserSessionManager.logout(this);

                        // Redirect the user to login screen
                        Intent intent = new Intent(this, LoginActivity.class);

                        // Clear back stack so that the user can't return after logging out
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                        // Finish Menu activity
                        finish();
                    })
                    // Cancel button closes dialog
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
