package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
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
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.AccountResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.UserResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityLoginBinding;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        binding.forgotAccountTextButton.setOnClickListener(v -> recoverPassword());
    }

    // Forgot Password Intent
    private void recoverPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
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
                        saveUserData(email);
                        isUserRegistered(email);
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

    private void isUserRegistered(String email) {
        boolean userRegistered = AppSharedPreferenceManager.getInstance(this).isUserRegistered();
        if (!userRegistered){
            fetchAccountDetails(email);
        }
    }

    private void saveUserData(String email) {
        AppSharedPreferenceManager.getInstance(this).saveUserEmail(email);
        UserSharedPreferencesManager sharedPref = UserSharedPreferencesManager.getInstance(this);

        String token = AppSharedPreferenceManager.getInstance(this).getJwtToken();

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
                            response.body().getOnlineBankingStatus(),
                            null,
                            response.body().getAddress()
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Network Error while loading Data", Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
                Log.d("User Details2", token);
            }
        });
    }


    private void fetchAccountDetails(String email) {
        String token = AppSharedPreferenceManager.getInstance(this).getJwtToken();

        UserApiService userApiService = RetrofitClient.getInstance().getUserApiService();
        Call<List<AccountResponse>> call = userApiService.getAllAccounts(email, token);

        call.enqueue(new Callback<List<AccountResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccountResponse>> call, @NonNull Response<List<AccountResponse>> response) {
                assert response.body() != null;
                List<AccountType> accountTypeList = response.body()
                        .stream()
                        .map(AccountResponse::getAccountType)
                        .collect(Collectors.toList());
                saveAccountTypeList(accountTypeList);
            }

            @Override
            public void onFailure(@NonNull Call<List<AccountResponse>> call, @NonNull Throwable throwable) {
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    private void saveToken(String token) {
        AppSharedPreferenceManager.getInstance(this).saveJwtToken(token);
    }

    private void saveAccountTypeList(List<AccountType> accountTypeList) {
        UserSharedPreferencesManager.getInstance(this).setUserAccounts(accountTypeList);
    }
}