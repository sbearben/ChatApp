package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.UUID

import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.NONE

data class FriendshipEntity(
    val uuid: UUID? = null,
    val username: String = "",
    val email: String = "",
    val isFriendshipAccepted: Boolean = false,
    val isSentFromCurrentUser: Boolean? = null,
    val chatUuid: UUID? = null,
    val loadingState: FriendshipLoadingState = NONE
)
