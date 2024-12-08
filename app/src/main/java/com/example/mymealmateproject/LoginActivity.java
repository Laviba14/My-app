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
        ivTogglePassword = findViewById(R.id.iv_toggle_password);  // Initialize the ImageView for password toggle

        // Check if credentials are already saved in SharedPreferences (for "Remember Me" functionality)
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
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            // Check hardcoded login for admin (optional)
            if (email.equals("admin") && password.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            // Check user credentials in the database (replace with your actual database validation logic)
            DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
            boolean result = dbHelper.checkUserByEmail(email);

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
                Intent intent = new Intent(LoginActivity.this, ProductDisplay.class);
                startActivity(intent);
                finish();
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
}
