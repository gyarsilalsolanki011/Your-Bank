package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.TransactionApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.ui.Mappers.TransactionMapper;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.TransactionAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    private RecyclerView rvTransactionHistory;
    private TransactionAdapter transactionAdapter;
    private List<TransactionModel> newTransactionList;
    private final String email;
    private final String token;

    public HistoryFragment() {
        this.email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        this.token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        rvTransactionHistory = view.findViewById(R.id.rvTransactionHistory);
        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        List<AccountType>  accountTypeList = UserSharedPreferencesManager.getInstance(getContext())
                .getUserAccounts();

        for (AccountType accountType : accountTypeList){
            // Load transaction data for each account
            loadTransactionHistory(accountType.toString());
        }
    }

    private void loadTransactionHistory(String accountType) {
        TransactionApiService transactionApiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<List<TransactionResponse>> call = transactionApiService.getAllTransaction(accountType, email, token);

        call.enqueue(new Callback<List<TransactionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<TransactionResponse>> call, @NonNull Response<List<TransactionResponse>> response) {
                assert response.body() != null;
                List<TransactionModel> transactionList = response.body()
                        .stream()
                        .map(TransactionMapper::mapToTransactionModel)
                        .collect(Collectors.toList());
                updateTransactionRecyclerView(transactionList);
                saveTransactionList(transactionList);
            }

            @Override
            public void onFailure(@NonNull Call<List<TransactionResponse>> call, @NonNull Throwable throwable) {
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    private void updateTransactionRecyclerView(List<TransactionModel> transactionList) {
        if (transactionAdapter == null) {
            transactionAdapter = new TransactionAdapter(getContext(), transactionList);
            rvTransactionHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTransactionHistory.setAdapter(transactionAdapter);
        } else {
            transactionAdapter.setData(transactionList);  // Update data dynamically
        }
    }

    private void saveTransactionList(List<TransactionModel> transactionList) {
        AppSharedPreferenceManager.getInstance(getContext()).saveTransactionList(transactionList);
    }
}