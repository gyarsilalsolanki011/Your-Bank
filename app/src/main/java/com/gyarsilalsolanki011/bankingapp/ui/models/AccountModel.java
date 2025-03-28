package com.gyarsilalsolanki011.bankingapp.ui.models;

public class AccountModel {
    private final String accountNumber;
    private final String accountType;
    private final String balance;

    public AccountModel(String accountNumber, String accountType, String balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBalance() {
        return balance;
    }
}
