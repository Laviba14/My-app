package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDisplay extends AppCompatActivity {
    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        listViewProducts = findViewById(R.id.list_view_products);
       Button buttonAddToCart = findViewById(R.id.buttonAddToCart);
        databaseHelper = new DatabaseHelper(this);

        displayProducts();

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                handleCart();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayProducts();
    }

    private void displayProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        ProductAdapter adapter = new ProductAdapter(this, cursor, 0);
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productName, double productPrice, int productQuantity, byte[] productImage) {
                Log.d("ProductDisplay", "Product clicked: " + productName);
                handleProductClick(productName, productPrice, productQuantity, productImage);
            }
        });
        listViewProducts.setAdapter(adapter);
    }

    private void handleProductClick(String productName, double productPrice, int productQuantity, byte[] productImage) {
        Log.d("ProductDisplay", "Adding product to cart: " + productName);

        // Logic for handling product click
        Intent intent = new Intent(ProductDisplay.this, CartActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productPrice", productPrice);
        intent.putExtra("productQuantity", productQuantity);
        intent.putExtra("productImage", productImage);
        startActivity(intent);
        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
    }

    private void handleCart() {

        Intent intent = new Intent(ProductDisplay.this, CartActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Viewing cart", Toast.LENGTH_SHORT).show();
    }




}
