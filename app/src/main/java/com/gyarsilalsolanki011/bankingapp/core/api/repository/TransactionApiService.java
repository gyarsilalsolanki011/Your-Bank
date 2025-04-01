package com.gyarsilalsolanki011.bankingapp.core.api.repository;

import com.gyarsilalsolanki011.bankingapp.core.models.TransactionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TransactionApiService {
    @POST("/api/transactions/deposit")
    Call<TransactionResponse> deposit(
            @Query("accountType") String accountType,
            @Query("amount") double amount,
            @Query("email") String email,
            @Header("Authorization") String token
    );

    @POST("/api/transactions/withdraw")
    Call<TransactionResponse> withdraw(
            @Query("accountType") String accountType,
            @Query("amount") double amount,
            @Query("email") String email,
            @Header("Authorization") String token
    );

    @POST("/api/transactions/transfer")
    Call<TransactionResponse> transfer(
            @Query("toAccountNumber") String toAccountNumber,
            @Query("accountType") String accountType,
            @Query("amount") double amount,
            @Query("email") String email,
            @Header("Authorization") String token
    );

    @GET("/api/transactions/account/transactions")
    Call<List<TransactionResponse>> getAllTransaction(
            @Query("accountType") String accountType,
            @Query("email") String email,
            @Header("Authorization") String token
    );
}
