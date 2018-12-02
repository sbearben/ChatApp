package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity

sealed class FriendAction {
    data class Clicked(val chatEntity: ChatEntity) : FriendAction()
}