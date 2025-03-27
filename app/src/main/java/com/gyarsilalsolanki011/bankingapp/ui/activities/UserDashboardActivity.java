package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityUserDashboardBinding;
import com.gyarsilalsolanki011.bankingapp.ui.fragments.HistoryFragment;
import com.gyarsilalsolanki011.bankingapp.ui.fragments.HomeFragment;
import com.gyarsilalsolanki011.bankingapp.ui.fragments.ProfileFragment;
import com.gyarsilalsolanki011.bankingapp.ui.fragments.ServiceFragment;

public class UserDashboardActivity extends AppCompatActivity {
    private ActivityUserDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set Default Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()).commit();
        }


        // Handle Bottom Navigation Selection
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragments(new HomeFragment());
            } else if (id == R.id.nav_services) {
                loadFragments(new ServiceFragment());
            } else if (id == R.id.nav_history) {
                loadFragments(new HistoryFragment());
            } else if (id == R.id.nav_profile) {
                loadFragments(new ProfileFragment());
            }
            return true;
        });

        // Handle Drawer Navigation Clicks
        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            binding.drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after selection

            if (id == R.id.nav_about_us) {
                startActivity(new Intent(this, AboutUsActivity.class));
            } else if (id == R.id.nav_contact_us) {
                startActivity(new Intent(this, ContactUsActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingActivity.class));
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationActivity.class));
            }
            return true;
        });
    }

    public void loadFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

}