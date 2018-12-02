package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatNwModel {

    @SerializedName("uuid") @Expose private UUID uuid;
    @SerializedName("messages") @Expose private List<MessageNwModel> messages;


    public ChatNwModel(UUID uuid) {
        this.uuid = uuid;
        this.messages = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<MessageNwModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageNwModel> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChatNwModel))return false;
        return this.getUuid().equals(((ChatNwModel) other).getUuid());
    }
}
