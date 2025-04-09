package com.gyarsilalsolanki011.bankingapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.AccountApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.TransactionApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.AccountResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogCreateAccountBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogDepositBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogTransferBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogWithdrawBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.FragmentServiceBinding;
import com.gyarsilalsolanki011.bankingapp.ui.Mappers.TransactionMapper;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.QrScannerActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.SettingActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.UpdateUserActivity;
import com.gyarsilalsolanki011.bankingapp.ui.models.NotificationModel;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceFragment extends Fragment {
    private FragmentServiceBinding binding;
    private ActivityResultLauncher<Intent> qrScannerLauncher;

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
        AppSharedPreferenceManager sharedPref = AppSharedPreferenceManager.getInstance(getContext());
        String accountNumber;
        AccountType type;

        binding.createAccountService.setOnClickListener(v -> showCreateAccountDialog());

        // Save accounts to App preference
        List<AccountType> accountTypeList = UserSharedPreferencesManager.getInstance(getContext()).getUserAccounts();
        for (AccountType accountType : accountTypeList){
            saveAccountNumber(accountType);
        }

        if (!accountTypeList.isEmpty()){
            String accountType = sharedPref.getDefaultAccount();
            if (accountType.equals("SAVINGS_ACCOUNT")) {
                accountNumber = sharedPref.getSavingAccount();
                type = AccountType.SAVINGS;
            } else if (accountType.equals("CURRENT_ACCOUNT")) {
                accountNumber = sharedPref.getCurrentAccount();
                type = AccountType.CURRENT;
            } else {
                accountNumber = sharedPref.getFixedDepositAccount();
                type = AccountType.FIXED_DEPOSIT;
            }
        } else {
            accountNumber = "952500013706";
            type = AccountType.SAVINGS;
        }

        qrScannerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String scannedAccountNumber = result.getData().getStringExtra("accountNumber");
                        Toast.makeText(getContext(), "Scanned Account: " + scannedAccountNumber, Toast.LENGTH_SHORT).show();

                        showTransferMoneyDialog(accountNumber, type, scannedAccountNumber);
                    }
                }
        );

        binding.depositMoneyService.setOnClickListener(v -> showDepositMoneyDialog(accountNumber, type));

        binding.withdrawMoneyService.setOnClickListener(v -> showWithdrawMoneyDialog(accountNumber, type));

        binding.transferMoneyService.setOnClickListener(v -> showTransferMoneyDialog(accountNumber, type, null));

        binding.updateProfileService.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UpdateUserActivity.class);
            startActivity(intent);
        });

        binding.viewNotificationService.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            startActivity(intent);
        });

        binding.qrScannerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QrScannerActivity.class);
            qrScannerLauncher.launch(intent);
        });
    }

    // 🔹create account Dialog
    private void showCreateAccountDialog() {
        DialogCreateAccountBinding binding = DialogCreateAccountBinding.inflate(LayoutInflater.from(getContext()));

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // set email
        binding.emailInput.setText(UserSharedPreferencesManager.getInstance(getContext()).getUserEmail());

        // Handle Withdraw Button Click
        binding.createAccountButton.setOnClickListener(v -> {
            String input = Objects.requireNonNull(binding.amountInput.getText()).toString().trim();
            String accountType = Objects.requireNonNull(binding.accountTypeInput.getText()).toString().trim();

            if (input.isEmpty()) {
                binding.amountInput.setError("Field cannot be empty");
            } else if (accountType.isEmpty()){
                binding.accountTypeInput.setError("Account Type is required");
            } else if (!isAccountTypeValid(accountType)) {
                binding.accountTypeInput.setError("Invalid account type! Choose: SAVINGS, CURRENT, or FIXED_DEPOSIT.");
            } else {
                try {
                    double amount = Double.parseDouble(input);
                    createAccount(accountType, amount);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            }
        });
    }

    private boolean isAccountTypeValid(String accountType) {
        AccountType type;
        try {
            type = AccountType.valueOf(accountType.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void createAccount(String accountType, double balance) {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        AccountApiService accountApiService = RetrofitClient.getInstance().getAccountApiService();
        Call<StringResponse> call = accountApiService.createAccount(email, accountType, balance, token);

        call.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    List<AccountType> accountTypes = UserSharedPreferencesManager.getInstance(getContext()).getUserAccounts();
                    accountTypes.add(0, AccountType.valueOf(accountType.toUpperCase()));
                    UserSharedPreferencesManager.getInstance(getContext()).setUserAccounts(accountTypes);
                    AppSharedPreferenceManager.getInstance(getContext()).saveUserRegistered(false);
                    sendNotification(new Date(),
                            "Account Created",
                            "Your "+accountType+" Account Created Successfully");
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    Toast.makeText(getContext(), "Please select Default account", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    // 🔹Deposit Money Dialog
    private void showDepositMoneyDialog(String accountNumber, AccountType accountType) {
        DialogDepositBinding binding = DialogDepositBinding.inflate(LayoutInflater.from(getContext()));

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
        binding.depositButton.setOnClickListener(v -> {
            String input = Objects.requireNonNull(binding.amountInput.getText()).toString().trim();
            if (!input.isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    doDeposit(accountType.toString(), amount);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            } else {
                binding.amountInput.setError("Field cannot be empty");
            }
        });
    }

    private void doDeposit(String accountType, double amount) {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        TransactionApiService apiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<TransactionResponse> call = apiService.deposit(accountType, amount, email, token);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(getContext(), "Your Deposit is success!", Toast.LENGTH_SHORT).show();
                    TransactionModel transaction = TransactionMapper.mapToTransactionModel(response.body());
                    sendNotification(response.body().getDate(),
                            "Transaction Successful",
                            "₹"+transaction.getAmount()+" Credited from your account");
                    saveRecentTransaction(transaction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
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
                    sendNotification(response.body().getDate(),
                            "Transaction Successful",
                            "₹"+transaction.getAmount()+" Debited from your account");
                    saveRecentTransaction(transaction);
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
    private void showTransferMoneyDialog(String accountNumber, AccountType accountType, @Nullable String scannedAccountNumber) {
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
        if (scannedAccountNumber != null){
            binding.toAccountNumberInput.setText(scannedAccountNumber);
        }

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
                    sendNotification(response.body().getDate(),
                            "Transaction Successful",
                            "₹"+transaction.getAmount()+" Debited from your account");
                    saveRecentTransaction(transaction);
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
    private void sendNotification(Date date, String title, String message) {
        List<NotificationModel> notificationList = AppSharedPreferenceManager.getInstance(getContext()).getNotificationList();
        notificationList.add(0, new NotificationModel(title, message, date));
        AppSharedPreferenceManager.getInstance(getContext()).saveNotificationList(notificationList);
    }

    private void saveRecentTransaction(TransactionModel transaction) {
        List<TransactionModel> transactionList = AppSharedPreferenceManager.getInstance(getContext()).getRecentTransactionList();
        transactionList.add(0, transaction);
        AppSharedPreferenceManager.getInstance(getContext()).saveRecentTransactionList(transactionList);
    }

    private void saveAccountNumber(AccountType accountType) {
        String email = UserSharedPreferencesManager.getInstance(getContext()).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(getContext()).getJwtToken();

        AccountApiService accountApiService = RetrofitClient.getInstance().getAccountApiService();
        Call<AccountResponse> call = accountApiService.getAccount(email, accountType.toString(), token);

        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccountResponse> call, @NonNull Response<AccountResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (accountType.equals(AccountType.SAVINGS)){
                        AppSharedPreferenceManager.getInstance(getContext()).saveSavingAccount(response.body().getAccountNumber());
                    } else if (accountType.equals(AccountType.CURRENT)){
                        AppSharedPreferenceManager.getInstance(getContext()).saveCurrentAccount(response.body().getAccountNumber());
                    } else {
                        AppSharedPreferenceManager.getInstance(getContext()).saveFixedDepositAccount(response.body().getAccountNumber());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }
}