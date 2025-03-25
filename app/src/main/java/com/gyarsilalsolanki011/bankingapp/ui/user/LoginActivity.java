package com.gyarsilalsolanki011.bankingapp.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gyarsilalsolanki011.bankingapp.core.api.ApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityLoginBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = Objects.requireNonNull(binding.emailInput.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter credentials!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<LoginResponse> call = apiService.loginUser(email, password);

        // Show Progress Bar and Disable Login Button
        binding.loginProgressIndicator.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                // Hide Progress Bar and Enable Login Button
                binding.loginProgressIndicator.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Hide Progress Bar and Enable Login Button
                binding.loginProgressIndicator.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);

                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("JWT_TOKEN", token);
        editor.apply();
    }
}