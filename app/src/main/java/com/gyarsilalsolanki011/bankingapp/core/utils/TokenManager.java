package com.gyarsilalsolanki011.bankingapp.core.utils;

import android.content.Context;
import android.util.Base64;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

public class TokenManager {
    // Method to check if token is expired
    public static boolean isTokenExpired(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer "
            }

            String[] split = token.split("\\.");
            String payload = new String(Base64.decode(split[1], Base64.DEFAULT), StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(payload);
            long exp = jsonObject.getLong("exp");  // Get expiration time
            long currentTime = System.currentTimeMillis() / 1000;  // Convert to seconds

            return currentTime > exp;  // Returns true if expired
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Treat as expired if decoding fails
        }
    }

    // Method to get saved token
    public static String getSavedToken(Context context) {
        return AppSharedPreferenceManager.getInstance(context).getJwtToken();
    }

    // Method to check if user is logged in
    public static boolean isUserLoggedIn(Context context) {
        String token = getSavedToken(context);
        return token != null && !isTokenExpired(token);  // Check token exists & is not expired
    }

    // Method to log out user
    public static void logoutUser(Context context) {
        AppSharedPreferenceManager.getInstance(context).clearJwtToken();
    }

}
