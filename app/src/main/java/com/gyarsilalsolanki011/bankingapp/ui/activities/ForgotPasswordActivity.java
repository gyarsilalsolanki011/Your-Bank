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

import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.AuthApiService;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityForgotPasswordBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String email = AppSharedPreferenceManager.getInstance(this).getUserEmail();

        binding.emailInput.setText(email);
        binding.recoverButton.setOnClickListener(v -> recoverPassword(email));
    }

    private void recoverPassword(String email) {
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();
        String passwordConfirm = Objects.requireNonNull(binding.passwordConfirmInput.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgotPasswordActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(ForgotPasswordActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordConfirm)){
                AuthApiService authApiService = RetrofitClient.getInstance().getAuthApiService();
                Call<StringResponse> call = authApiService.recoverPassword(email, password);

                // Show Progress Bar and Disable Login Button
                binding.recoverProgressIndicator.setVisibility(View.VISIBLE);
                binding.recoverButton.setEnabled(false);

                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        binding.recoverProgressIndicator.setVisibility(View.GONE);
                        binding.recoverButton.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            String status = response.body().getStatus();
                            Toast.makeText(ForgotPasswordActivity.this, status, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable throwable) {
                        binding.recoverProgressIndicator.setVisibility(View.GONE);
                        binding.recoverButton.setEnabled(true);
                        Toast.makeText(ForgotPasswordActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
            }
        }
    }
}