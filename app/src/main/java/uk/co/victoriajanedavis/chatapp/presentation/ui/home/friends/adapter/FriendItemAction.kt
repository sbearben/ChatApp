package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter

import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

sealed class FriendItemAction {
    data class Clicked(val friendEntity: FriendshipEntity) : FriendItemAction()
}