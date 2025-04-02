package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.UserApiService;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.TokenManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityUpdateUserBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    private ActivityUpdateUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Toolbar
        setSupportActionBar(binding.toolbar);

        // Enable Back Button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setTitle("Update Service");

        binding.updateButton.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String email = Objects.requireNonNullElse(binding.userNameInput.getText(), null).toString().trim();
        String name = Objects.requireNonNullElse(binding.emailInput.getText(), null).toString().trim();
        String phone = Objects.requireNonNullElse(binding.phoneNumberInput.getText(), null).toString().trim();
        String address = Objects.requireNonNullElse(binding.addressInput.getText(), null).toString().trim();

        String originalEmail = UserSharedPreferencesManager.getInstance(this).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(this).getJwtToken();

        UserApiService userApiService = RetrofitClient.getInstance().getUserApiService();
        Call<StringResponse> call = userApiService.updateUser(originalEmail, email, name, phone, address, token);

        // Show Progress Bar and Disable Login Button
        binding.updateProgressIndicator.setVisibility(View.VISIBLE);
        binding.updateButton.setEnabled(false);

        call.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                binding.updateProgressIndicator.setVisibility(View.GONE);
                binding.updateButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    if (!email.isEmpty() || !name.isEmpty()) {
                        TokenManager.logoutUser(getApplicationContext());
                        // Redirect to Login Activity
                        Intent intent = new Intent(UpdateUserActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    String status = response.body().getStatus();
                    Toast.makeText(UpdateUserActivity.this, status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable throwable) {
                binding.updateProgressIndicator.setVisibility(View.GONE);
                binding.updateButton.setEnabled(true);
                Toast.makeText(UpdateUserActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}