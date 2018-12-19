package uk.co.victoriajanedavis.chatapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import java.util.UUID

import androidx.room.ForeignKey.CASCADE


@Entity(
    tableName = "friendships",
    foreignKeys = arrayOf(ForeignKey(
        entity = ChatDbModel::class,
        parentColumns = arrayOf("uuid"),
        childColumns = arrayOf("chat_uuid"),
        onDelete = CASCADE
    )),
    indices = arrayOf(Index(value = ["chat_uuid"]))
)
data class FriendshipDbModel (

    @PrimaryKey var uuid: UUID,
    var username: String,
    var email: String,
    @ColumnInfo(name = "friendship_accepted") var isFriendshipAccepted: Boolean = false,
    @ColumnInfo(name = "sent_from_current_user") var isSentFromCurrentUser: Boolean? = null,  // TODO: potential bug with TypeConverter here
    @ColumnInfo(name = "chat_uuid") var chatUuid: UUID? = null
)

    /*
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        return if (other !is FriendshipDbModel) false else this.uuid == other.uuid
    }
    */
