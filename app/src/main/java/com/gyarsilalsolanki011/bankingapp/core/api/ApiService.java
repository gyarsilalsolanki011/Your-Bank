package com.gyarsilalsolanki011.bankingapp.core.api;

import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
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

    @POST("/api/auth/login")
    Call<StringResponse> cratePassword(
            @Query("email") String email,
            @Query("bankingPassword") String password
    );
}
