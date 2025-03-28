package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.utils.TokenManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        //Set animation for SplashActivity

    }

    private void checkLoginStatus() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (TokenManager.isUserLoggedIn(this)) {
                startActivity(new Intent(SplashActivity.this, UserDashboardActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 200);
    }
}