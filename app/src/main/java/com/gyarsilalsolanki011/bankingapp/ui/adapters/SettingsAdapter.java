package com.gyarsilalsolanki011.bankingapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gyarsilalsolanki011.bankingapp.R;
import com.gyarsilalsolanki011.bankingapp.core.utils.TokenManager;
import com.gyarsilalsolanki011.bankingapp.ui.activities.LoginActivity;
import com.gyarsilalsolanki011.bankingapp.ui.activities.NotificationActivity;
import com.gyarsilalsolanki011.bankingapp.ui.models.SettingsItem;

import java.util.List;

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
            if (item.getTitle().equals("Logout")) {
                TokenManager.logoutUser(context);
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                Intent intent = new Intent(context, NotificationActivity.class);
                context.startActivity(intent);
            }
        });
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
}
