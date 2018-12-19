package uk.co.victoriajanedavis.chatapp.data.model.db.converter

import androidx.room.TypeConverter

import java.util.UUID

// TODO: need to potentially do something about the fact that chat_uuid can sometimes be null in FriendshipsDao
class UuidConverter {
    @TypeConverter
    fun toUuid(value: String?): UUID? {
        return if (value != null) UUID.fromString(value) else null
    }

    @TypeConverter
    fun toString(value: UUID?): String? {
        return value?.toString()
    }
}
