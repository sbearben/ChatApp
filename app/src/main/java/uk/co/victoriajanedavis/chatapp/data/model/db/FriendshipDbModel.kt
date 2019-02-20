package uk.co.victoriajanedavis.chatapp.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.UUID

import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.NONE

@Entity(tableName = "friendships")
data class FriendshipDbModel (
    @PrimaryKey var uuid: UUID,
    var username: String,
    var email: String,
    @ColumnInfo(name = "friendship_accepted") var isFriendshipAccepted: Boolean = false,
    @ColumnInfo(name = "sent_from_current_user") var isSentFromCurrentUser: Boolean? = null,  // TODO: potential bug with TypeConverter here
    @ColumnInfo(name = "chat_uuid") var chatUuid: UUID? = null,
    @ColumnInfo(name = "timestamp") var timestamp: Long = TimestampProvider.currentTimeMillis(),
    // Used for FriendRequests if user clicked one of Accept/Reject/Cancel and we're in middle of network call
    @ColumnInfo(name = "loading_state") var loadingState: FriendshipLoadingState = NONE
)
