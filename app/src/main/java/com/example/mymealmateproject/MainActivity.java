package com.example.mymealmateproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView etWelcome, etStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWelcome = findViewById(R.id.et_welcome);
        etStarted = findViewById(R.id.et_started);

        // Apply fade-in animation to text
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000); // Animation duration
        fadeIn.setFillAfter(true); // Keep the text visible after the animation

        etWelcome.startAnimation(fadeIn);
        etStarted.startAnimation(fadeIn);

        // Check login state after animation is done using a handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check login state using SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    // If user is logged in, redirect to ProductDisplayActivity
                    Intent intent = new Intent(MainActivity.this, ProductDisplay.class);
                    startActivity(intent);
                } else {
                    // Otherwise, redirect to LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish(); // Close MainActivity to prevent going back to it
            }
        }, 2500); // Wait for 2.5 seconds to allow the animation to complete
    }
}
