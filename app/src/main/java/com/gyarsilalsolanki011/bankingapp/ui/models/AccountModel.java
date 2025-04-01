package com.gyarsilalsolanki011.bankingapp.ui.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;

public class AccountModel {
    private final String accountNumber;
    private final AccountType accountType;
    private final Double balance;

    public AccountModel(AccountType accountType, String accountNumber, Double balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Double getBalance() {
        return balance;
    }
}
