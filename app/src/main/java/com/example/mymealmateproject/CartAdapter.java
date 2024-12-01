package com.example.mymealmateproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends ArrayAdapter<CartItem> {

    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper databaseHelper;
    private CartUpdateListener cartUpdateListener;

    public interface CartUpdateListener {
        void updateTotalPrice();
    }

    // Constructor accepting the context and cartItems
    public CartAdapter(Context context, List<CartItem> cartItems, DatabaseHelper databaseHelper) {
        super(context, 0, cartItems);
        this.context = context;
        this.cartItems = cartItems;
        this.databaseHelper = databaseHelper;

        // Check if context implements CartUpdateListener
        if (context instanceof CartUpdateListener) {
            this.cartUpdateListener = (CartUpdateListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement CartUpdateListener");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        CheckBox checkBox = convertView.findViewById(R.id.cart_item_checkbox);
        ImageView imageView = convertView.findViewById(R.id.cart_item_image);
        TextView nameTextView = convertView.findViewById(R.id.cart_item_name);
        TextView priceTextView = convertView.findViewById(R.id.cart_item_price);
        TextView quantityTextView = convertView.findViewById(R.id.cart_item_quantity);
        Button removeButton = convertView.findViewById(R.id.cart_item_remove_button);

        // Set the data for each cart item
        // Convert byte[] image to Bitmap and display it in the ImageView
        if (cartItem.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(cartItem.getImage(), 0, cartItem.getImage().length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.background); // Placeholder for no image
        }
        nameTextView.setText(cartItem.getName());
        priceTextView.setText("$" + String.format("%.2f", cartItem.getPrice()));
        quantityTextView.setText("Quantity: " + cartItem.getQuantity());

        // Set CheckBox listener to update selection
        checkBox.setChecked(cartItem.isSelected());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            // Update the cart item in the database
            databaseHelper.updateCartItemSelection(cartItem.getName(), isChecked);
        });

        // Set remove button click listener
        removeButton.setOnClickListener(v -> {
            // Remove item from the database and the list
            databaseHelper.removeCartItem(cartItem.getName());
            cartItems.remove(position);
            notifyDataSetChanged();

            // Update total price
            cartUpdateListener.updateTotalPrice();
        });

        return convertView;
    }

    // Method to get selected items
    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selectedItems.add(item.getName());
            }
        }
        return selectedItems;
    }

    // Method to calculate the total price of selected items
    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
        }
        return totalPrice;
    }
}
