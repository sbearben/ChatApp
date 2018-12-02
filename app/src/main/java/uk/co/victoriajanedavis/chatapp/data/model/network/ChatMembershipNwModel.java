package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChatMembershipNwModel {

    @SerializedName("chat") @Expose private ChatNwModel chat;
    @SerializedName("other_user") @Expose private UserNwModel otherUser;


    public ChatMembershipNwModel(ChatNwModel chat) {
        this(chat, null);
    }

    public ChatMembershipNwModel(ChatNwModel chat, UserNwModel otherUser) {
        this.chat = chat;
        this.otherUser = otherUser;
    }

    public ChatNwModel getChat() {
        return chat;
    }

    public void setChat(ChatNwModel chat) {
        this.chat = chat;
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
        return this.getChat().getUuid().equals(((ChatMembershipNwModel) other).getChat().getUuid());
    }
}
