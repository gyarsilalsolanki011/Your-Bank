package com.gyarsilalsolanki011.bankingapp.core.api;

import com.gyarsilalsolanki011.bankingapp.core.api.repository.AccountApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.AuthApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.TransactionApiService;
import com.gyarsilalsolanki011.bankingapp.core.api.repository.UserApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://localhost:8080"; // Replace with your API base URL
    private static RetrofitClient instance = null;
    private final AuthApiService authApiService;
    private final UserApiService userApiService;
    private final AccountApiService accountApiService;
    private final TransactionApiService transactionApiService;



    private RetrofitClient() {
        // Logging interceptor for debugging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        authApiService = retrofit.create(AuthApiService.class);
        userApiService = retrofit.create(UserApiService.class);
        accountApiService = retrofit.create(AccountApiService.class);
        transactionApiService = retrofit.create(TransactionApiService.class);
    }

    // Singleton Instance
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // This method provides access to the Auth API service
    public AuthApiService getAuthApiService() {
        return authApiService;
    }

    public UserApiService getUserApiService() {
        return userApiService;
    }

    public AccountApiService getAccountApiService() {
        return accountApiService;
    }

    public TransactionApiService getTransactionApiService() {
        return transactionApiService;
    }
}
