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
import com.gyarsilalsolanki011.bankingapp.databinding.CreatePasswordBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePassword extends AppCompatActivity {
    private String email;
    private CreatePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = CreatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");

        binding.emailInput.setText(email);
        binding.createButton.setOnClickListener(v -> createPassword());
    }

    private void createPassword() {
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();
        String passwordConfirm = Objects.requireNonNull(binding.passwordConfirmInput.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(CreatePassword.this, "Email is required", Toast.LENGTH_SHORT).show();
            Log.d("Email", email);
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(CreatePassword.this, "Password is required", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordConfirm)){
                AuthApiService authApiService = RetrofitClient.getInstance().getAuthApiService();
                Call<StringResponse> call = authApiService.cratePassword(email, password);

                // Show Progress Bar and Disable Login Button
                binding.createProgressIndicator.setVisibility(View.VISIBLE);
                binding.createButton.setEnabled(false);

                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        binding.createProgressIndicator.setVisibility(View.GONE);
                        binding.createButton.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            String status = response.body().getStatus();
                            Toast.makeText(CreatePassword.this, status, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreatePassword.this, UserDashboardActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable throwable) {
                        binding.createProgressIndicator.setVisibility(View.GONE);
                        binding.createButton.setEnabled(true);
                        Toast.makeText(CreatePassword.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
                    }
                });
            }
        }
    }
}