package com.gyarsilalsolanki011.bankingapp.ui.Mappers;

import android.accounts.Account;

import com.gyarsilalsolanki011.bankingapp.core.models.AccountResponse;
import com.gyarsilalsolanki011.bankingapp.ui.models.AccountModel;

public class AccountMapper {
    public static AccountModel mapToAccountModel(AccountResponse accountResponse) {
        return new AccountModel(
                accountResponse.getAccountType(),
                accountResponse.getAccountNumber(),
                accountResponse.getBalance()
        );
    }
}
