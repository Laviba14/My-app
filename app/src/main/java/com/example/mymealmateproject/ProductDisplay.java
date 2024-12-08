package com.example.mymealmateproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDisplay extends Activity {

    private static final String TAG = "ProductDisplay";
    private EditText searchBar;
    private Button searchButton;
    private GridView productGridView;
    private ProductAdapter productAdapter;
    private TextView noResultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        // Initialize the views
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        productGridView = findViewById(R.id.product_grid_view);
        noResultsText = findViewById(R.id.no_results_text);

        // Set up the ProductAdapter
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null) {
            productAdapter = new ProductAdapter(this, cursor, 0);
            productGridView.setAdapter(productAdapter);
        } else {
            Log.e(TAG, "Cursor is null when loading all products.");
        }

        // Initially hide the "No Results" message
        noResultsText.setVisibility(View.GONE);

        // Implement search functionality when search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchBar.getText().toString().trim();
                if (!searchTerm.isEmpty()) {
                    Cursor filteredCursor = dbHelper.searchProducts(searchTerm);
                    if (filteredCursor != null && filteredCursor.getCount() == 0) {
                        noResultsText.setVisibility(View.VISIBLE);
                        Toast.makeText(ProductDisplay.this, "No results found", Toast.LENGTH_SHORT).show();
                    } else {
                        noResultsText.setVisibility(View.GONE);
                        if (filteredCursor != null) {
                            productAdapter.changeCursor(filteredCursor);
                        }
                    }
                } else {
                    Toast.makeText(ProductDisplay.this, "Enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle GridView item clicks to show product details
        productGridView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor clickedCursor = (Cursor) productAdapter.getItem(position);
            if (clickedCursor != null) {
                String productName = clickedCursor.getString(
                        clickedCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
                double productPrice = clickedCursor.getDouble(
                        clickedCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_PRICE));
                showProductDetailsDialog(productName, productPrice, dbHelper);
            }
        });

        // Live search functionality
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String searchTerm = charSequence.toString();
                if (searchTerm.isEmpty()) {
                    Cursor allProductsCursor = dbHelper.getAllProducts();
                    if (allProductsCursor != null) {
                        productAdapter.changeCursor(allProductsCursor);
                    }
                } else {
                    Cursor filteredCursor = dbHelper.searchProducts(searchTerm);
                    if (filteredCursor != null && filteredCursor.getCount() == 0) {
                        noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        noResultsText.setVisibility(View.GONE);
                        if (filteredCursor != null) {
                            productAdapter.changeCursor(filteredCursor);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
    }

    private void showProductDetailsDialog(String productName, double productPrice, DatabaseHelper dbHelper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(productName)
                .setMessage("Price: $" + productPrice)
                .setPositiveButton("Add to Cart", (dialog, which) -> {
                    int quantity = 1; // You can prompt the user to choose quantity if needed
                    byte[] imageBytes = null; // If you want to store an image, you can replace this

                    // Pass the product name, price, quantity, and imageBytes to add to cart
                    boolean isAdded = dbHelper.addToCart(productName, productPrice, quantity, imageBytes);
                    if (isAdded) {
                        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productAdapter != null) {
            Cursor cursor = productAdapter.getCursor();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
