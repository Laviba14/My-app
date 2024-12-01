package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
            updateTotalPrice();
        }

        private void initializeViews() {
            cartItemsListView = findViewById(R.id.cart_items_listview);
            totalPriceTextView = findViewById(R.id.cart_total_price);
            checkoutButton = findViewById(R.id.checkout_button);
        }

        private void loadCartData() {
            List<CartItem> cartItems = databaseHelper.getCartItemsList();
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "No items in the cart.", Toast.LENGTH_SHORT).show();
            } else {
                cartAdapter=new CartAdapter(this,cartItems,databaseHelper);
                cartItemsListView.setAdapter(cartAdapter);
            }
        }

        private void setupListeners() {
            checkoutButton.setOnClickListener(v -> {
                if (cartAdapter.getSelectedItems().isEmpty()) {
                    Toast.makeText(this, "Please select at least one item.", Toast.LENGTH_SHORT).show();
                } else {
                    proceedToCheckout();
                }
            });
        }

        private void updateTotalPrice() {
            double totalPrice = cartAdapter.getTotalPrice();
            totalPriceTextView.setText("$" + String.format("%.2f", totalPrice));
        }

    private void proceedToCheckout() {
        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        ArrayList<String> selectedItems = new ArrayList<>(cartAdapter.getSelectedItems());

        intent.putStringArrayListExtra("selected_items", selectedItems);

        startActivity(intent);
    }


}







