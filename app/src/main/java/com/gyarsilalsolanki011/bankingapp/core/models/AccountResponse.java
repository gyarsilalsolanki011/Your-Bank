package com.gyarsilalsolanki011.bankingapp.core.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;

public class AccountResponse {
    private final Long accountId;
    private final String accountHolderName;
    private final String accountNumber;
    private final AccountType accountType;
    private final Double balance;

    public AccountResponse(Long accountId, String accountHolderName, String accountNumber, AccountType accountType, Double balance) {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
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
