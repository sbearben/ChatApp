package uk.co.victoriajanedavis.chatapp.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "chat_memberships")
public class ChatDbModel {

    @PrimaryKey @ColumnInfo(name = "chat_uuid") @NonNull private UUID uuid;
    @ColumnInfo(name = "last_message_text") private @Nullable String lastMessageText;
    @ColumnInfo(name = "last_message_from_current_user") @Nullable private Boolean lastMessageFromCurrentUser;
    @ColumnInfo(name = "last_message_date") private @Nullable Date lastMessageDate;
    @Ignore private FriendshipDbModel friendship;
    @Ignore private List<MessageDbModel> messages;


    public ChatDbModel() {
        this.messages = new ArrayList<>();
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

    public FriendshipDbModel getFriendship() {
        return friendship;
    }

    public void setFriendship(FriendshipDbModel friendship) {
        this.friendship = friendship;
    }

    public List<MessageDbModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDbModel> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChatDbModel))return false;
        return this.getUuid().equals(((ChatDbModel) other).getUuid());
    }
}
