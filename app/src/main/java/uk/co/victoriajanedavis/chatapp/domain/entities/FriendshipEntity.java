package uk.co.victoriajanedavis.chatapp.domain.entities;

import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class FriendshipEntity {

    @NonNull private UUID uuid;
    private String username;
    private String email;
    private boolean friendshipAccepted;
    private @Nullable Boolean sentFromCurrentUser = null;
    private UUID chatUuid;


    public FriendshipEntity() {};

    public FriendshipEntity(UUID uuid) {
        this.uuid = uuid;
    };

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
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

    public boolean isFriendshipAccepted() {
        return friendshipAccepted;
    }

    public void setFriendshipAccepted(boolean friendshipAccepted) {
        this.friendshipAccepted = friendshipAccepted;
    }

    @Nullable
    public Boolean isSentFromCurrentUser() {
        return sentFromCurrentUser;
    }

    public void setSentFromCurrentUser(@Nullable Boolean sentFromCurrentUser) {
        this.sentFromCurrentUser = sentFromCurrentUser;
    }

    public UUID getChatUuid() {
        return chatUuid;
    }

    public void setChatUuid(UUID chatUuid) {
        this.chatUuid = chatUuid;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof FriendshipEntity))return false;
        return this.getUuid().equals(((FriendshipEntity) other).getUuid());
    }

}
