package com.example.mymealmateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView Welcome = (TextView)  findViewById(R.id.et_welcome);
        TextView Password =(TextView) findViewById(R.id.et_started);
        Button LoginBtn = (Button) findViewById(R.id.btn_login);
        Button RegisterBtn = (Button) findViewById(R.id.btn_register);
        LoginBtn.setOnClickListener(v-> {
            Toast.makeText(MainActivity.this, "Login Button clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Register Button clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}