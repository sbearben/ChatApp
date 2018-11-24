package uk.co.victoriajanedavis.chatapp.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

@Entity(tableName = "chat_memberships")
public class ChatMembershipDbModel {

    @PrimaryKey @ColumnInfo(name = "chat_uuid") @NonNull private UUID uuid;
    @Ignore private FriendshipDbModel friendship;
    @Ignore private List<MessageDbModel> messages;


    public ChatMembershipDbModel() {}

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
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
        if (!(other instanceof ChatMembershipDbModel))return false;
        return this.getUuid().equals(((ChatMembershipDbModel) other).getUuid());
    }
}
