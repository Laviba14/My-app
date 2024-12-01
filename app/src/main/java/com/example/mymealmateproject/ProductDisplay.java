package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDisplay extends AppCompatActivity {
    private ListView listViewProducts;
    private EditText searchBar;
    private DatabaseHelper databaseHelper;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        listViewProducts = findViewById(R.id.list_view_products);
        searchBar = findViewById(R.id.search_bar);

        databaseHelper = new DatabaseHelper(this);

        displayProducts("");

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayProducts("");
    }

    private void displayProducts(String searchQuery) {
        Cursor cursor = databaseHelper.searchProducts(searchQuery);
        int layoutResourceId = R.layout.activity_product_display;
        productAdapter=new ProductAdapter(this,cursor,layoutResourceId);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(String productName, double productPrice, int productQuantity, byte[] productImage) {
                handleAddToCart(productName, productPrice, productQuantity, productImage);
            }
            @Override
            public void onBuyNowClick(String productName, double productPrice, int productQuantity, byte[] productImage) {
                handleBuyNow(productName, productPrice, productQuantity, productImage);
            }
        });
        listViewProducts.setAdapter(productAdapter);
    }
    private void handleAddToCart(String productName, double productPrice, int productQuantity, byte[] productImage) {
        Log.d("ProductDisplay", "Adding product to cart: " + productName);
        Intent intent = new Intent(ProductDisplay.this, CartActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productPrice", productPrice);
        intent.putExtra("productQuantity", productQuantity);
        intent.putExtra("productImage", productImage);
        startActivity(intent);
        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
    }
    private void handleBuyNow(String productName, double productPrice, int productQuantity, byte[] productImage) {
        Log.d("ProductDisplay", "Buying product: " + productName);
        Intent intent = new Intent(ProductDisplay.this, CheckoutActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("productPrice", productPrice);
        intent.putExtra("productQuantity", productQuantity);
        intent.putExtra("productImage", productImage);
        startActivity(intent);
        Toast.makeText(this, "Proceeding to checkout", Toast.LENGTH_SHORT).show();


        }


}
