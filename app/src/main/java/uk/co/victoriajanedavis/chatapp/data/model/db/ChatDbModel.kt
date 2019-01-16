package uk.co.victoriajanedavis.chatapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider

import java.util.Date
import java.util.UUID
import kotlin.collections.ArrayList

@Entity(tableName = "chat_memberships")
data class ChatDbModel(
    @PrimaryKey var uuid: UUID,
    @ColumnInfo(name = "last_message_text") var lastMessageText: String = "",
    @ColumnInfo(name = "last_message_from_current_user") var isLastMessageFromCurrentUser: Boolean? = null,
    @ColumnInfo(name = "last_message_date") var lastMessageDate: Date? = null,
    @ColumnInfo(name = "timestamp") var timestamp: Long = TimestampProvider.currentTimeMillis(),
    @Ignore var friendship: FriendshipDbModel? = null,
    @Ignore var messages: MutableList<MessageDbModel>? = null
) {
    constructor(
        uuid: UUID,
        lastMessageText: String,
        isLastMessageFromCurrentUser: Boolean?,
        lastMessageDate: Date?
    ): this(
        uuid = uuid,
        lastMessageText = lastMessageText,
        isLastMessageFromCurrentUser = isLastMessageFromCurrentUser,
        lastMessageDate = lastMessageDate,
        friendship = null,
        messages = null)

    init {
        messages = ArrayList()
    }
}

/*
override fun equals(other: Any?): Boolean {
    if (other == null) return false
    if (other === this) return true
    return if (other !is ChatDbModel) false else this.uuid == other.uuid
}
 */
