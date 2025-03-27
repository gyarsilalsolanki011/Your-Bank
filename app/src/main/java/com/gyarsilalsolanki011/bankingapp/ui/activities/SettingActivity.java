package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivitySettingBinding;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.SettingsAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.SettingsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Toolbar
        setSupportActionBar(binding.toolbar);

        // Enable Back Button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");

        // Example Data (You can load from API)
        binding.tvUserName.setText("Tiger Bhau");
        binding.tvUserEmail.setText("tiger@yahu.in");

        List<SettingsItem> settingsList = new ArrayList<>();
        settingsList.add(new SettingsItem(R.drawable.ic_profile, "Account", "Manage your account settings"));
        settingsList.add(new SettingsItem(R.drawable.ic_security, "Security", "Change password and manage security settings"));
        settingsList.add(new SettingsItem(R.drawable.ic_notifications, "Notifications", "Customize your notification preferences"));
        settingsList.add(new SettingsItem(R.drawable.ic_theme, "Theme", "Switch between light and dark mode"));
        settingsList.add(new SettingsItem(R.drawable.ic_logout, "Logout", "Sign out of your account"));

        SettingsAdapter adapter = new SettingsAdapter(settingsList, this);
        binding.rvSettings.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSettings.setAdapter(adapter);

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