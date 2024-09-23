package com.example.mymealmateproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewProductActivity extends AppCompatActivity {
    private ListView listViewProducts;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product2);
        listViewProducts = findViewById(R.id.list_view_products);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);
        databaseHelper = new DatabaseHelper(this);

        displayProducts();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleUpdate();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleDelete();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        displayProducts();
    }

    private void displayProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        ProductAdapter adapter = new ProductAdapter(this, cursor, 0);
        listViewProducts.setAdapter(adapter);
    }

    private void handleUpdate() {

        Intent intent = new Intent(ViewProductActivity.this, UpdateProductActivity.class);
        startActivity(intent);
    }

    private void handleDelete() {
        Intent intent = new Intent(ViewProductActivity.this, DeleteProductActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Delete button clicked", Toast.LENGTH_SHORT).show();
       }
}




