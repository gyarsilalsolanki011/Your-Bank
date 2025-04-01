package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.AccountApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.UserApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.AccountResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.ui.Mappers.AccountMapper;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.AccountAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.TransactionAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recentTransactions, allAccounts;
    private AccountAdapter accountAdapter;


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
        loadRecentTransactions();

        // Load Accounts
        fetchAccountDetails();

        // set name from sharedPreference
        String userName = UserSharedPreferencesManager.getInstance(getContext()).getUserName();
        tvUserName.setText("Hello " + extractFirstName(userName));

        // Notification Icon
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecentTransactions() {
        // Dummy transaction list (Replace with API call)
        List<TransactionModel> transactionList = new ArrayList<>();
        transactionList.add(new TransactionModel( "25 Mar 2025", "Transfer", 5000.00, true));
        transactionList.add(new TransactionModel( "22 Mar 2025", "Withdrawal", 2000.00, true));
        transactionList.add(new TransactionModel( "20 Mar 2025", "Transfer", 7500.00, true));
        transactionList.add(new TransactionModel( "18 Mar 2025", "Withdrawal", 1500.00, false)); // Failed txn
        transactionList.add(new TransactionModel( "15 Mar 2025", "Transfer", 3000.00, true));

        // Set Adapter
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);
        recentTransactions.setAdapter(transactionAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAccountDetails() {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        UserApiService userApiService = RetrofitClient.getInstance().getUserApiService();
        Call<List<AccountResponse>> call = userApiService.getAllAccounts(email, token);

        call.enqueue(new Callback<List<AccountResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccountResponse>> call, @NonNull Response<List<AccountResponse>> response) {
                assert response.body() != null;
                List<AccountModel> accountList = response.body()
                        .stream()
                        .map(AccountMapper::mapToAccountModel)
                        .collect(Collectors.toList());
                updateRecyclerView(accountList);
            }

            @Override
            public void onFailure(@NonNull Call<List<AccountResponse>> call, @NonNull Throwable throwable) {
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    // Setup RecyclerView and ensure immediate UI update
    private void updateRecyclerView(List<AccountModel> accountList) {
        if (accountAdapter == null) {
            accountAdapter = new AccountAdapter(getContext(), accountList);
            allAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
            allAccounts.setAdapter(accountAdapter);
        } else {
            accountAdapter.setData(accountList);  // Update data dynamically
        }
    }

    public static String extractFirstName(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "";
        }

        String[] parts = username.trim().split("\\s+");

        return parts[0];
    }
}