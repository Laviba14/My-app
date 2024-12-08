package com.example.mymealmateproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends CursorAdapter {

    private DatabaseHelper databaseHelper; // Add a reference to DatabaseHelper

    public ProductAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.databaseHelper = new DatabaseHelper(context); // Initialize DatabaseHelper
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get views from the layout
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.product_price);
        ImageView productImageView = view.findViewById(R.id.product_image);
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);

        // Extract data from the cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_PRICE));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

        // Set the text and image in the view
        nameTextView.setText(name);
        priceTextView.setText("Price: $" + price);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        productImageView.setImageBitmap(bitmap);

        // Set click listener for "Add to Cart" button
        addToCartButton.setOnClickListener(v -> {
            // Add product to cart using the DatabaseHelper
            boolean isAdded = databaseHelper.addToCart(name, price, 1, imageBytes); // Add product with quantity 1
            if (isAdded) {
                Toast.makeText(context, name + " added to cart.", Toast.LENGTH_SHORT).show();

                // Optional: Navigate to CartActivity if needed
                Intent intent = new Intent(context, CartActivity.class);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Failed to add " + name + " to cart.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the cursor dynamically and refresh the data
    public void updateCursor(Cursor newCursor) {
        // Close the old cursor to avoid memory leaks
        Cursor oldCursor = swapCursor(newCursor);
        if (oldCursor != null && !oldCursor.isClosed()) {
            oldCursor.close();
        }
        // Notify the adapter that data has changed
        notifyDataSetChanged();
    }
}
