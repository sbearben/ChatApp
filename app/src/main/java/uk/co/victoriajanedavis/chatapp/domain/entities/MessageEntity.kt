package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.Date
import java.util.UUID

data class MessageEntity(
    val uuid: UUID,
    val userUuid: UUID,
    val userUsername: String = "",
    val text: String = "",
    val created: Date,
    val chatUuid: UUID,
    val isFromCurrentUser: Boolean
)
