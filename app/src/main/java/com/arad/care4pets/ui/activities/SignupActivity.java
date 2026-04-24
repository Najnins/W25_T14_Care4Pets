package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arad.care4pets.utils.PasswordUtils;
import com.arad.care4pets.R;
import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.repository.AppRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    // Repository for handling user data (database operations)
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for signup screen
        setContentView(R.layout.activity_signup);

        // Initialize repository
        repository = new AppRepository(getApplication());

        // Get references to UI elements
        TextInputEditText etName = findViewById(R.id.etName);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        // Handle "Create Account" button click
        btnCreateAccount.setOnClickListener(v -> {
            // Safely extract input values
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            String confirm = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

            // ── Validation ────────────────────────────────────────────────────

            // Check for empty fields
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password strength using utility method
            String passwordError = PasswordUtils.validate(password);
            if (passwordError != null) {
                Toast.makeText(this, passwordError, Toast.LENGTH_LONG).show();
                return;
            }

            // Ensure passwords match
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // ── Save to database on background thread ─────────────────────────

            // Run database operation off main thread
            Executors.newSingleThreadExecutor().execute(() -> {
                long newUserId = repository.registerUser(email, password, name);

                // Switch back to UI thread to update UI
                runOnUiThread(() -> {
                    if (newUserId == -1) {
                        // Email already exists (unique constraint failed)
                        Toast.makeText(this,
                                "An account with this email already exists",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Save user session after successful signup
                    UserSessionManager.login(this, email, (int) newUserId);

                    // Navigate to main menu
                    startActivity(new Intent(this, MenuActivity.class));

                    // Close signup screen
                    finish();
                });
            });
        });

        // Navigate to login screen if user already has an account
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}