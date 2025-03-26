package com.gyarsilalsolanki011.bankingapp.core.api.repository;

import androidx.annotation.Nullable;

import com.gyarsilalsolanki011.bankingapp.core.models.StringResponse;
import com.gyarsilalsolanki011.bankingapp.core.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApiService {
    @GET("/api/users/get-one")
    Call<UserResponse> getUser(
            @Query("email") String email,
            @Header("Authorization") String token
    );

    @DELETE("/api/users/delete")
    Call<StringResponse> delete(
            @Query("email") String email,
            @Header("Authorization") String token
    );

    @PUT("/api/users/update")
    Call<StringResponse> updateUser(
            @Query("originalEmail") String originalEmail,
            @Query("name") @Nullable String name,
            @Query("email") @Nullable String email,
            @Query("phone") @Nullable String phone,
            @Query("address") @Nullable String address,
            @Header("Authorization") String token
    );

    @GET("/api/users/online-banking-status")
    Call<StringResponse> getOnlineBankingStatus(
            @Query("email") String email,
            @Header("Authorization") String token
    );
}
