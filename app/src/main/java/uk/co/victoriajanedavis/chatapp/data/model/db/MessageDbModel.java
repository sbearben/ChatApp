package uk.co.victoriajanedavis.chatapp.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "messages",
        foreignKeys = @ForeignKey(
                entity = ChatDbModel.class,
                parentColumns = "chat_uuid",
                childColumns = "chat_uuid",
                onDelete = CASCADE),
        indices = { @Index(value = "chat_uuid")})
public class MessageDbModel {

    @PrimaryKey @NonNull private UUID uuid;
    private String text;
    private Date created;
    @ColumnInfo(name = "chat_uuid") private UUID chatUuid;
    @ColumnInfo(name = "user_uuid") private UUID userUuid;
    @ColumnInfo(name = "user_username") private String userUsername;
    @ColumnInfo(name = "from_current_user") private boolean fromCurrentUser;


    public MessageDbModel() {};

    @NonNull
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
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
        if (!(other instanceof MessageDbModel))return false;
        return this.getUuid().equals(((MessageDbModel) other).getUuid());
    }
}
