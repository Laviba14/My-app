package com.example.mymealmateproject;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CartAdapter extends CursorAdapter {
    public CartAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.cart_item_name);
        TextView priceTextView = view.findViewById(R.id.cart_item_price);
        TextView quantityTextView = view.findViewById(R.id.cart_item_quantity);
        ImageView imageView = view.findViewById(R.id.cart_item_image);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_QUANTITY));
        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_IMAGE_URI));

        nameTextView.setText(name);
        priceTextView.setText(String.valueOf(price));
        quantityTextView.setText(String.valueOf(quantity));

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        imageView.setImageBitmap(bitmap);

    }
}
