package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter

import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

sealed class ReceivedFriendRequestAction {
    data class Accept(val friendshipEntity: FriendshipEntity) : ReceivedFriendRequestAction()
    data class Reject(val friendshipEntity: FriendshipEntity) : ReceivedFriendRequestAction()
}