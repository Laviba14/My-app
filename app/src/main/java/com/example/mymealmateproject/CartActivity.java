package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView cartItemsListView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private DatabaseHelper databaseHelper;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        databaseHelper = new DatabaseHelper(this);
        initializeViews();
        loadCartData();
        setupListeners();
    }

    private void initializeViews() {
        cartItemsListView = findViewById(R.id.cart_items_listview);
        totalPriceTextView = findViewById(R.id.cart_total_price);
        checkoutButton = findViewById(R.id.checkout_button);
    }

    private void loadCartData() {
        List<CartItem> cartItems = databaseHelper.getCartItems(); // Get cart items from database
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No items in the cart.", Toast.LENGTH_SHORT).show();
        } else {
            cartAdapter = new CartAdapter(this, cartItems, databaseHelper, this);
            cartItemsListView.setAdapter(cartAdapter);
            updateTotalPrice(); // Update total price when cart data is loaded
        }
    }

    private void setupListeners() {
        checkoutButton.setOnClickListener(v -> {
            ArrayList<CartItem> selectedItems = new ArrayList<>(cartAdapter.getSelectedItems());
            double totalPrice = cartAdapter.getTotalPrice();

            if (selectedItems.isEmpty()) {
                Toast.makeText(CartActivity.this, "No items selected to checkout!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Log the data being passed
            for (CartItem item : selectedItems) {
                Log.d("CartActivity", "Item: " + item.getName() + ", Price: " + item.getPrice());
            }
            Log.d("CartActivity", "Total Price: " + totalPrice);

            // Create an intent and pass the selected items and total price
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("cart_items", selectedItems); // Pass ArrayList<CartItem> using putExtra
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
        });
    }

    public void updateTotalPrice() {
        double totalPrice = cartAdapter.getTotalPrice(); // Get the total price from the adapter
        totalPriceTextView.setText("$" + String.format("%.2f", totalPrice));
    }

    // This method is called when the checkbox selection state changes
    public void onCartItemSelectionChanged(int cartItemId, boolean isSelected) {
        databaseHelper.updateProductSelection(cartItemId, isSelected);
        updateTotalPrice();
    }

    // Method to remove an item from the cart
    public void removeCartItem(String itemName) {
        databaseHelper.removeCartItem(itemName);
        loadCartData();
        Toast.makeText(this, itemName + " removed from the cart.", Toast.LENGTH_SHORT).show();
    }

    // Method to update the quantity of an item in the cart
    public void updateCartItemQuantity(int cartItemId, int quantity) {
        databaseHelper.updateProductQuantity(cartItemId, quantity);
        loadCartData();
    }
}
