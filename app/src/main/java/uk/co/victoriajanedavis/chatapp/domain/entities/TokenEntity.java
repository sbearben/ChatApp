package uk.co.victoriajanedavis.chatapp.domain.entities;

import java.util.Date;
import java.util.UUID;

import io.reactivex.annotations.NonNull;

public class TokenEntity {

    private String token;
    private UUID userUuid;
    private String userUsername;
    private String userEmail;


    public TokenEntity() {};

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

    public boolean isEmpty() {
        return token == null || token.isEmpty();
    }
}
