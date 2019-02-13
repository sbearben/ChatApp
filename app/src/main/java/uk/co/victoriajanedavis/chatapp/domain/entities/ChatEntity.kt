package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.Date
import java.util.UUID

import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable

data class ChatEntity (
    val uuid: UUID? = null,
    val lastMessageText: String = "",
    val isLastMessageFromCurrentUser: Boolean? = null,
    val lastMessageDate: Date? = null,
    var friendship: FriendshipEntity? = null
    //@Nullable private List<MessageEntity> messages;
)
