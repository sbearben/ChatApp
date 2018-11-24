package uk.co.victoriajanedavis.chatapp.data.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TokenNwModel {

    @SerializedName("key") @Expose private String token;
    @SerializedName("user") @Expose private UserNwModel user;


    public TokenNwModel() {
        user = new UserNwModel();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserNwModel getUser() {
        return user;
    }

    public void setUser(UserNwModel user) {
        this.user = user;
    }
}
