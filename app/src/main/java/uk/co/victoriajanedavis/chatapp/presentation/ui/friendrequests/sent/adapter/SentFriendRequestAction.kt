package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

sealed class SentFriendRequestAction {
    data class Cancel(val friendshipEntity: FriendshipEntity) : SentFriendRequestAction()
}