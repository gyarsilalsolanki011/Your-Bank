package com.gyarsilalsolanki011.bankingapp.core.api.repository;

import com.gyarsilalsolanki011.bankingapp.core.models.AccountResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountApiService {
    @POST("/api/accounts/create")
    Call<StringResponse> createAccount(
            @Query("email") String email,
            @Query("accountType") String accountType,
            @Query("balance") double balance,
            @Header("Authorization") String token
    );

    @GET("/api/accounts/get-one")
    Call<AccountResponse> getAccount(
            @Query("email") String email,
            @Query("accountType") String accountType,
            @Header("Authorization") String token
    );

    @DELETE("/api/accounts/delete")
    Call<StringResponse> deleteAccount(
            @Query("email") String email,
            @Query("accountType") String accountType,
            @Header("Authorization") String token
    );

}
