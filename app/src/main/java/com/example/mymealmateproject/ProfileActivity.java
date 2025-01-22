package com.example.mymealmateproject;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, phoneEditText;
    private TextView nameTextView, emailTextView, phoneTextView;
    private Button saveButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);
        saveButton = findViewById(R.id.buttonSave);

        nameTextView = findViewById(R.id.textViewName);
        emailTextView = findViewById(R.id.textViewEmail);
        phoneTextView = findViewById(R.id.textViewPhone);

        databaseHelper = new DatabaseHelper(this);

        // Load user profile from the database and SharedPreferences
        loadUserProfile();

        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "No email found! Please register first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getUserByEmailCursor(email);

        if (cursor != null && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COL_USERNAME);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COL_EMAIL);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COL_MOBILE);

            if (usernameIndex == -1 || emailIndex == -1 || phoneIndex == -1) {
                Toast.makeText(this, "Database column names are incorrect!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            String username = cursor.getString(usernameIndex);
            String userEmail = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);

            // Set data in UI fields
            usernameEditText.setText(username);
            emailEditText.setText(userEmail);
            phoneEditText.setText(phone);

            nameTextView.setText(username);
            emailTextView.setText(userEmail);
            phoneTextView.setText(phone);

            cursor.close();
        } else {
            Toast.makeText(this, "User details not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String currentEmail = sharedPreferences.getString("Email", "");

        String username = usernameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseHelper.updateUserProfile(currentEmail, username, newEmail, phone);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email", newEmail);
            editor.apply();

            nameTextView.setText(username);
            emailTextView.setText(newEmail);
            phoneTextView.setText(phone);
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }
}
