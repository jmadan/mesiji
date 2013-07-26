package com.thirtysix.serendip.model;

public class Message {

    public String message;
    public String userId;
    public String parentMessageId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentMessageId() {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
    }

    public Message(String msg, String userId){
        this.message = msg;
        this.userId = userId;
    }
}
