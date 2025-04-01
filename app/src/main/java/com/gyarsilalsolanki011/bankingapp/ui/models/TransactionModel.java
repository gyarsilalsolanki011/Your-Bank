package com.gyarsilalsolanki011.bankingapp.ui.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionStatus;
import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionType;

public class TransactionModel {
    private final String date;
    private final TransactionType type;
    private final double amount;
    private final TransactionStatus success;

    public TransactionModel(String date, TransactionType type, double amount, TransactionStatus success) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.success = success;
    }

    public String getDate() { return date; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public TransactionStatus getTransactionStatus() { return success; }
}
