package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;

import java.util.List;

public class AccountPagerAdapter extends RecyclerView.Adapter<AccountPagerAdapter.AccountViewHolder> {
    private final Context context;
    private final List<AccountModel> accountList;
    private boolean isBalanceVisible = false;

    public AccountPagerAdapter(Context context, List<AccountModel> accountList) {
        this.context = context;
        this.accountList = accountList;
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
        holder.tvAccountType.setText(account.getAccountType());
        holder.tvAccountNumber.setText("***********" + account.getAccountNumber().substring(account.getAccountNumber().length() - 4));

        // Toggle balance visibility
        holder.eyeIcon.setOnClickListener(v -> {
            isBalanceVisible = !isBalanceVisible;
            holder.tvBalance.setText(isBalanceVisible ? "₹" + account.getBalance() : "****");
            if (holder.tvBalance.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                holder.tvBalance.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                holder.eyeIcon.setImageResource(R.drawable.ic_eye_open);
            } else {
                holder.tvBalance.setTransformationMethod(PasswordTransformationMethod.getInstance());
                holder.eyeIcon.setImageResource(R.drawable.ic_eye_closed);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountType, tvAccountNumber, tvBalance;
        ImageView eyeIcon;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountType = itemView.findViewById(R.id.tvAccountType);
            tvAccountNumber = itemView.findViewById(R.id.tvAccountNumber);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            eyeIcon = itemView.findViewById(R.id.eyeIcon);
        }
    }
}
