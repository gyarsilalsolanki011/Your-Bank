package com.gyarsilalsolanki011.bankingapp.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyarsilalsolanki011.bankingapp.ui.models.NotificationModel;
import com.gyarsilalsolanki011.bankingapp.ui.models.TransactionModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppSharedPreferenceManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_JWT_TOKEN = "JWT_TOKEN";
    private static final String KEY_REGISTERED = "REGISTERED";
    private static final String KEY_CURRENT = "CURRENT_ACCOUNT";
    private static final String KEY_SAVINGS = "SAVINGS_ACCOUNT";
    private static final String KEY_FIXED = "FIXED_DEPOSIT";
    private static final String KEY_DEFAULT = "DEFAULT_ACCOUNT";
    private static final String KEY_NOTIFICATIONS = "NOTIFICATION_LIST";
    private static final String RECENT_TRANSACTION = "RECENT_TRANSACTION";

    private static AppSharedPreferenceManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    // 🔹 Private Constructor for Singleton
    private AppSharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // 🔹 Get Singleton Instance
    public static synchronized AppSharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppSharedPreferenceManager(context);
        }
        return instance;
    }

    // 🔹 Save User Registered
    public void saveUserRegistered(Boolean register) {
        editor.putBoolean(KEY_REGISTERED, register);
        editor.apply();
    }

    // 🔹 check User Registered
    public boolean isUserRegistered(){
        return sharedPreferences.getBoolean(KEY_REGISTERED, false);
    }

    // 🔹 Save JWT Token
    public void saveJwtToken(String token) {
        editor.putString(KEY_JWT_TOKEN, "Bearer " + token);
        editor.apply();
    }

    // 🔹 Retrieve JWT Token
    public String getJwtToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    // 🔹 Save Saving Account
    public void saveSavingAccount(String accountNumber) {
        editor.putString(KEY_SAVINGS, accountNumber);
        editor.apply();
    }

    public String getSavingAccount(){
        return sharedPreferences.getString(KEY_SAVINGS, null);
    }

    // 🔹 Save Current Account
    public void saveCurrentAccount(String accountNumber) {
        editor.putString(KEY_CURRENT, accountNumber);
        editor.apply();
    }

    public String getCurrentAccount(){
        return sharedPreferences.getString(KEY_CURRENT, null);
    }

    // 🔹 Save Fixed Deposit Account
    public void saveFixedDepositAccount(String accountNumber) {
        editor.putString(KEY_FIXED, accountNumber);
        editor.apply();
    }

    public String getFixedDepositAccount(){
        return sharedPreferences.getString(KEY_SAVINGS, null);
    }

    // 🔹 Save Default Account
    public void saveDefaultAccount(String accountType) {
        editor.putString(KEY_DEFAULT, accountType);
        editor.apply();
    }

    public String getDefaultAccount(){
        return sharedPreferences.getString(KEY_DEFAULT, null);
    }

    // 🔹 Save Notification List
    public void saveNotificationList(List<NotificationModel> notifications) {
        String json = gson.toJson(notifications);
        editor.putString(KEY_NOTIFICATIONS, json);
        editor.apply();
    }

    // 🔹 Retrieve Notification List
    public List<NotificationModel> getNotificationList() {
        String json = sharedPreferences.getString(KEY_NOTIFICATIONS, null);
        Type type = new TypeToken<List<NotificationModel>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    public void saveUserEmail(String email) {
        editor.putString("user_email", email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("user_email", "user@example.com");
    }

    // 🔹 Save Transaction List
    public void saveRecentTransactionList(List<TransactionModel> transactions) {
        String json = gson.toJson(transactions);
        editor.putString(RECENT_TRANSACTION, json);
        editor.apply();
    }

    // 🔹 Retrieve Notification List
    public List<TransactionModel> getRecentTransactionList() {
        String json = sharedPreferences.getString(RECENT_TRANSACTION, null);
        Type type = new TypeToken<List<TransactionModel>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // 🔹 Clear JWT Token (Logout)
    public void clearJwtToken() {
        editor.remove(KEY_JWT_TOKEN);
        editor.apply();
    }

    // 🔹 Clear Transactions
    public void clearRecentTransactions() {
        editor.remove(RECENT_TRANSACTION);
        editor.apply();
    }

    // 🔹 Clear Notifications
    public void clearNotifications() {
        editor.remove(KEY_NOTIFICATIONS);
        editor.apply();
    }

    // 🔹 Clear All Data (Logout Completely)
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
