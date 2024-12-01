package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        EditText usernameField = findViewById(R.id.et_username);
        EditText emailField = findViewById(R.id.et_email);
        EditText passwordField = findViewById(R.id.et_password);
        EditText confirmPasswordField = findViewById(R.id.et_confirm_password);
        EditText phoneField = findViewById(R.id.et_phone);
        Button registerButton = findViewById(R.id.btn_register);
        TextView loginTextView = findViewById(R.id.tv_login);

        // Register button click listener
        registerButton.setOnClickListener(v -> {
            // Retrieve user input
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();

            // Perform input validation
            if (password.equals(confirmPassword) && !password.isEmpty() && !username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Well done! Let me insert your info in DB!", Toast.LENGTH_SHORT).show();

                if (validateInputs(username, email, password, confirmPassword, phone)) {
                    DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
                    boolean isInserted = dbHelper.insertUser(username, email, password, phone);

                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to LoginActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close RegisterActivity
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Passwords do not match or empty fields!", Toast.LENGTH_SHORT).show();
            }
        });

        // Login text click listener
        loginTextView.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Input validation method
    private boolean validateInputs(String username, String email, String password, String confirmPassword, String phone) {
        // Validate username (only alphanumeric characters)
        if (!username.matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Invalid username. Only letters and numbers are allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate phone number format
        if (!phone.matches("\\+880\\d{9}")) {
            Toast.makeText(this, "Phone number must start with +880 and have 11 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password matches confirm password
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
