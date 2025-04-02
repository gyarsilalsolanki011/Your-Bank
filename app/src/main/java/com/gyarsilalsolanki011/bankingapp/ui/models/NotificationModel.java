package com.gyarsilalsolanki011.bankingapp.ui.models;

import java.util.Date;

public class NotificationModel {
    private final String title;
    private final String message;
    private final Date time;

    public NotificationModel(String title, String message, Date time) {
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Date getTime() { return time; }

}
