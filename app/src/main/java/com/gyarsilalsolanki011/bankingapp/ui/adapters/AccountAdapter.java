package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogTransferBinding;
import com.gyarsilalsolanki011.bankingapp.databinding.DialogWithdrawBinding;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;

import java.util.List;
import java.util.Objects;

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
            Toast.makeText(holder.itemView.getContext(), "Withdraw from " + account.getAccountNumber(), Toast.LENGTH_SHORT).show();
        });

        // Handle Transfer Button Click
        holder.btnTransfer.setOnClickListener(v -> {
            showTransferDialog(account.getAccountNumber(), account.getAccountType());
            Toast.makeText(holder.itemView.getContext(), "Transfer from " + account.getAccountNumber(), Toast.LENGTH_SHORT).show();
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
                    makeWithdrawal(accountType, amount);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            } else {
                binding.amountInput.setError("Field cannot be empty");
            }
        });
    }

    private void makeWithdrawal(AccountType accountType, double amount) {

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
                    makeTransfer(accountType, amount, toAccountNumber);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    binding.amountInput.setError("Enter a valid amount");
                }
            }
        });
    }

    private void makeTransfer(AccountType accountType, double amount, String toAccountNumber) {

    }
}
