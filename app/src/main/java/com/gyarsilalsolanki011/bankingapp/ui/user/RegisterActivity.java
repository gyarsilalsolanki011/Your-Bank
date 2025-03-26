package com.gyarsilalsolanki011.bankingapp.ui.user;

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
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityRegisterBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.alreadyAccountTextButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = Objects.requireNonNull(binding.emailInput.getText()).toString().trim();
        String name = Objects.requireNonNull(binding.userNameInput.getText()).toString().trim();
        String phone = Objects.requireNonNull(binding.phoneNumberInput.getText()).toString().trim();
        String address = Objects.requireNonNull(binding.addressInput.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "User Name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)){
            Toast.makeText(RegisterActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
        } else {
            AuthApiService authApiService = RetrofitClient.getInstance().getAuthApiService();
            Call<StringResponse>  call = authApiService.registerUser(name, email, phone, address);

            // Show Progress Bar and Disable Login Button
            binding.registerProgressIndicator.setVisibility(View.VISIBLE);
            binding.registerButton.setEnabled(false);

            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    binding.registerProgressIndicator.setVisibility(View.GONE);
                    binding.registerButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        String status = response.body().getStatus();
                        Toast.makeText(RegisterActivity.this, status, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, CreatePassword.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable throwable) {
                    binding.registerProgressIndicator.setVisibility(View.GONE);
                    binding.registerButton.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
                }
            });
        }
    }
}