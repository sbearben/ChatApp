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
                entity = ChatMembershipDbModel.class,
                parentColumns = "chat_uuid",
                childColumns = "chat_uuid",
                onDelete = CASCADE),
        indices = { @Index(value = "chat_uuid")})
public class MessageDbModel {

    @PrimaryKey @NonNull private UUID uuid;
    @ColumnInfo(name = "user_uuid") private UUID userUuid;
    private String text;
    private Date created;
    @ColumnInfo(name = "chat_uuid") private UUID chatUuid;


    public MessageDbModel() {};

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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof MessageDbModel))return false;
        return this.getUuid().equals(((MessageDbModel) other).getUuid());
    }
}
