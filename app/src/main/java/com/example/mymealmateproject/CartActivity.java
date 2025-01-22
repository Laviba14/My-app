package com.example.mymealmateproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
        List<CartItem> cartItems = databaseHelper.getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No items in the cart.", Toast.LENGTH_SHORT).show();
        } else {
            cartAdapter = new CartAdapter(this, cartItems, databaseHelper, this);
            cartItemsListView.setAdapter(cartAdapter);
            updateTotalPrice();
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

            // Remove items from the database after checkout
            for (CartItem item : selectedItems) {
                databaseHelper.removeCartItem(item.getName());
            }

            // Refresh the cart after checkout
            loadCartData();

            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("cart_items", selectedItems);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
        });

        // Long click listener to delete an item from the cart
        cartItemsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            CartItem cartItem = (CartItem) cartAdapter.getItem(position);
            Log.d("CartActivity", "Item long clicked: " + cartItem.getName()); // Debugging
            showDeleteConfirmationDialog(cartItem);
            return true;  // Ensure the event is handled
        });

        // Regular click listener to update quantity and check selections
        cartItemsListView.setOnItemClickListener((parent, view, position, id) -> {
            CartItem cartItem = (CartItem) cartAdapter.getItem(position);
            // Handle cart item click (select/unselect item for checkout)
            boolean newSelectionState = !cartItem.isSelected();
            cartItem.setSelected(newSelectionState);
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();  // Update total price based on selection
        });
    }

    private void showDeleteConfirmationDialog(CartItem cartItem) {
        Log.d("CartActivity", "Preparing to show dialog for: " + cartItem.getName());
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete " + cartItem.getName() + " from the cart?")
                .setPositiveButton("Yes", (dialog, which) -> removeCartItem(cartItem)) // Delete the item
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()) // Cancel deletion
                .show();
        Log.d("CartActivity", "Dialog displayed.");
    }

    private void removeCartItem(CartItem cartItem) {
        // Remove the item from the database
        databaseHelper.removeCartItem(cartItem.getName());
        // Remove the item from the adapter and notify changes
        cartAdapter.remove(cartItem);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
        Toast.makeText(this, cartItem.getName() + " removed from the cart.", Toast.LENGTH_SHORT).show();
    }

    public void updateTotalPrice() {
        double totalPrice = cartAdapter.getTotalPrice();
        totalPriceTextView.setText("$" + String.format("%.2f", totalPrice));
    }

    public void onCartItemSelectionChanged(int cartItemId, boolean isSelected) {
        databaseHelper.updateProductSelection(cartItemId, isSelected);
        updateTotalPrice();
    }

    public void updateCartItemQuantity(int cartItemId, int quantity) {
        databaseHelper.updateProductQuantity(cartItemId, quantity);
        // Update adapter and refresh the total price
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }
}
