package com.example.employeeondemand.Models;

public class CallListData {
    String userId, type;

    public CallListData(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

    public CallListData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
