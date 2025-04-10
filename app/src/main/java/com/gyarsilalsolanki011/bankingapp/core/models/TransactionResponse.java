package com.gyarsilalsolanki011.bankingapp.core.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionStatus;
import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionType;

import java.util.Date;

public class TransactionResponse {
    private final Long transactionId;
    private final String accountNumber;
    private final TransactionType transactionType;
    private final Double amount;
    private final Date date;
    private final TransactionStatus transactionStatus;
    private final Double avlBalance;

    public TransactionResponse(Long transactionId, String accountNumber, TransactionType transactionType, Double amount, Date date, TransactionStatus transactionStatus, Double avlBalance) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.transactionStatus = transactionStatus;
        this.avlBalance = avlBalance;
    }

    public Long getTransactionId() {

        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public Double getAvlBalance() {
        return avlBalance;
    }
}
