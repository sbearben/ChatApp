package uk.co.victoriajanedavis.chatapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import java.util.Date
import java.util.UUID

import androidx.room.ForeignKey.CASCADE


@Entity(
    tableName = "messages",
    foreignKeys = arrayOf(ForeignKey(
        entity = ChatDbModel::class,
        parentColumns = arrayOf("chat_uuid"),
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
    @ColumnInfo(name = "from_current_user") var isFromCurrentUser: Boolean
)

    /*
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        return if (other !is MessageDbModel) false else this.uuid == other.uuid
    }
    */
