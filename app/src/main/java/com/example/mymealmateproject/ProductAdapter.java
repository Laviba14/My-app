package com.example.mymealmateproject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;

public class ProductAdapter extends CursorAdapter {

    private DatabaseHelper databaseHelper;

    public ProductAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find views
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.product_price);
        ImageView productImageView = view.findViewById(R.id.product_image);
        ImageView saveToCartIcon = view.findViewById(R.id.save_to_cart_icon);

        // Extract data from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_PRICE));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_IMAGE_URI));

        // Populate the views
        nameTextView.setText(name);
        priceTextView.setText(String.format("Price: $%.2f", price));
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            productImageView.setImageBitmap(bitmap);
        } else {
            productImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // Check if the product is already in the cart
        boolean isFavorite = databaseHelper.isProductInCart(name);

        // Set the initial icon state
        saveToCartIcon.setImageResource(isFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);

        // Save to cart functionality with toggle
        saveToCartIcon.setOnClickListener(v -> {
            boolean currentFavoriteState = databaseHelper.isProductInCart(name);
            if (currentFavoriteState) {
                // Remove from cart
                boolean isRemoved = databaseHelper.removeFromCart(name);
                if (isRemoved) {
                    Toast.makeText(context, name + " removed from cart.", Toast.LENGTH_SHORT).show();
                    saveToCartIcon.setImageResource(R.drawable.baseline_favorite_border_24);
                    // Update cart item count in the main activity
                    ((ProductDisplay) context).updateCartBadge();
                } else {
                    Toast.makeText(context, "Failed to remove " + name + " from cart.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add to cart
                boolean isAdded = databaseHelper.addToCart(name, price, 1, imageBytes);
                if (isAdded) {
                    Toast.makeText(context, name + " added to cart.", Toast.LENGTH_SHORT).show();
                    saveToCartIcon.setImageResource(R.drawable.baseline_favorite_24);
                    // Update cart item count in the main activity
                    ((ProductDisplay) context).updateCartBadge();
                } else {
                    Toast.makeText(context, "Failed to add " + name + " to cart.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
