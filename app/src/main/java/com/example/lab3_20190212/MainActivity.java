package com.example.lab3_20190212;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button boton = findViewById(R.id.botoncito);
        boton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AppActivity.class);
            startActivity(intent);
        });
    }
}