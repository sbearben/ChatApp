package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter

import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import java.util.UUID

sealed class ReceivedFriendRequestAction {
    data class Accept(val senderUserUuid: UUID) : ReceivedFriendRequestAction()
    data class Reject(val senderUserUuid: UUID) : ReceivedFriendRequestAction()
}