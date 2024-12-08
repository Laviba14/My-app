package com.example.mymealmateproject;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private byte[] imageByteArray;
    private boolean selected;

    public CartItem(int id, String name, double price, int quantity, byte[] imageByteArray, boolean selected) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageByteArray = imageByteArray;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Add this method to set the quantity of the cart item
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
