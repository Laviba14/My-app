package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView etWelcome, etStarted;
    private Switch notificationSwitch;  // Declare the Switch variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWelcome = findViewById(R.id.et_welcome);
        etStarted = findViewById(R.id.et_started);
        notificationSwitch = findViewById(R.id.notificationSwitch);  // Initialize the Switch

        // Apply fade-in animation to text
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(3000);
        fadeIn.setFillAfter(true);

        etWelcome.startAnimation(fadeIn);
        etStarted.startAnimation(fadeIn);

        // Set notification switch listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show a toast when the user enables notifications
                Toast.makeText(MainActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Show a toast when the user disables notifications
                Toast.makeText(MainActivity.this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Automatically navigate to LoginActivity after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }).start();
    }
}
