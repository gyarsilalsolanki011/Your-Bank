package com.gyarsilalsolanki011.bankingapp.core.models;

import com.gyarsilalsolanki011.bankingapp.core.enums.OnlineBankingStatus;

public class UserResponse {
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final OnlineBankingStatus onlineBankingStatus;

    public UserResponse(String name, String email, String phone, String address, OnlineBankingStatus onlineBankingStatus) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.onlineBankingStatus = onlineBankingStatus;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public OnlineBankingStatus getOnlineBankingStatus() {
        return onlineBankingStatus;
    }
}
