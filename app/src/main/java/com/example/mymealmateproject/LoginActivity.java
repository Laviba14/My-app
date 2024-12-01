package com.example.mymealmateproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private CheckBox rememberMe;
    private TextView tvRegister;

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

        // Check if credentials are already saved in SharedPreferences (for "Remember Me" functionality)
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("Email", "");
        String savedPassword = sharedPreferences.getString("Password", "");
        etEmail.setText(savedEmail);
        etPassword.setText(savedPassword);

        // Set up Register button click listener
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Make sure RegisterActivity exists and is imported
            startActivity(intent);
        });

        // Set up Login button click listener
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // Check hardcoded login for admin (optional)
            if (email.equals("admin") && password.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class); // Make sure AdminHomeActivity exists
                startActivity(intent);
                finish();  // Close the LoginActivity so the user can't go back
                return;
            }

            // Check user credentials in the database (replace with your actual database validation logic)
            DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
            boolean result = dbHelper.checkUserByUsername(email, password);

            if (result) {
                // If Remember Me is checked, save the email and password
                if (rememberMe.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Email", email);
                    editor.putString("Password", password);
                    editor.apply();  // Save changes
                }

                // Successful login
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ProductDisplay.class); // Make sure ProductDisplay exists
                startActivity(intent);
                finish();  // Close the LoginActivity so the user can't go back
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
