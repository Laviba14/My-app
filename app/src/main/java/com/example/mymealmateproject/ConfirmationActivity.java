package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView thankYouText, orderConfirmationMessage;
    private RatingBar ratingBar;
    private RadioGroup ratingGroup;
    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize UI components
        thankYouText = findViewById(R.id.thankYouText);
        orderConfirmationMessage = findViewById(R.id.orderConfirmationMessage);
        ratingBar = findViewById(R.id.ratingBar);
        ratingGroup = findViewById(R.id.ratingGroup);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Set click listener for the submit review button
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected rating option
                int selectedRatingId = ratingGroup.getCheckedRadioButtonId();
                RadioButton selectedRating = findViewById(selectedRatingId);

                // Check if a rating option is selected
                if (selectedRating == null) {
                    Toast.makeText(ConfirmationActivity.this, "Please select a rating", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get rating from RatingBar
                float rating = ratingBar.getRating();

                // Display a toast message for confirmation
                String reviewMessage = "Thank you for your feedback!\n" +
                        "Your rating: " + selectedRating.getText() + "\n" +
                        "Stars: " + rating;
                Toast.makeText(ConfirmationActivity.this, reviewMessage, Toast.LENGTH_LONG).show();

                // Optionally, you can handle storing the review or sending it to a server here

                // Optionally, navigate to another activity, like a main page or thank you page.
                Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class); // Or wherever you want to navigate
                startActivity(intent);
                finish(); // Close the ConfirmationActivity
            }
        });
    }
}
