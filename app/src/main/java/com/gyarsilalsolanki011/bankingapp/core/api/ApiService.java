package com.gyarsilalsolanki011.bankingapp.core.api;

import com.gyarsilalsolanki011.bankingapp.core.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(
            @Query("email") String email,
            @Query("password") String password
    );
}
