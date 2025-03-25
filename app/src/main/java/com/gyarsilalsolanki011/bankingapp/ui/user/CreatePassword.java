package com.gyarsilalsolanki011.bankingapp.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.ApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.databinding.CreatePasswordBinding;

import java.util.Objects;

import retrofit2.Call;

public class CreatePassword extends AppCompatActivity {
    private String emailExtra;
    private CreatePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = CreatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailExtra = getIntent().getStringExtra("email");
        binding.createButton.setOnClickListener(v -> createPassword());
    }

    private void createPassword() {
        String email = Objects.requireNonNull(binding.emailInput.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();
        String passwordConfirm = Objects.requireNonNull(binding.passwordConfirmInput.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(CreatePassword.this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(CreatePassword.this, "Password is required", Toast.LENGTH_SHORT).show();
        } else if (email.equals(emailExtra)) {
            Toast.makeText(CreatePassword.this, "Please Enter the same email", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordConfirm)){
                ApiService apiService = RetrofitClient.getInstance().getApi();
                Call<StringResponse> call = apiService.cratePassword(email, password);

                // Show Progress Bar and Disable Login Button
                binding.createProgressIndicator.setVisibility(View.VISIBLE);
                binding.createButton.setEnabled(false);
            }
        }
    }
}