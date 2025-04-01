package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileFragment extends Fragment {
    private TextView userName, userEmail, userPhone, userOnlineBankingStatus, userAccounts, userAddress;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userPhone = view.findViewById(R.id.userPhone);
        userOnlineBankingStatus = view.findViewById(R.id.userOnlineBankingStatus);
        userAccounts = view.findViewById(R.id.userAccounts);
        userAddress = view.findViewById(R.id.userAddress);
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        ImageView profileImage = view.findViewById(R.id.profileImage);

        // Load User Data from SharedPreferences or API
        loadUserData();

        // Open Edit Profile Activity
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        // Simulating data from SharedPreferences (Replace with API call if needed)
        UserSharedPreferencesManager sharedPref = UserSharedPreferencesManager.getInstance(getContext());

        userName.setText(sharedPref.getUserName());
        userEmail.setText(sharedPref.getUserEmail());
        userPhone.setText(sharedPref.getUserPhone());
        userOnlineBankingStatus.setText(sharedPref.getOnlineBankingStatus());
        userAccounts.setText(stringListWithComa(sharedPref.getUserAccounts()));
        userAddress.setText(sharedPref.getUserAddress());
    }

    private String stringListWithComa(List<AccountType> userAccounts) {
        String[] accounts = userAccounts.stream().map(Enum::toString).toArray(String[]::new);
        if (accounts.length > 1){
            return accounts[0]+", "+accounts[1];
        } else if (accounts.length == 1){
            return accounts[0];
        } else {
            return "Not Available, Create One";
        }
    }
}