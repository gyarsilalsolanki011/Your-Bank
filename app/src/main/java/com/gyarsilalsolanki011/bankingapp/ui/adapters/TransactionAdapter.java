package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final Context context;
    private final List<TransactionModel> transactionList;

    public TransactionAdapter(Context context, List<TransactionModel> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);

        holder.tvAmount.setText("₹" + transaction.getAmount());
        holder.tvDate.setText(transaction.getDate());
        holder.tvType.setText(transaction.getType());
        holder.tvStatus.setText(transaction.isSuccess() ? "Success" : "Failed");

        // Change Icon Based on Transaction Type
        if (transaction.getType().equalsIgnoreCase("Deposit")) {
            holder.ivIcon.setImageResource(R.drawable.ic_deposit);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.deposit_color));
        } else if (transaction.getType().equalsIgnoreCase("Withdrawal")){
            holder.ivIcon.setImageResource(R.drawable.ic_withdrawal);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.withdraw_color));
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_transfer);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.transfer_color));
        }

        // Change Icon Based on Transaction Type
        if (transaction.isSuccess()){
            holder.tvStatus.setTextColor(Color.GREEN);
        } else {
            holder.tvStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvDate, tvType, tvStatus;
        ImageView ivIcon;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvType = itemView.findViewById(R.id.tvType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
