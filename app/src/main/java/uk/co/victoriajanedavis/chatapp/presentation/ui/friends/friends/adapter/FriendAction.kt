package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import android.widget.TextView
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity

sealed class FriendAction {
    data class Clicked(val chatEntity: ChatEntity, val sharedTextView: TextView) : FriendAction()
}