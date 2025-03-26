package com.gyarsilalsolanki011.bankingapp.core.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;

public class AccountResponse {
    private Long accountId;
    private String accountHolderName;
    private String accountNumber;
    private AccountType accountType;
    private Double balance;
}
