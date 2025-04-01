package com.gyarsilalsolanki011.bankingapp.ui.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionType;

public class TransactionModel {
    private final String date;
    private final String type; // Deposit / Withdrawal
    private final double amount;
    private final boolean success;

    public TransactionModel(String date, String type, double amount, boolean success) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.success = success;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public boolean isSuccess() { return success; }
}
