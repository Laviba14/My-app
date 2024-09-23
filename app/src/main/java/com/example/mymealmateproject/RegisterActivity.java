package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText Username = (EditText) findViewById(R.id.et_register_username);
        EditText Email = (EditText) findViewById(R.id.et_register_email);
        EditText Password = (EditText) findViewById(R.id.et_register_password);
        EditText ConfirmPassword = (EditText) findViewById(R.id.et_register_confirm_password);
        EditText Mobile = (EditText) findViewById(R.id.et_register_mobile);
        Button LoginBtn = (Button) findViewById(R.id.btn_signup_login);
        Button RegisterBtn = (Button) findViewById(R.id.btn_signup_register);
        RegisterBtn.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            String confirmPassword = ConfirmPassword.getText().toString();
            String mobile = Mobile.getText().toString();
            if (password.equals(confirmPassword) && !password.isEmpty() && !username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "well done! Let me insert your info in DB!", Toast.LENGTH_SHORT).show();
                DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
                boolean isInserted = dbHelper.insertUser(username, email, password, mobile);
                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(RegisterActivity.this, "Passwords do not match or empty password or empty username!", Toast.LENGTH_SHORT).show();
            }


        });

        LoginBtn.setOnClickListener(v-> {
            Toast.makeText(RegisterActivity.this, "Login Button clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });


    }
}