package com.example.employeeondemand.Models;

public class FeedbackData {
    String senderId, rating, comment;

    public FeedbackData(String senderId, String rating, String comment) {
        this.senderId = senderId;
        this.rating = rating;
        this.comment = comment;
    }

    public FeedbackData() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
