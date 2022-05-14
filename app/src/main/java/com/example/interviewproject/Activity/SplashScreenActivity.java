package com.example.interviewproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.interviewproject.MainActivity;
import com.example.interviewproject.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*splash = findViewById(R.id.splash_image);*/


        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
        String st = sharedPreferences.getString("status", "");
        /* String type = sharedPreferences.getString("type", "");*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("login".equals(st)) {
                    startActivity(new Intent(SplashScreenActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
                finish();
            }
        }, 2000);

    }
}