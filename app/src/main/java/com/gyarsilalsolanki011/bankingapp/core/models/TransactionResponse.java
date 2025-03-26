package com.gyarsilalsolanki011.bankingapp.core.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionStatus;
import com.gyarsilalsolanki011.bankingapp.core.enums.TransactionType;

import java.util.Date;

public class TransactionResponse {
    private Long transactionId;
    private String accountNumber;
    private TransactionType transactionType;
    private Double amount;
    private Date date;
    private TransactionStatus transactionStatus;
}
