package com.example.mymealmateproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private TextView tvRegister;
    private ImageView ivTogglePassword;  // ImageView for password visibility toggle
    private boolean isPasswordVisible = false;  // To track password visibility state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        rememberMe = findViewById(R.id.remember_me);
        tvRegister = findViewById(R.id.tv_register);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        // Check if 'remember_me' is true, and auto-login if credentials exist
        SharedPreferences loginPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isRemembered = loginPreferences.getBoolean("remember_me", false);
        if (isRemembered) {
            String userEmail = loginPreferences.getString("user_email", "");
            String userPassword = loginPreferences.getString("user_password", "");
            login(userEmail, userPassword);  // Auto-login using stored credentials
            return;
        }

        // Restore saved credentials if "Remember Me" was previously checked
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("Email", "");
        String savedPassword = sharedPreferences.getString("Password", "");
        etEmail.setText(savedEmail);
        etPassword.setText(savedPassword);

        // Set up Register button click listener
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up Login button click listener
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check hardcoded login for admin (optional)
            if (email.equals("admin") && password.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            // Validate credentials against the database
            DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
            if (dbHelper.isUserValid(email, password)) {
                // Save "Remember Me" preference if checked
                SharedPreferences.Editor editor = loginPreferences.edit();
                if (rememberMe.isChecked()) {
                    editor.putBoolean("remember_me", true);
                    editor.putString("user_email", email);
                    editor.putString("user_password", password);
                } else {
                    editor.clear(); // Clear preferences if "Remember Me" is unchecked
                }
                editor.putBoolean("isLoggedIn", true); // Set the login state to true
                editor.apply();

                // Successful login
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProductDisplay.class);
                startActivity(intent);
                finish(); // Close LoginActivity
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the toggle password visibility click listener
        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // If password is currently visible, hide it
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.baseline_visibility_off_24);  // Set icon to 'off'
            } else {
                // If password is currently hidden, show it
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.baseline_visibility_24);  // Set icon to 'on'
            }
            isPasswordVisible = !isPasswordVisible;  // Toggle the visibility state
            etPassword.setSelection(etPassword.getText().length());  // Move the cursor to the end of the text
        });
    }

    // Method to handle auto-login using stored credentials
    private void login(String email, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
        if (dbHelper.isUserValid(email, password)) {
            // Successful auto-login
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, ProductDisplay.class);
            startActivity(intent);
            finish(); // Close LoginActivity
        } else {
            // Clear "remember me" if invalid credentials (optional safeguard)
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            Toast.makeText(LoginActivity.this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
        }
    }
}
