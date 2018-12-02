package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;


public class MessageNwModel {

    @SerializedName("uuid") @Expose private UUID uuid;
    @SerializedName("text") @Expose private String text;
    @SerializedName("created") @Expose private Date created;
    @SerializedName("chat_uuid") @Expose private UUID chatUuid;
    @SerializedName("user_uuid") @Expose private UUID userUuid;
    @SerializedName("user_username") @Expose private String userUsername;
    @SerializedName("from_current_user") @Expose private boolean fromCurrentUser;


    public MessageNwModel(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public UUID getChatUuid() {
        return chatUuid;
    }

    public void setChatUuid(UUID chatUuid) {
        this.chatUuid = chatUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public boolean isFromCurrentUser() {
        return fromCurrentUser;
    }

    public void setFromCurrentUser(boolean fromCurrentUser) {
        this.fromCurrentUser = fromCurrentUser;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof MessageNwModel))return false;
        return this.getUuid().equals(((MessageNwModel) other).getUuid());
    }
}
