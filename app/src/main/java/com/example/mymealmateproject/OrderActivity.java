package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderActivity extends AppCompatActivity {
    private ListView listViewCart;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        listViewCart= findViewById(R.id.list_view_cart);
        Button buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        databaseHelper = new DatabaseHelper(this);
        displayCartItems();

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder();
            }
        });
    }

    private void displayCartItems() {
        Cursor cursor = databaseHelper.getAllCartItems();
        CartAdapter adapter = new CartAdapter(this, cursor, 0);
        listViewCart.setAdapter(adapter);
    }
    private void PlaceOrder() {
        Cursor cursor = databaseHelper.getAllCartItems();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_QUANTITY));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_IMAGE_URI));

                databaseHelper.placeOrder(name, price, quantity, imageBytes);
            } while (cursor.moveToNext());
        }
        cursor.close();

        databaseHelper.clearCart();
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(OrderActivity.this, ProductDisplay.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

}