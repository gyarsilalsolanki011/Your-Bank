package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.AccountPagerAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*ViewPager2 viewPagerAccounts = view.findViewById(R.id.viewPagerAccounts);
        *//*WormDotsIndicator dotsIndicator = view.findViewById(R.id.dotsIndicator);*//**/
        TextView tvUserName = view.findViewById(R.id.tvUserName);

        // set name from sharedPreference
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", "Tiger");
        tvUserName.setText("Hello "+extractFirstName(userName));

        // Load Accounts
        List<AccountModel> accountList = fetchUserAccounts();
        /*AccountPagerAdapter accountAdapter = new AccountPagerAdapter(requireContext(), accountList);
        viewPagerAccounts.setAdapter(accountAdapter);*/

        // Attach Indicator
        // dotsIndicator.attachTo(viewPagerAccounts);

        // Notification Icon
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });
    }

    public static String extractFirstName(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "";
        }

        String[] parts = username.trim().split("\\s+");

        return parts[0];
    }

    private List<AccountModel> fetchUserAccounts() {
        return Arrays.asList(
                new AccountModel("Saving", "1234567890", "5000"),
                new AccountModel("Current", "0987654321", "12000")
        );
    }
}