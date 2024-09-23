package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText Username = (EditText) findViewById(R.id.et_username);
        EditText Password = (EditText) findViewById(R.id.et_password);
        Button LoginBtn = (Button) findViewById(R.id.btn_login);
        Button RegisterBtn = (Button) findViewById(R.id.btn_register);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Register Button clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        LoginBtn.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String password = Password.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            } else {

                    if(username.equals("admin") && password.equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    }
                DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                boolean result = dbHelper.checkUserByUsername(username, password);
                if (result) {
                    Toast.makeText(LoginActivity.this, "Welcome valid user!!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,ProductDisplay.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "valid Username and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}