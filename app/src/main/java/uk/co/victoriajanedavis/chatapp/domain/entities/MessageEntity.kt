package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.Date
import java.util.UUID

import io.reactivex.annotations.NonNull

data class MessageEntity(
    val uuid: UUID,
    var userUuid: UUID? = null,
    var userUsername: String = "",
    var text: String = "",
    var created: Date? = null,
    var chatUuid: UUID? = null,
    var isFromCurrentUser: Boolean = false
)
