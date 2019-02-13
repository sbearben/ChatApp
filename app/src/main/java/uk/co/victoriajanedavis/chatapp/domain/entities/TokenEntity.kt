package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.UUID

data class TokenEntity(
    val token: String,
    val userUuid: UUID?,
    val userUsername: String,
    val userEmail: String
) {
    val isEmpty: Boolean
        get() = token.isEmpty()
}
