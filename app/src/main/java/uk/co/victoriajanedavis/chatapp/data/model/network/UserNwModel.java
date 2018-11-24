package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;


public class UserNwModel {

    @SerializedName("uuid") @Expose private UUID uuid;
    @SerializedName("username") @Expose private String username;
    @SerializedName("email") @Expose private String email;


    public UserNwModel() {
    }

    public UserNwModel(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof UserNwModel))return false;
        return this.getUuid().equals(((UserNwModel) other).getUuid());
    }
}
