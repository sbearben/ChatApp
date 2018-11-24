package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;


public class ChatMembershipNwModel {

    @SerializedName("chat_uuid") @Expose private UUID chatUuid;
    @SerializedName("other_user") @Expose private UserNwModel otherUser;


    public ChatMembershipNwModel(UUID chatUuid) {
        this(chatUuid, null);
    }

    public ChatMembershipNwModel(UUID chatUuid, UserNwModel otherUser) {
        this.chatUuid = chatUuid;
        this.otherUser = otherUser;
    }

    public UUID getChatUuid() {
        return chatUuid;
    }

    public void setChatUuid(UUID chatUuid) {
        this.chatUuid = chatUuid;
    }

    public UserNwModel getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(UserNwModel otherUser) {
        this.otherUser = otherUser;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChatMembershipNwModel))return false;
        return this.getChatUuid().equals(((ChatMembershipNwModel) other).getChatUuid());
    }
}
