package com.example.mymealmateproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        // Password visibility toggle buttons
        ImageView togglePasswordVisibility = findViewById(R.id.iv_toggle_password);
        ImageView toggleConfirmPasswordVisibility = findViewById(R.id.iv_toggle_confirm_password);

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24); // 'eye' icon
            } else {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24); // 'eye slash' icon
            }
            passwordField.setSelection(passwordField.getText().length()); // Maintain cursor position
        });

        // Toggle confirm password visibility
        toggleConfirmPasswordVisibility.setOnClickListener(v -> {
            if (confirmPasswordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                confirmPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleConfirmPasswordVisibility.setImageResource(R.drawable.baseline_visibility_24); // 'eye' icon
            } else {
                confirmPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleConfirmPasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24); // 'eye slash' icon
            }
            confirmPasswordField.setSelection(confirmPasswordField.getText().length()); // Maintain cursor position
        });

        // Register button click listener
        registerButton.setOnClickListener(v -> {
            // Retrieve user input
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();
            String phone = phoneField.getText().toString().trim();

            // Check for empty fields
            if (username.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate inputs
            if (!validateInputs(username, email, password, confirmPassword, phone)) {
                return;
            }

            // Initialize database helper
            DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);

            // Check if email or phone number is already registered
            boolean isEmailUnique = dbHelper.isEmailUnique(email);
            boolean isPhoneUnique = dbHelper.isPhoneNumberUnique(phone);

            if (!isEmailUnique && !isPhoneUnique) {
                Toast.makeText(this, "Both email and phone number are already registered. Please use different details.", Toast.LENGTH_LONG).show();
            } else if (!isEmailUnique) {
                Toast.makeText(this, "Email is already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
            } else if (!isPhoneUnique) {
                Toast.makeText(this, "Phone number is already registered. Please use a different phone number.", Toast.LENGTH_SHORT).show();
            } else {
                // Save user profile in SharedPreferences
                saveUserProfile(username, email, phone);

                // Save details in the database
                boolean isInserted = dbHelper.insertUser(username, email, password, phone);

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful! Your details have been saved.", Toast.LENGTH_SHORT).show();
                    // Navigate to LoginActivity
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close RegisterActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login text click listener
        loginTextView.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Method to save user profile in SharedPreferences
    private void saveUserProfile(String name, String email, String phone) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Phone", phone);
        editor.apply();  // Save changes
    }

    // Input validation method
    private boolean validateInputs(String username, String email, String password, String confirmPassword, String phone) {
        // Validate username (letters and spaces only)
        if (!username.matches("^[a-zA-Z\\s]+$")) {
            Toast.makeText(this, "Invalid username. Only letters and spaces are allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate phone number format for Bangladesh
        if (!phone.matches("^(\\+8801[3-9]\\d{8})|(01[3-9]\\d{8})$")) {
            Toast.makeText(this, "Phone number must start with +880 or 01 and have 11 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate password strength
        if (password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[!@#$%^&*].*")) {
            Toast.makeText(this, "Password must be at least 8 characters, include upper, lower, number, and special character.", Toast.LENGTH_SHORT).show();
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
