package uk.co.victoriajanedavis.chatapp.domain.entities;

import java.util.Date;
import java.util.UUID;

import io.reactivex.annotations.NonNull;

public class MessageEntity {

    @NonNull private UUID uuid;
    private UUID userUuid;
    private String userUsername;
    private String text;
    private Date created;
    private UUID chatUuid;
    private boolean fromCurrentUser;


    public MessageEntity() {};

    public MessageEntity(@NonNull UUID uuid) {
        this.uuid = uuid;
    };

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
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
        if (!(other instanceof MessageEntity))return false;
        return this.getUuid().equals(((MessageEntity) other).getUuid());
    }
}
