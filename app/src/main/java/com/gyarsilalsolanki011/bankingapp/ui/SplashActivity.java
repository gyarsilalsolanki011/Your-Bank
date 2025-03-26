package com.gyarsilalsolanki011.bankingapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.TokenManager;
import com.gyarsilalsolanki011.bankingapp.ui.user.LoginActivity;
import com.gyarsilalsolanki011.bankingapp.ui.user.UserDashboardActivity;

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

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        if (TokenManager.isUserLoggedIn(this)){
            // User is logged in, go to Dashboard
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        } else {
            // User is not logged in, go to Login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}