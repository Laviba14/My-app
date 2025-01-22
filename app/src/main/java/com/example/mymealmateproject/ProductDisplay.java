package com.example.mymealmateproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ProductDisplay extends AppCompatActivity {

    private static final String TAG = "ProductDisplay";

    private EditText searchBar;
    private GridView productGridView;
    private ProductAdapter productAdapter;
    private TextView noResultsText;
    private ImageView cartIcon;
    private TextView cartItemCount;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

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
        drawerLayout = findViewById(R.id.drawer_layout);

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        // Cart icon functionality
        cartIcon.setOnClickListener(v -> {
            // Navigate to CartActivity
            startActivity(new Intent(this, CartActivity.class));
        });

        // Update cart badge count
        updateCartBadge();

        // Drawer toggle setup
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(ProductDisplay.this, getString(R.string.drawer_open), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(ProductDisplay.this, getString(R.string.drawer_close), Toast.LENGTH_SHORT).show();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Menu icon functionality
        findViewById(R.id.menu_icon).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Navigation menu setup
        NavigationView navigationView = findViewById(R.id.navigation_menu);
        navigationView.inflateMenu(R.menu.menu_navigation);  // Inflate the menu if not done in XML
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Use if-else instead of switch-case
                if (item.getItemId() == R.id.menu_home) {
                    startActivity(new Intent(ProductDisplay.this, ProductDisplay.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_profile) {
                    startActivity(new Intent(ProductDisplay.this, ProfileActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_settings) {
                    startActivity(new Intent(ProductDisplay.this, SettingsActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.menu_logout) {
                    // Clear "remember me" and user credentials from SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("remember_me");  // Clear remember me flag
                    editor.remove("user_email");   // Clear stored email
                    editor.remove("user_password"); // Clear stored password
                    editor.putBoolean("isLoggedIn", false);  // Set isLoggedIn to false
                    editor.apply();  // Apply changes to SharedPreferences

                    // Optionally, you can also show a confirmation message to the user
                    Toast.makeText(ProductDisplay.this, "You have been logged out", Toast.LENGTH_SHORT).show();

                    // Navigate to the login page and finish current activity
                    startActivity(new Intent(ProductDisplay.this, LoginActivity.class));
                    finish(); // Finish the current activity so the user can't go back to it
                    return true;
                }

                return false;
            }
        });

    }

    // Update the cart badge count
    public void updateCartBadge() {
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

