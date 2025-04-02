package com.gyarsilalsolanki011.bankingapp.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyarsilalsolanki011.bankingapp.core.enums.AccountType;
import com.gyarsilalsolanki011.bankingapp.core.enums.OnlineBankingStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserSharedPreferencesManager {
    private static final String PREF_NAME = "user_prefs";
    private static UserSharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    private UserSharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized UserSharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSharedPreferencesManager(context);
        }
        return instance;
    }

    // 🔹 Store User Data
    public void saveUserDetails(String name, String email, String phone, OnlineBankingStatus onlineBankingStatus, String accounts, String address) {
        editor.putString("user_name", name);
        editor.putString("user_email", email);
        editor.putString("user_phone", phone);
        editor.putString("online_banking", getProperCase(onlineBankingStatus));
        editor.putString("user_accounts", accounts);
        editor.putString("user_address", address);
        editor.apply();
    }

    private String getProperCase(OnlineBankingStatus onlineBankingStatus) {
        if (onlineBankingStatus.equals(OnlineBankingStatus.ACTIVE)){
            return "Active";
        } else if (onlineBankingStatus.equals(OnlineBankingStatus.NOT_ACTIVE)) {
            return "Not Active";
        } else {
            return "Pending for Approval";
        }
    }

    // 🔹 Clear UserAccounts Data
    public void clearUserAccounts(){
        editor.remove("user_accounts");
        editor.apply();
    }

    // 🔹 Set UserAccounts Data
    public void setUserAccounts(List<AccountType> accounts){
        String json = gson.toJson(accounts);
        editor.putString("user_accounts", json);
        editor.apply();
    }

    public List<AccountType> getUserAccounts() {
        String json = sharedPreferences.getString("user_accounts", null);
        Type type = new TypeToken<List<AccountType>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // 🔹 Get User Data
    public String getUserName() {
        return sharedPreferences.getString("user_name", "User Name");
    }

    public String getUserEmail() {
        return sharedPreferences.getString("user_email", "user@example.com");
    }

    public String getUserPhone() {
        return sharedPreferences.getString("user_phone", "+91 9876543210");
    }

    public String getOnlineBankingStatus() {
        return sharedPreferences.getString("online_banking", "Not Active");
    }

    public String getUserAddress() {
        return sharedPreferences.getString("user_address", "No Address Provided");
    }

    // 🔹 Clear Data (For Logout)
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }

}
