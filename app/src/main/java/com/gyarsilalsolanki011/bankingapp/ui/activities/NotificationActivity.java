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
import com.gyarsilalsolanki011.bankingapp.databinding.ActivityNotificationBinding;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.NotificationAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    List<NotificationModel> notificationList;

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
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel("Transaction Successful", "You received ₹10,000.", "2 min ago"));
        notificationList.add(new NotificationModel("New Offer", "Get 5% cashback on bill payments.", "1 hr ago"));
        notificationList.add(new NotificationModel("Security Alert", "New login from an unknown device.", "5 hrs ago"));

        // Set Adapter
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        binding.recyclerViewNotifications.setAdapter(adapter);
    }

    //Call this method to add transaction notification
    public void sendTransactionNotifications(NotificationModel notification){
        notificationList.add(0, notification);
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