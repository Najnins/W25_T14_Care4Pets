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

    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new AppRepository(getApplication());

        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = repository.login(email, password);

                runOnUiThread(() -> {
                    if (user == null) {
                        // Don't tell the user WHICH field is wrong for security
                        Toast.makeText(this,
                                "Incorrect email or password",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Valid login — save session
                    UserSessionManager.login(this, user.getEmail(), user.getId());
                    startActivity(new Intent(this, DashboardActivity.class));
                    finish();
                });
            });
        });

        btnCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));
    }
}