package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.AccountAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.TransactionAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recentTransactions, allAccounts;

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


        TextView tvUserName = view.findViewById(R.id.tvUserName);
        recentTransactions = view.findViewById(R.id.recyclerTransactions);
        allAccounts = view.findViewById(R.id.accountRecycler);

        // Initialise recycler view
        allAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        recentTransactions.setLayoutManager(new LinearLayoutManager(getContext()));


        // Attach SnapHelper to make scrolling stop at each item
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(allAccounts);

        // Load transaction data
        loadTransactionHistory();

        // Load Accounts
        loadUserAccounts();

        // set name from sharedPreference
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", "Tiger");
        tvUserName.setText("Hello "+extractFirstName(userName));

        // Notification Icon
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });
    }

    private void loadTransactionHistory() {
        // Dummy transaction list (Replace with API call)
        List<TransactionModel> transactionList = new ArrayList<>();
        transactionList.add(new TransactionModel("TXN001", "25 Mar 2025", "Transfer", 5000.00, true));
        transactionList.add(new TransactionModel("TXN002", "22 Mar 2025", "Withdrawal", 2000.00, true));
        transactionList.add(new TransactionModel("TXN003", "20 Mar 2025", "Transfer", 7500.00, true));
        transactionList.add(new TransactionModel("TXN004", "18 Mar 2025", "Withdrawal", 1500.00, false)); // Failed txn
        transactionList.add(new TransactionModel("TXN005", "15 Mar 2025", "Transfer", 3000.00, true));

        // Set Adapter
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);
        recentTransactions.setAdapter(transactionAdapter);
    }

    private void loadUserAccounts() {
        List<AccountModel> accountList = new ArrayList<>();
        accountList.add(new AccountModel("Saving", "1234567890", "5000"));
        accountList.add(new AccountModel("Current", "0987654321", "12000"));

        // set Adapter
        AccountAdapter accountAdapter = new AccountAdapter(requireContext(), accountList);
        allAccounts.setAdapter(accountAdapter);
    }

    public static String extractFirstName(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "";
        }

        String[] parts = username.trim().split("\\s+");

        return parts[0];
    }
}