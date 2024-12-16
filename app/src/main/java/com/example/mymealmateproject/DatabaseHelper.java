package com.example.mymealmateproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
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
    public static final String COL_ORDER_ADDRESS = "orderAddress";
    public static final String COL_ORDER_PAYMENT_METHOD = "orderPaymentMethod";

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
                COL_ORDER_IMAGE_URI + " BLOB, " +
                COL_ORDER_ADDRESS + " TEXT, " +
                COL_ORDER_PAYMENT_METHOD + " TEXT)");
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

    // Insert a user
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

    // Check if user exists by username
    public boolean checkUserByEmail(String email ) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_REGISTER + " WHERE " + COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Insert a product
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
    public Cursor getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " = ?", new String[]{productName});

    }

    // Update product details
    public void updateProduct(int productId, String name, double price, int quantity, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_QUANTITY, quantity);
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray);
        db.update(TABLE_PRODUCTS, values, COL_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Delete a product
    public void deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_PRODUCT_NAME + " = ?", new String[]{String.valueOf(productName)});
        db.close();
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public Cursor searchProducts(String searchTerm) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + searchTerm + "%"});
        return cursor;
    }

    public boolean addToCart(String name, double price, int quantity, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_NAME, name);
        values.put(COL_CART_PRICE, price);
        values.put(COL_CART_QUANTITY, quantity);
        values.put(COL_CART_IMAGE_URI, imageBytes);
        long result = db.insert(TABLE_CART, null, values);
        return result != -1; // Return true if insert was successful
    }


    // Get all cart items
    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CART;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    // Ensure the column exists and retrieve data safely
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CART_NAME));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CART_PRICE));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_QUANTITY));
                    byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_CART_IMAGE_URI));
                    boolean selected = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_SELECTED)) > 0;

                    CartItem cartItem = new CartItem(id, name, price, quantity, imageByteArray, selected);
                    cartItems.add(cartItem);
                } catch (Exception e) {
                    // Catch any exception during data retrieval and log it
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cartItems;
    }
    // Update the quantity of a product in the cart
    public void updateProductQuantity(int cartItemId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_QUANTITY, newQuantity);  // Update quantity
        db.update(TABLE_CART, values, COL_ID + " = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }



    // Update product selection in the cart
    public void updateProductSelection(int cartItemId, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_SELECTED, isSelected ? 1 : 0);
        db.update(TABLE_CART, values, COL_ID + " = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }
    public int getCartItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COL_CART_QUANTITY + ") AS totalQuantity FROM " + TABLE_CART;
        Cursor cursor = db.rawQuery(query, null);
        int totalCount = 0;
        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuantity"));
        }
        cursor.close();
        db.close();
        return totalCount;
    }
    public boolean isProductInCart(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_CART + " WHERE " + COL_CART_NAME + " = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{name});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return false;
    }


    public boolean removeFromCart(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART, COL_CART_NAME + " = ?", new String[]{name});
        db.close();
        return rowsAffected > 0;
    }




    // Place an order
    public void placeOrder(ArrayList<CartItem> cartItems, String address, String paymentMethod) {
        // Assuming you have an orders table to save the order details.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put("address", address);
        orderValues.put("payment_method", paymentMethod);
        orderValues.put("total_price", calculateTotalPrice(cartItems));  // Add total price of the cart

        // Insert the order into the database (adjust to your schema)
        long orderId = db.insert("orders", null, orderValues);

        // Insert cart items into the order_items table
        for (CartItem item : cartItems) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("order_id", orderId);
            itemValues.put("product_name", item.getName());
            itemValues.put("quantity", item.getQuantity());
            itemValues.put("price", item.getPrice());
            db.insert("order_items", null, itemValues);
        }
    }

    private double calculateTotalPrice(ArrayList<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }


    // Delete item from cart
    public void removeCartItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_CART_NAME + " = ?", new String[]{name});
        db.close();
    }
}
