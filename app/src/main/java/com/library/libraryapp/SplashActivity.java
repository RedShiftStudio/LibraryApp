package com.library.libraryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("SplashActivity", "SplashActivity started");

        mAuth = FirebaseAuth.getInstance();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Intent intent;

            if (currentUser != null) {
                Log.d("SplashActivity", "Авторизация успешна: " + currentUser.getUid());
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                Log.d("SplashActivity", "Требуется вход");
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 2000);
    }
}
