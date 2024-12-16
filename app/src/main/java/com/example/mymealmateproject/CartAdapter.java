package com.example.mymealmateproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper databaseHelper;
    private CartActivity cartActivity;

    public CartAdapter(Context context, List<CartItem> cartItems, DatabaseHelper databaseHelper, CartActivity cartActivity) {
        this.context = context;
        this.cartItems = cartItems;
        this.databaseHelper = databaseHelper;
        this.cartActivity = cartActivity;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        CartItem cartItem = cartItems.get(position);

        // Set up views
        CheckBox checkBox = view.findViewById(R.id.cart_item_checkbox);
        ImageView imageView = view.findViewById(R.id.cart_item_image);
        TextView nameTextView = view.findViewById(R.id.cart_item_name);
        TextView priceTextView = view.findViewById(R.id.cart_item_price);
        TextView quantityTextView = view.findViewById(R.id.cart_item_quantity);
        ImageButton removeButton = view.findViewById(R.id.cart_item_remove_button); // ImageButton for remove
        ImageButton incrementButton = view.findViewById(R.id.cart_item_increment_button);  // ImageButton for increment
        ImageButton decrementButton = view.findViewById(R.id.cart_item_decrement_button);  // ImageButton for decrement

        // Set item details
        checkBox.setChecked(cartItem.isSelected());
        nameTextView.setText(cartItem.getName());
        priceTextView.setText("$" + String.format("%.2f", cartItem.getPrice()));
        quantityTextView.setText("Quantity: " + cartItem.getQuantity());

        // Set image (convert byte array to Bitmap)
        Bitmap bitmap = BitmapFactory.decodeByteArray(cartItem.getImageByteArray(), 0, cartItem.getImageByteArray().length);
        imageView.setImageBitmap(bitmap);

        // Set listeners for item selection checkbox
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            cartActivity.onCartItemSelectionChanged(cartItem.getId(), isChecked);
        });

        // Set listeners for increment and decrement buttons
        incrementButton.setOnClickListener(v -> updateItemQuantity(cartItem, 1));  // Increment quantity by 1
        decrementButton.setOnClickListener(v -> updateItemQuantity(cartItem, -1));  // Decrement quantity by 1

        // Set listener for remove button
        removeButton.setOnClickListener(v -> cartActivity.removeCartItem(cartItem.getName()));

        return view;
    }

    // Get selected items
    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    // Calculate total price of selected items
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    // Method to update quantity of an item in the cart
    private void updateItemQuantity(CartItem cartItem, int change) {
        int newQuantity = cartItem.getQuantity() + change;

        // Prevent negative quantities
        if (newQuantity >= 1) {
            cartItem.setQuantity(newQuantity);
            databaseHelper.updateProductQuantity(cartItem.getId(), newQuantity);  // Update quantity in database
            notifyDataSetChanged();  // Notify the adapter that data has changed (to update the view)
            cartActivity.updateTotalPrice();  // Update the total price in CartActivity
        } else {
            Toast.makeText(context, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show();  // Show error for invalid quantity
        }
    }
}
