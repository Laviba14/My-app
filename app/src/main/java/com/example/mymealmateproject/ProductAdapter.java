package com.example.mymealmateproject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends CursorAdapter {
    private OnItemClickListener onItemClickListener;
    private DatabaseHelper databaseHelper;

    public interface OnItemClickListener {
        void onAddToCartClick(String productName, double productPrice, int productQuantity, byte[] productImage);
        void onBuyNowClick(String productName, double productPrice, int productQuantity, byte[] productImage);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        databaseHelper = new DatabaseHelper(context);  // Initialize DatabaseHelper here
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Initialize views for the first product layout
        TextView nameTextView1 = view.findViewById(R.id.product_name);
        TextView priceTextView1 = view.findViewById(R.id.product_price);
        Button addToCartButton1 = view.findViewById(R.id.add_to_cart_button);
        Spinner quantitySpinner1 = view.findViewById(R.id.quantity_spinner);
        ImageView productImage1 = view.findViewById(R.id.product_image);

        // Initialize views for the second product layout (if needed)
        TextView nameTextView2 = view.findViewById(R.id.product_name2);
        TextView priceTextView2 = view.findViewById(R.id.product_price2);
        Button addToCartButton2 = view.findViewById(R.id.add_to_cart_button2);
        Spinner quantitySpinner2 = view.findViewById(R.id.quantity_spinner2);
        ImageView productImage2 = view.findViewById(R.id.product_image2);

        // Fetch product details from cursor for the first product
        String productName1 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        double productPrice1 = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        int productQuantity1 = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        byte[] image1 = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

        // Set data to views for the first product
        nameTextView1.setText(productName1);
        priceTextView1.setText(String.format("$%.2f", productPrice1));
        if (image1 != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image1, 0, image1.length);
            productImage1.setImageBitmap(bitmap);
        } else {
            productImage1.setImageResource(R.drawable.pizza);  // Set a default image if no image is available
        }

        // Handle "Add to Cart" button click for the first product
        addToCartButton1.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onAddToCartClick(productName1, productPrice1, productQuantity1, image1);
                Toast.makeText(context, productName1 + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle "Buy Now" button click for the first product
        addToCartButton2.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onBuyNowClick(productName1, productPrice1, productQuantity1, image1);
            }
        });

        // Fetch product details for the second product (if necessary)
        String productName2 = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        double productPrice2 = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        int productQuantity2 = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        byte[] image2 = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

        // Set data to views for the second product (if necessary)
        nameTextView2.setText(productName2);
        priceTextView2.setText(String.format("$%.2f", productPrice2));
        if (image2 != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image2, 0, image2.length);
            productImage2.setImageBitmap(bitmap);
        } else {
            productImage2.setImageResource(R.drawable.pizza);  // Set a default image if no image is available
        }

        // Handle "Add to Cart" button click for the second product
        addToCartButton2.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onAddToCartClick(productName2, productPrice2, productQuantity2, image2);
                Toast.makeText(context, productName2 + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle "Buy Now" button click for the second product
        addToCartButton2.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onBuyNowClick(productName2, productPrice2, productQuantity2, image2);
            }
        });
    }
}
