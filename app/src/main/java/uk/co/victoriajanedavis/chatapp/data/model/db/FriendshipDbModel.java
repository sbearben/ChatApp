package uk.co.victoriajanedavis.chatapp.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "friendships",
        foreignKeys = @ForeignKey(
                entity = ChatDbModel.class,
                parentColumns = "chat_uuid",
                childColumns = "chat_uuid",
                onDelete = CASCADE),
        indices = { @Index(value = "chat_uuid")})
public class FriendshipDbModel {

    @PrimaryKey @NonNull private UUID uuid;
    private String username;
    private String email;
    @ColumnInfo(name = "friendship_accepted") private boolean friendshipAccepted;
    @ColumnInfo(name = "sent_from_current_user") @Nullable private Boolean sentFromCurrentUser = null; //TODO: potential bug with TypeConverter here
    @ColumnInfo(name = "chat_uuid") private UUID chatUuid;


    public FriendshipDbModel() {};

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
        if (!(other instanceof FriendshipDbModel))return false;
        return this.getUuid().equals(((FriendshipDbModel) other).getUuid());
    }
}
