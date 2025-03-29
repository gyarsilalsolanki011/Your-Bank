package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.ui.adapters.TransactionAdapter;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView rvTransactionHistory;

    public HistoryFragment() {
        // Required empty public constructor
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

        // Load transaction data
        loadTransactionHistory();
    }

    private void loadTransactionHistory() {
        // Dummy transaction list (Replace with API call)
        List<TransactionModel> transactionList = new ArrayList<>();
        transactionList.add(new TransactionModel("TXN001", "25 Mar 2025", "Deposit", 5000.00, true));
        transactionList.add(new TransactionModel("TXN002", "22 Mar 2025", "Withdrawal", 2000.00, true));
        transactionList.add(new TransactionModel("TXN003", "20 Mar 2025", "Transfer", 7500.00, true));
        transactionList.add(new TransactionModel("TXN004", "18 Mar 2025", "Withdrawal", 1500.00, false)); // Failed txn
        transactionList.add(new TransactionModel("TXN005", "15 Mar 2025", "Deposit", 3000.00, true));

        // Set Adapter
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);
        rvTransactionHistory.setAdapter(transactionAdapter);
    }
}