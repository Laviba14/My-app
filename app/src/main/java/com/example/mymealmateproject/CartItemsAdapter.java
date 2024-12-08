package com.example.mymealmateproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public CartItemsAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>(); // Null check for cartItems
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        if (cartItems != null && !cartItems.isEmpty()) {
            CartItem cartItem = cartItems.get(position);
            holder.itemNameTextView.setText(cartItem.getName());
            holder.itemPriceTextView.setText(String.format("$%.2f", cartItem.getPrice()));
            holder.itemQuantityTextView.setText("Qty: " + cartItem.getQuantity());// Cleaner price formatting
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0; // Ensure that the list is not null
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemPriceTextView;
        TextView itemQuantityTextView;

        public CartItemViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.checkout_item_name);
            itemPriceTextView = itemView.findViewById(R.id.checkout_item_price);
            itemQuantityTextView = itemView.findViewById(R.id.checkout_item_quantity);

        }
    }
}
