package com.example.employeeondemand.Models;

public class MessageData {

    private String messageId, messageText, senderId, imageUrl,receiverRoom;
    private long timestamp;
    private boolean isSeen;

    public MessageData(String messageText, String senderId, long timestamp,String receiverRoom,boolean isSeen) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.receiverRoom = receiverRoom;
        this.isSeen = isSeen;
    }

    public MessageData() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReceiverRoom() {
        return receiverRoom;
    }

    public void setReceiverRoom(String receiverRoom) {
        this.receiverRoom = receiverRoom;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
