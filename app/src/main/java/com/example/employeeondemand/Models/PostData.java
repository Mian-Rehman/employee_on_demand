package com.example.employeeondemand.Models;

public class PostData {

    String postId, serviceBudget, serviceName, serviceTime, serviceWorkplace, userCity, userId;

    public PostData() {

    }

    public PostData(String postId, String serviceBudget, String serviceName, String serviceTime, String serviceWorkplace, String userCity, String userId) {
        this.postId = postId;
        this.serviceBudget = serviceBudget;
        this.serviceName = serviceName;
        this.serviceTime = serviceTime;
        this.serviceWorkplace = serviceWorkplace;
        this.userCity = userCity;
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getServiceBudget() {
        return serviceBudget;
    }

    public void setServiceBudget(String serviceBudget) {
        this.serviceBudget = serviceBudget;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceWorkplace() {
        return serviceWorkplace;
    }

    public void setServiceWorkplace(String serviceWorkplace) {
        this.serviceWorkplace = serviceWorkplace;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
