package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

public class CartActivity extends AppCompatActivity {
    private ListView listViewCart;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listViewCart = findViewById(R.id.list_view_cart);
       Button buttonOrder = findViewById(R.id.buttonOrder);

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productName")) {
            String productName = intent.getStringExtra("productName");
            double productPrice = intent.getDoubleExtra("productPrice", 0);
            int productQuantity = intent.getIntExtra("productQuantity", 0);
            byte[] productImage = intent.getByteArrayExtra("productImage");

            databaseHelper.addToCart(productName, productPrice, productQuantity, productImage);
        }

        displayCartItems();

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrder();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        displayCartItems();
    }

    private void displayCartItems() {
        Cursor cursor = databaseHelper.getAllCartItems();
        CartAdapter adapter = new CartAdapter(this, cursor, 0);
        listViewCart.setAdapter(adapter);
    }
    private void handleOrder() {
        Intent intent = new Intent(CartActivity.this, OrderActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Proceeding to Order", Toast.LENGTH_SHORT).show();
    }



    }
