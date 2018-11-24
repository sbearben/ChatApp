package uk.co.victoriajanedavis.chatapp.data.model.db.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.UUID;

// TODO: need to potentially do something about the fact that chat_uuid can sometimes be null in FriendshipsDao
public class UuidConverter {
    @TypeConverter
    public static UUID toUuid(String value) {
        return value != null ? UUID.fromString(value) : null;
    }

    @TypeConverter
    public static String toString(UUID value) {
        return value != null ? value.toString() : null;
    }
}
