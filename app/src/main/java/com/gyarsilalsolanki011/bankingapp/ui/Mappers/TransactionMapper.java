package com.gyarsilalsolanki011.bankingapp.ui.Mappers;

import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionMapper {

    public static TransactionModel mapToTransactionModel(TransactionResponse response){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = sdf.format(new Date(String.valueOf(response.getDate())));
        return new TransactionModel(
                formattedDate,
                response.getTransactionType(),
                response.getAmount(),
                response.getTransactionStatus()
        );
    }
}
