package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.utils.AppSharedPreferenceManager;
import com.gyarsilalsolanki011.bankingapp.core.utils.TokenManager;
import com.gyarsilalsolanki011.bankingapp.ui.activities.ForgotPasswordActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.LoginActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.models.SettingsItem;

import java.util.List;
import java.util.Objects;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{
    private final List<SettingsItem> settingsList;
    private final Context context;

    public SettingsAdapter(List<SettingsItem> settingsList, Context context) {
        this.settingsList = settingsList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsItem item = settingsList.get(position);
        holder.icon.setImageResource(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        holder.itemView.setOnClickListener(v -> {
            switch (item.getTitle()) {
                case "Logout":
                    showLogoutDialog();
                    break;
                case "Security": {
                    Intent intent = new Intent(context, ForgotPasswordActivity.class);
                    context.startActivity(intent);
                    break;
                }
                case "Account":
                    setDefaultAccount();
                    break;
                case "Theme":
                    Toast.makeText(context, "Default Theme activated", Toast.LENGTH_SHORT).show();
                    break;
                default: {
                    Intent intent = new Intent(context, NotificationActivity.class);
                    context.startActivity(intent);
                    break;
                }
            }
        });
    }

    private void setDefaultAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.lay_account_setting, null);
        builder.setView(dialogView);

        // Create Dialog
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        MaterialButton setDefaultAccount = dialogView.findViewById(R.id.setDefaultAccount);

        setDefaultAccount.setOnClickListener(v -> {
            String accountType = selectYourField(dialogView);
            Toast.makeText(context, "You have selected "+accountType, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private String selectYourField(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        MaterialButton setDefaultAccount = view.findViewById(R.id.setDefaultAccount);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        setDefaultAccount.setVisibility(View.GONE);
        RadioButton radioButton = view.findViewById(selectedId);
        String accountType = radioButton.getText().toString();

        if (accountType.equals("Savings")){
            AppSharedPreferenceManager.getInstance(context).saveDefaultAccount("SAVINGS_ACCOUNT");
        } else if (accountType.equals("Current")){
            AppSharedPreferenceManager.getInstance(context).saveDefaultAccount("CURRENT_ACCOUNT");
        } else {
            AppSharedPreferenceManager.getInstance(context).saveDefaultAccount("FIXED_DEPOSIT");
        }
        return accountType;
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }

    // Logout Function
    private void logoutUser() {
        // Clear SharedPreferences
        TokenManager.logoutUser(context);

        // Redirect to Login Activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears activity stack
        context.startActivity(intent);

        // Finish Current Activity
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    // Show Confirmation Dialog
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);
        builder.setView(dialogView);

        // Initialize Dialog Views
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnLogout = dialogView.findViewById(R.id.btnLogout);

        // Create Dialog
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent Background
        dialog.show();

        // Handle Cancel Button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Handle Logout Button
        btnLogout.setOnClickListener(v -> {
            dialog.dismiss();
            logoutUser();
        });
    }
}
