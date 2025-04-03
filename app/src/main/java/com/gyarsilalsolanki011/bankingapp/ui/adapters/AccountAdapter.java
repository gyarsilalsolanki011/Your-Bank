package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.api.RetrofitClient;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.TransactionApiService;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.UserSharedPreferencesManager;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogTransferBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogWithdrawBinding;
import com.gyarsilalsolanki011.bankingapp.ui.Mappers.TransactionMapper;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;
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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private final Context context;
    private final List<AccountModel> accountList;
    private boolean isBalanceVisible = false;

    public AccountAdapter(Context context, List<AccountModel> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<AccountModel> newData) {
        this.accountList.clear();  // Clear old data
        this.accountList.addAll(newData);  // Add new data
        notifyDataSetChanged();  // Refresh UI
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account_card, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountModel account = accountList.get(position);
        holder.tvAccountType.setText(account.getAccountType().toString());
        holder.tvAccountNumber.setText("***********" + account.getAccountNumber().substring(account.getAccountNumber().length() - 4));

        // Toggle balance visibility
        holder.eyeIcon.setOnClickListener(v -> toggleBalance(holder, account));

        // Handle Withdraw Button Click
        holder.btnWithdraw.setOnClickListener(v -> {
            showWithdrawDialog(account.getAccountNumber(), account.getAccountType());
        });

        // Handle Transfer Button Click
        holder.btnTransfer.setOnClickListener(v -> {
            showTransferDialog(account.getAccountNumber(), account.getAccountType());
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountType, tvAccountNumber, tvBalance;
        Button btnWithdraw, btnTransfer;
        ImageView eyeIcon;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountType = itemView.findViewById(R.id.tvAccountType);
            tvAccountNumber = itemView.findViewById(R.id.tvAccountNumber);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            eyeIcon = itemView.findViewById(R.id.eyeIcon);
            btnWithdraw = itemView.findViewById(R.id.btnWithdraw);
            btnTransfer = itemView.findViewById(R.id.btnTransfer);
        }
    }

    private void toggleBalance(AccountViewHolder holder, AccountModel account) {
        isBalanceVisible = !isBalanceVisible;
        holder.tvBalance.setText(isBalanceVisible ? "₹" + account.getBalance() : "********");
        holder.eyeIcon.setImageResource(isBalanceVisible ? R.drawable.ic_eye_open : R.drawable.ic_eye_closed );
    }

    // Withdraw method
    private void showWithdrawDialog(String accountNumber, AccountType accountType) {
        // Use View Binding for Dialog
        DialogWithdrawBinding binding = DialogWithdrawBinding.inflate(LayoutInflater.from(context));

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        String email = UserSharedPreferencesManager.getInstance(context).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(context).getJwtToken();

        TransactionApiService apiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<TransactionResponse> call = apiService.withdraw(accountType, amount, email, token);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(context, "Money Withdrawal is success", Toast.LENGTH_SHORT).show();
                    TransactionModel transaction = TransactionMapper.mapToTransactionModel(response.body());
                    sendNotification(transaction, response.body().getDate());
                    saveRecentTransaction(transaction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(context, "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));
            }
        });
    }

    // Transfer Method
    private void showTransferDialog(String accountNumber, AccountType accountType) {
        DialogTransferBinding binding = DialogTransferBinding.inflate(LayoutInflater.from(context));

        // Create Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        String email = UserSharedPreferencesManager.getInstance(context).getUserEmail();
        String token = AppSharedPreferenceManager.getInstance(context).getJwtToken();

        TransactionApiService apiService = RetrofitClient.getInstance().getTransactionApiService();
        Call<TransactionResponse> call = apiService.transfer(accountType, amount, email, toAccountNumber, token);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call, @NonNull Response<TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(context, "Money Debited Successfully ", Toast.LENGTH_SHORT).show();
                    TransactionModel transaction = TransactionMapper.mapToTransactionModel(response.body());
                    sendNotification(transaction, response.body().getDate());
                    saveRecentTransaction(transaction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(context, "Error: "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Network Error", Objects.requireNonNull(throwable.getMessage()));

            }
        });

    }

    private void sendNotification(TransactionModel transaction, Date date) {
        List<NotificationModel> notificationList = AppSharedPreferenceManager.getInstance(context).getNotificationList();
        notificationList.add(0, new NotificationModel(
                "Transaction Successful",
                "₹"+transaction.getAmount()+" Debited from your account",
                date));
        AppSharedPreferenceManager.getInstance(context).saveNotificationList(notificationList);
    }

    private void saveRecentTransaction(TransactionModel transaction) {
        List<TransactionModel> transactionList = AppSharedPreferenceManager.getInstance(context).getRecentTransactionList();
        transactionList.add(0, transaction);
        AppSharedPreferenceManager.getInstance(context).saveRecentTransactionList(transactionList);
    }
}
