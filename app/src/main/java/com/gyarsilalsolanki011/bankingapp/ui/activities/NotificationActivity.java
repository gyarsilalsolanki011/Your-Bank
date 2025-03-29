package com.gyarsilalsolanki011.bankingapp.ui.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityNotificationBinding;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.NotificationAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Enable Back Button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setTitle("Notifications");

        // Initialize RecyclerView
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        //Initialise Notifications
         List<NotificationModel> notificationList = AppSharedPreferenceManager.getInstance(this).getNotificationList();

        // Set Adapter
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        binding.recyclerViewNotifications.setAdapter(adapter);
    }

    //Call this method to add transaction notification
    public void sendTransactionNotifications(NotificationModel notification){
        List<NotificationModel> notifications = new ArrayList<>();
        notifications.add(0, notification);
        AppSharedPreferenceManager.getInstance(this).saveNotificationList(notifications);
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