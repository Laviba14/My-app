package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private EditText addressEditText;
    private RadioGroup paymentOptionsRadioGroup;
    private RecyclerView cartItemsRecyclerView;
    private TextView totalPriceTextView;
    private Button placeOrderButton;
    private ArrayList<CartItem> cartItems;
    private double totalPrice;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        initializeViews();

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get cart items and total price from previous activity
        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cart_items");
        totalPrice = getIntent().getDoubleExtra("total_price", 0.0);

        // Debugging: Check if cart items are received
        if (cartItems == null || cartItems.isEmpty()) {
            Log.e("CheckoutActivity", "No cart items received!");
            Toast.makeText(this, "No items in the cart.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            for (CartItem item : cartItems) {
                Log.d("CheckoutActivity", "Received Item: " + item.getName() + ", Price: " + item.getPrice());
            }
            Log.d("CheckoutActivity", "Total Price: " + totalPrice);

            // Load cart items into the RecyclerView
            loadCartItems();
            updateTotalPrice();
        }

        // Set up button listeners
        setupListeners();
    }

    private void initializeViews() {
        addressEditText = findViewById(R.id.checkout_address);
        paymentOptionsRadioGroup = findViewById(R.id.payment_options);
        cartItemsRecyclerView = findViewById(R.id.cart_items_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price);
        placeOrderButton = findViewById(R.id.place_order_button);
    }

    private void loadCartItems() {
        // Set up RecyclerView for displaying cart items
        CartItemsAdapter adapter = new CartItemsAdapter(this, cartItems);
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemsRecyclerView.setAdapter(adapter);
    }

    private void updateTotalPrice() {
        // Display the total price
        totalPriceTextView.setText("$" + String.format("%.2f", totalPrice));
    }

    private void setupListeners() {
        placeOrderButton.setOnClickListener(v -> {
            // Get the selected payment method
            int selectedPaymentOptionId = paymentOptionsRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedPaymentOption = findViewById(selectedPaymentOptionId);
            String selectedPaymentMethod = selectedPaymentOption != null ? selectedPaymentOption.getText().toString() : "";

            // Get the address entered by the user
            String address = addressEditText.getText().toString().trim();

            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter a shipping address.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show();
                return;
            }

            placeOrder(address, selectedPaymentMethod);
        });
    }

    private void placeOrder(String address, String paymentMethod) {
        databaseHelper.placeOrder(cartItems, address, paymentMethod);
        Toast.makeText(this, "Order placed successfully!\nShipping to: " + address + "\nPayment Method: " + paymentMethod, Toast.LENGTH_LONG).show();

        // Redirect to a confirmation or home activity
        Intent intent = new Intent(CheckoutActivity.this, ConfirmationActivity.class);
        startActivity(intent);
        finish();  // Finish the current activity to remove it from the stack
    }
}
