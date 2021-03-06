package com.chat;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Technovibe on 17-04-2015.
 */
public class ChatMessage {
    private String id;
    private boolean isMe;
    private String message;
    private String chatId;
    private String dateTime;
    private String specialType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getchatId() {
        return chatId;
    }

    public void setchatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }
}
