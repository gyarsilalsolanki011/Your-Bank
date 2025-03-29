package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gyarsilalsolanki011.bankingapp.core.api.repository.AuthApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.UserApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.OnlineBankingStatus;
import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.UserResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
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

        binding.createAccountTextButton.setOnClickListener(v -> registerUser());
    }

    //Register User Navigation
    private void registerUser() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    // Login User Method
    private void loginUser() {
        String email = Objects.requireNonNull(binding.emailInput.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
        }else{

            AuthApiService authApiService = RetrofitClient.getInstance().getAuthApiService();
            Call<LoginResponse> call = authApiService.loginUser(email, password);

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
                        saveUserData(email, token);
                        startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable throwable) {
                    binding.loginProgressIndicator.setVisibility(View.GONE);
                    binding.loginButton.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
                }
            });
        }
    }

    private void saveUserData(String email, String token) {
        UserSharedPreferencesManager sharedPref = UserSharedPreferencesManager.getInstance(this);

        UserApiService userApiService = RetrofitClient.getInstance().getUserApiService();
        Call<UserResponse> call = userApiService.getUser(email, token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sharedPref.saveUserDetails(
                            response.body().getName(),
                            response.body().getEmail(),
                            response.body().getPhone(),
                            getOnlineBankingStatus(response),
                            null,
                            response.body().getAddress()
                    );
                }
            }

            private boolean getOnlineBankingStatus(Response<UserResponse> response) {
                assert response.body() != null;
                return response.body().getOnlineBankingStatus().equals(OnlineBankingStatus.ACTIVE);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Network Error while loading Data", Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    private void saveToken(String token) {
        AppSharedPreferenceManager.getInstance(this).saveJwtToken(token);
    }
}