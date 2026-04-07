package com.arad.care4pets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        repository = new AppRepository(getApplication());

        TextInputEditText etName = findViewById(R.id.etName);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        btnCreateAccount.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            String confirm = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

            // ── Validation ────────────────────────────────────────────────────

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check password strength rules
            String passwordError = PasswordUtils.validate(password);
            if (passwordError != null) {
                Toast.makeText(this, passwordError, Toast.LENGTH_LONG).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // ── Save to database on background thread ─────────────────────────
            // registerUser() does a DB insert which must not run on the main thread
            Executors.newSingleThreadExecutor().execute(() -> {
                long newUserId = repository.registerUser(email, password, name);

                runOnUiThread(() -> {
                    if (newUserId == -1) {
                        Toast.makeText(this,
                                "An account with this email already exists",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    UserSessionManager.login(this, email, (int) newUserId);
                    startActivity(new Intent(this, MenuActivity.class));
                    finish();
                });
            });
        });

        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}