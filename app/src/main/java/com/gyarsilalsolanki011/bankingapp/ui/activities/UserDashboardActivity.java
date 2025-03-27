package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);

        // ✅ Hide the default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Open Drawer when clicking menu icon
        ImageView menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        // Customize SearchView
        searchView = findViewById(R.id.searchView);

        // Trigger search on clicking search icon
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            searchView.requestFocus();
            searchView.setIconified(false);
            setupSearch();
        });


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

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        return super.getOnBackInvokedDispatcher();
    }

    public void loadFragments(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private void setupSearch() {
        searchView.setQueryHint("Search here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(UserDashboardActivity.this, "Searching: " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Implement live search filtering here
                return false;
            }
        });
    }

}