package com.example.mymealmateproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // References to layouts
        View thankYouLayout = findViewById(R.id.thankYouLayoutTop);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);

        // Show the Thank You Section
        thankYouLayout.setVisibility(View.VISIBLE);

        // Display Rating Dialog after 2 seconds
        new Handler().postDelayed(this::showRatingDialog, 2000);

        // Back to Home Button functionality
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class); // Replace with your home activity
            startActivity(intent);
            finish();
        });
    }

    private void showRatingDialog() {
        // Inflate the custom rating dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);

        // Find the RatingBar, EditText, and Submit Button in the dialog layout
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText commentEditText = dialogView.findViewById(R.id.commentEditText);
        Button submitButton = dialogView.findViewById(R.id.submitRatingButton);

        // Build the AlertDialog
        AlertDialog ratingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        // Submit Rating Button functionality
        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentEditText.getText().toString().trim();

            // Check if a comment was entered
            if (comment.isEmpty()) {
                Toast.makeText(ConfirmationActivity.this, "Please provide a comment before submitting.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        ConfirmationActivity.this,
                        "Thanks for your feedback! You gave " + rating + " stars.\nComment: " + comment,
                        Toast.LENGTH_LONG
                ).show();
                ratingDialog.dismiss(); // Close the dialog
            }
        });

        // Show the dialog
        ratingDialog.show();
    }
}
