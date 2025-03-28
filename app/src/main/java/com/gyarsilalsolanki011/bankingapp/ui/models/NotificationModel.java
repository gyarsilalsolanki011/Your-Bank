package com.gyarsilalsolanki011.bankingapp.ui.models;

public class NotificationModel {
    private final String title;
    private final String message;
    private final String time;

    public NotificationModel(String title, String message, String time) {
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTime() { return time; }

}
