package uk.co.victoriajanedavis.chatapp.data.model.sharedpref;

import java.util.UUID;


public class TokenSpModel {

    public static final String PREF_TOKEN_KEY = "tokenKey";
    public static final String PREF_USER_UUID_KEY = "userUuidKey";
    public static final String PREF_USER_USERNAME_KEY = "userUsernameKey";
    public static final String PREF_USER_EMAIL_KEY = "userEmailKey";

    private String token;
    private UUID userUuid;
    private String userUsername;
    private String userEmail;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
