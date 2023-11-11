package com.example.employeeondemand.Models;

public class ListData {
    String userId;

    public ListData(String userId) {
        this.userId = userId;
    }

    public ListData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
