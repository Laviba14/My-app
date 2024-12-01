package com.example.mymealmateproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test_DB";
    public static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_REGISTER = "register";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_ORDERS = "orders";

    // Column names for register table
    public static final String COL_ID = "_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_MOBILE = "mobile";

    // Column names for products table
    public static final String COL_PRODUCT_NAME = "productName";
    public static final String COL_PRODUCT_PRICE = "productPrice";
    public static final String COL_PRODUCT_QUANTITY = "productQuantity";
    public static final String COL_PRODUCT_IMAGE_URI = "productImageUri";

    // Column names for cart table
    public static final String COL_CART_NAME = "cartName";
    public static final String COL_CART_QUANTITY = "quantity";
    public static final String COL_CART_PRICE = "cartPrice";
    public static final String COL_CART_IMAGE_URI = "cartImageUri";
    public static final String COL_CART_SELECTED = "selected";

    // Column names for orders table
    public static final String COL_ORDER_NAME = "orderName";
    public static final String COL_ORDER_PRICE = "orderPrice";
    public static final String COL_ORDER_QUANTITY = "orderQuantity";
    public static final String COL_ORDER_IMAGE_URI = "orderImageUri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for register
        db.execSQL("CREATE TABLE " + TABLE_REGISTER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_MOBILE + " TEXT)");

        // Create table for products
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_PRICE + " REAL, " +
                COL_PRODUCT_QUANTITY + " INTEGER, " +
                COL_PRODUCT_IMAGE_URI + " BLOB)");

        // Create table for cart
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_NAME + " TEXT," +
                COL_CART_QUANTITY + " INTEGER," +
                COL_CART_PRICE + " REAL, " +
                COL_CART_IMAGE_URI + " BLOB, " +
                COL_CART_SELECTED + " INTEGER)");

        // Create table for orders
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_NAME + " TEXT, " +
                COL_ORDER_PRICE + " REAL, " +
                COL_ORDER_QUANTITY + " INTEGER, " +
                COL_ORDER_IMAGE_URI + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);  // Recreate tables after upgrade
    }

    // Insert new user into the 'register' table
    public boolean insertUser(String username, String email, String password, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_MOBILE, mobile);
        long result = db.insert(TABLE_REGISTER, null, contentValues);
        db.close();
        return result != -1;
    }

    // Check user credentials for login
    public boolean checkUserByUsername(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;  // Valid user
        }
        return false;  // Invalid user
    }


    // Insert product into the 'products' table
    public void insertProduct(String name, double price, int quantity, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Get products by name
    public Cursor getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " = ?", new String[]{productName});
    }

    // Update product details
    public void updateProduct(int productId, String productName, double price, int quantity, byte[] productImageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, productImageByteArray);
        db.update(TABLE_PRODUCTS, values, COL_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Delete product
    public void deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_PRODUCT_NAME + " = ?", new String[]{productName});
        db.close();
    }

    // Search products by name
    public Cursor searchProducts(String searchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " LIKE ?", new String[]{"%" + searchQuery + "%"});
    }

    // Add item to the cart
    public void addCartItem(String name, double price, int quantity, byte[] imageByteArray, boolean selected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_NAME, name);
        values.put(COL_CART_PRICE, price);
        values.put(COL_CART_QUANTITY, quantity);
        values.put(COL_CART_IMAGE_URI, imageByteArray);
        values.put(COL_CART_SELECTED, selected ? 1 : 0); // 1 for true, 0 for false
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    // Get all items from the cart
    public List<CartItem> getCartItemsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CartItem> cartItemList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndexOrThrow(COL_CART_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CART_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_QUANTITY));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_CART_IMAGE_URI));
                boolean selected = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_SELECTED)) == 1;
                CartItem cartItem = new CartItem(name, price, quantity, image, selected);
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItemList;
    }

    // Update item selection in the cart
    public void updateCartItemSelection(String name, boolean selected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_SELECTED, selected ? 1 : 0);
        db.update(TABLE_CART, values, COL_CART_NAME + " = ?", new String[]{name});
        db.close();
    }

    // Remove item from the cart
    public void removeCartItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_CART_NAME + " = ?", new String[]{name});
        db.close();
    }
}
