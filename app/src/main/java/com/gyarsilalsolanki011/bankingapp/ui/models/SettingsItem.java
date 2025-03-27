package com.gyarsilalsolanki011.bankingapp.ui.models;

public class SettingsItem {
    private final int icon;
    private final String title;
    private final String description;

    public SettingsItem(int icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public int getIcon() { return icon; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
