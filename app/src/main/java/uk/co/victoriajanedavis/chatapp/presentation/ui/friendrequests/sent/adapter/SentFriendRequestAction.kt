package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import java.util.UUID

sealed class SentFriendRequestAction {
    data class Cancel(val receiverUserUuid: UUID) : SentFriendRequestAction()
}