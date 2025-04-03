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

    // 🔹 Save JWT Token
    public void saveJwtToken(String token) {
        editor.putString(KEY_JWT_TOKEN, "Bearer " + token);
        editor.apply();
    }

    // 🔹 Retrieve JWT Token
    public String getJwtToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
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

    /*// 🔹 Save Transaction List
    public void saveTransactionList(List<TransactionModel> transactions) {
        String json = gson.toJson(transactions);
        editor.putString(KEY_TRANSACTIONS, json);
        editor.apply();
    }

    // 🔹 Retrieve Notification List
    public List<TransactionModel> getTransactionList() {
        String json = sharedPreferences.getString(KEY_TRANSACTIONS, null);
        Type type = new TypeToken<List<TransactionModel>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }*/

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
