package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.TransactionApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogTransferBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogWithdrawBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.FragmentServiceBinding;
import com.gyarsilalsolanki011.bankingapp.ui.Mappers.TransactionMapper;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.UpdateUserActivity;
import com.gyarsilalsolanki011.bankingapp.ui.models.NotificationModel;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceFragment extends Fragment {
    private FragmentServiceBinding binding;

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createAccountService.setOnClickListener(v -> showCreateAccountDialog());

        binding.depositMoneyService.setOnClickListener(v -> showDepositMoneyDialog());

        binding.withdrawMoneyService.setOnClickListener(v -> showWithdrawMoneyDialog("952500083680", AccountType.SAVINGS));

        binding.transferMoneyService.setOnClickListener(v -> showTransferMoneyDialog("952500083680", AccountType.SAVINGS));

        binding.updateProfileService.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UpdateUserActivity.class);
            startActivity(intent);
        });

        binding.viewNotificationService.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            startActivity(intent);
        });

    }

    // 🔹create account Dialog
    private void showCreateAccountDialog() {

    }

    // 🔹Deposit Money Dialog
    private void showDepositMoneyDialog() {

    }

    // 🔹Withdraw Money Dialog
    private void showWithdrawMoneyDialog(String accountNumber, AccountType accountType) {
        DialogWithdrawBinding binding = DialogWithdrawBinding.inflate(LayoutInflater.from(getContext()));

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Set Text
        binding.accountTypeInput.setText(accountType.toString());
        binding.accountNumberInput.setText(accountNumber);

        // Handle Withdraw Button Click
        binding.withdrawButton.setOnClickListener(v -> {
            String input = Objects.requireNonNull(binding.amountInput.getText()).toString().trim();
            if (!input.isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    makeWithdrawal(accountType.toString(), amount);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            } else {
                binding.amountInput.setError("Field cannot be empty");
            }
        });
    }

    private void makeWithdrawal(String accountType, double amount) {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        TransactionApiService apiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<TransactionResponse> call = apiService.withdraw(accountType, amount, email, token);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(getContext(), "Your Withdrawal is success!", Toast.LENGTH_SHORT).show();
                    TransactionModel transaction = TransactionMapper.mapToTransactionModel(response.body());
                    sendNotification(transaction, response.body().getDate());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }


    // 🔹Transfer Money Dialog
    private void showTransferMoneyDialog(String accountNumber, AccountType accountType) {
        DialogTransferBinding binding = DialogTransferBinding.inflate(LayoutInflater.from(getContext()));

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Set Text
        binding.accountTypeInput.setText(accountType.toString());
        binding.accountNumberInput.setText(accountNumber);

        // Handle Transfer Button Click
        binding.transferButton.setOnClickListener(v -> {
            String input = Objects.requireNonNull(binding.amountInput.getText()).toString().trim();
            String toAccountNumber = Objects.requireNonNull(binding.toAccountNumberInput.getText()).toString().trim();
            if (input.isEmpty()) {
                binding.amountInput.setError("Field cannot be empty");
            } else if (toAccountNumber.isEmpty()){
                binding.toAccountNumberInput.setError("To account Number is required");
            } else {
                try {
                    double amount = Double.parseDouble(input);
                    makeTransfer(accountType.toString(), amount, toAccountNumber);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            }
        });
    }

    private void makeTransfer(String accountType, double amount, String toAccountNumber) {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        TransactionApiService apiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<TransactionResponse> call = apiService.transfer(accountType, amount, email, toAccountNumber, token);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(getContext(), "Your Transfer is success !", Toast.LENGTH_SHORT).show();
                    TransactionModel transaction = TransactionMapper.mapToTransactionModel(response.body());
                    sendNotification(transaction, response.body().getDate());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));

            }
        });
    }


    // 🔹Save Transaction To sharedPreference
    private void sendNotification(TransactionModel transaction, Date date) {
        List<NotificationModel> notificationList = AppSharedPreferenceManager.getInstance(getContext()).getNotificationList();
        notificationList.add(0, new NotificationModel(
                "Transaction Successful",
                "₹"+transaction.getAmount()+" Debited from your account",
                 date));
        AppSharedPreferenceManager.getInstance(getContext()).saveNotificationList(notificationList);
    }
}