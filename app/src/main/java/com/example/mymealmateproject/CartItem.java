package com.example.mymealmateproject;

public class CartItem {
    private String name;
    private double price;
    private int quantity;
    private byte[] image;  // Use byte array for image
    private boolean selected;

    // Constructor
    public CartItem(String name, double price, int quantity, byte[] image, boolean selected) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.selected = selected;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImage() {
        return image;  // Get the image as byte array
    }

    public boolean isSelected() {
        return selected;
    }

    // Setter for 'selected'
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
