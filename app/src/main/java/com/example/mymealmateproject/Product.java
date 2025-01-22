package com.example.mymealmateproject;

public class Product {
    private int productId;
    private String productName;
    private String productPrice;
    private byte[] productImage;

    // Constructor
    public Product(int productId, String productName, String productPrice, byte[] productImage) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductPrice() { return productPrice; }
    public byte[] getProductImage() { return productImage; }

    // Setters (if needed)
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }
    public void setProductImage(byte[] productImage) { this.productImage = productImage; }
}
