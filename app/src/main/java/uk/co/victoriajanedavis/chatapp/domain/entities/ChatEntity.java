package uk.co.victoriajanedavis.chatapp.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ChatEntity {

    @NonNull private UUID uuid;
    @Nullable private String lastMessageText;
    @Nullable private Boolean lastMessageFromCurrentUser;
    @Nullable private Date lastMessageDate;
    @Nullable private FriendshipEntity friendship;
    //@Nullable private List<MessageEntity> messages;


    public ChatEntity() {}

    public ChatEntity(UUID uuid) {
        this.uuid = uuid;
    }

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
    }

    @Nullable
    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(@Nullable String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    @Nullable
    public Boolean isLastMessageFromCurrentUser() {
        return lastMessageFromCurrentUser;
    }

    public void setLastMessageFromCurrentUser(@Nullable Boolean lastMessageFromCurrentUser) {
        this.lastMessageFromCurrentUser = lastMessageFromCurrentUser;
    }

    @Nullable
    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(@Nullable Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    @Nullable
    public FriendshipEntity getFriendship() {
        return friendship;
    }

    public void setFriendship(FriendshipEntity friendship) {
        this.friendship = friendship;
    }

    /*

    @Nullable
    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
    */

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChatEntity))return false;
        return this.getUuid().equals(((ChatEntity) other).getUuid());
    }

}
