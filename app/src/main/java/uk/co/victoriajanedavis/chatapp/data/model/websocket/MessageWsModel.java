package uk.co.victoriajanedavis.chatapp.data.model.websocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class MessageWsModel {

    @SerializedName("uuid") @Expose private UUID uuid;
    @SerializedName("chat_uuid") @Expose private UUID chat_uuid;
    @SerializedName("sender_uuid") @Expose private UUID sender_uuid;
    @SerializedName("sender_username") @Expose private String sender_username;
    @SerializedName("date") @Expose private Date date;
    @SerializedName("message") @Expose private String message;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getChatUuid() {
        return chat_uuid;
    }

    public void setChatUuid(UUID chat_uuid) {
        this.chat_uuid = chat_uuid;
    }

    public UUID getSenderUuid() {
        return sender_uuid;
    }

    public void setSenderUuid(UUID sender_uuid) {
        this.sender_uuid = sender_uuid;
    }

    public String getSenderUsername() {
        return sender_username;
    }

    public void setSenderUsername(String sender_username) {
        this.sender_username = sender_username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
