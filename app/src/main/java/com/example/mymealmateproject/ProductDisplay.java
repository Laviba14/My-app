package com.example.mymealmateproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDisplay extends Activity {

    private static final String TAG = "ProductDisplay";
    private EditText searchBar;
    private GridView productGridView;
    private ProductAdapter productAdapter;
    private TextView noResultsText;
    private ImageView cartIcon;
    private TextView cartItemCount;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        // Initialize views
        searchBar = findViewById(R.id.search_bar);
        productGridView = findViewById(R.id.product_grid_view);
        noResultsText = findViewById(R.id.no_results_text);
        cartIcon = findViewById(R.id.cart_icon);
        cartItemCount = findViewById(R.id.cart_item_count);

        dbHelper = new DatabaseHelper(this);

        // Set up ProductAdapter
        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null) {
            productAdapter = new ProductAdapter(this, cursor, 0);
            productGridView.setAdapter(productAdapter);
        } else {
            Log.e(TAG, "Cursor is null when loading all products.");
        }

        // Hide "No Results" initially
        noResultsText.setVisibility(View.GONE);

        // Live search functionality
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchTerm = s.toString().trim();
                Cursor filteredCursor = searchTerm.isEmpty()
                        ? dbHelper.getAllProducts()
                        : dbHelper.searchProducts(searchTerm);
                if (filteredCursor != null) {
                    if (filteredCursor.getCount() == 0) {
                        noResultsText.setVisibility(View.VISIBLE);
                    } else {
                        noResultsText.setVisibility(View.GONE);
                    }
                    productAdapter.changeCursor(filteredCursor);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Cart icon functionality
        cartIcon.setOnClickListener(v -> {
            // Navigate to CartActivity
            startActivity(new android.content.Intent(this, CartActivity.class));
        });

        // Update cart badge count
        updateCartBadge();
    }

    private void updateCartBadge() {
        int cartCount = dbHelper.getCartItemCount();
        cartItemCount.setText(String.valueOf(cartCount));
        cartItemCount.setVisibility(cartCount > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cart badge count
        updateCartBadge();
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
