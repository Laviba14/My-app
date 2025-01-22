package com.example.mymealmateproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat notificationSwitch;
    private Spinner languageSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        notificationSwitch = findViewById(R.id.notification_switch);
        languageSpinner = findViewById(R.id.language_spinner);
        saveButton = findViewById(R.id.save_button);

        // Show the back arrow in the action bar
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load saved settings
        loadSettings();

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            boolean isNotificationsEnabled = notificationSwitch.isChecked();
            String selectedLanguage = languageSpinner.getSelectedItem().toString();

            // Save settings to Database
            saveSettings(isNotificationsEnabled, selectedLanguage);

            // Display settings feedback
            Toast.makeText(this, "Settings Saved: " + selectedLanguage + ", Notifications: " + isNotificationsEnabled, Toast.LENGTH_SHORT).show();
        });
    }

    // Handle back arrow click (navigate back)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // This will navigate back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to load saved settings
    private void loadSettings() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try (Cursor cursor = dbHelper.loadUserSettings()) {
            if (cursor != null && cursor.moveToFirst()) {
                // Safely fetch column indices
                int notificationsIndex = cursor.getColumnIndex("notifications_enabled");
                int languageIndex = cursor.getColumnIndex("language");

                if (notificationsIndex != -1 && languageIndex != -1) {
                    boolean isNotificationsEnabled = cursor.getInt(notificationsIndex) == 1;
                    String language = cursor.getString(languageIndex);

                    // Apply loaded settings
                    notificationSwitch.setChecked(isNotificationsEnabled);
                    languageSpinner.setSelection(getLanguagePosition(language));
                } else {
                    Toast.makeText(this, "Settings data is incomplete or corrupted.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If no settings were found in the database
                Toast.makeText(this, "No settings found. Using default settings.", Toast.LENGTH_SHORT).show();
                notificationSwitch.setChecked(false);  // Default value
                languageSpinner.setSelection(0);      // Default to the first language
            }
        } catch (Exception e) {
            // Handle any errors with the cursor or database operations
            Toast.makeText(this, "Error loading settings.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the position of the selected language in the Spinner
    private int getLanguagePosition(String language) {
        String[] languages = getResources().getStringArray(R.array.language_options);
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                return i;
            }
        }
        return 0; // Default to the first language (English)
    }

    // Method to save user settings (notifications and language) to the database
    private void saveSettings(boolean notificationsEnabled, String language) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.saveUserSettings(notificationsEnabled, language);
    }
}
