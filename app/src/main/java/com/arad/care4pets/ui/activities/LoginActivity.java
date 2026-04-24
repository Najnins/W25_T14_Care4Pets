package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arad.care4pets.R;
import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.model.User;
import com.arad.care4pets.data.repository.AppRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    // Repository handles data operations
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the UI layout for this activity
        setContentView(R.layout.activity_login);

        // Initialize repository with application context
        repository = new AppRepository(getApplication());

        // Get references to UI elements
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            // Safely extract input values
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

            // Validation: check empty fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Run login process in a background thread
            Executors.newSingleThreadExecutor().execute(() -> {
                // Attempt to authenticate the user
                User user = repository.login(email, password);

                // Switch back to UI thread to update UI elements
                runOnUiThread(() -> {
                    if (user == null) {
                        // Vague error message for security (don't reveal to user which field failed)
                        Toast.makeText(this,
                                "Incorrect email or password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Successful login:
                    // Save user session (email and ID)
                    UserSessionManager.login(this, user.getEmail(), user.getId());

                    // Navigate to the dashboard screen
                    startActivity(new Intent(this, DashboardActivity.class));

                    // Close login screen
                    finish();
                });
            });
        });

        // Navigate to the signup screen when user clicks "Create Account"
        btnCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));
    }
}