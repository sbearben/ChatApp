package uk.co.victoriajanedavis.chatapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import java.util.Date
import java.util.UUID

import androidx.room.ForeignKey.CASCADE
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider

@Entity(
    tableName = "messages",
    foreignKeys = arrayOf(ForeignKey(
        entity = ChatDbModel::class,
        parentColumns = arrayOf("uuid"),
        childColumns = arrayOf("chat_uuid"),
        onDelete = CASCADE
    )),
    indices = arrayOf(Index(value = ["chat_uuid"]))
)
data class MessageDbModel (
    @PrimaryKey var uuid: UUID,
    var text: String,
    var created: Date,
    @ColumnInfo(name = "chat_uuid") var chatUuid: UUID,
    @ColumnInfo(name = "user_uuid") var userUuid: UUID,
    @ColumnInfo(name = "user_username") var userUsername: String,
    @ColumnInfo(name = "from_current_user") var isFromCurrentUser: Boolean,
    @ColumnInfo(name = "timestamp") var timestamp: Long = TimestampProvider.currentTimeMillis()
)
