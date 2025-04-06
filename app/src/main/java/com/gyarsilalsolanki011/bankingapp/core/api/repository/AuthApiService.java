package com.gyarsilalsolanki011.bankingapp.core.api.repository;

import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AuthApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("/api/auth/create")
    Call<StringResponse> registerUser(
           @Query("name")  String name,
           @Query("email") String email,
           @Query("phone") String phone,
           @Query("address") String address
    );

    @POST("/api/auth/request-online-banking")
    Call<StringResponse> cratePassword(
            @Query("email") String email,
            @Query("bankingPassword") String password
    );

    @PUT("/api/auth/recover-password")
    Call<StringResponse> recoverPassword(
            @Query("email") String email,
            @Query("password") String password
    );
}
