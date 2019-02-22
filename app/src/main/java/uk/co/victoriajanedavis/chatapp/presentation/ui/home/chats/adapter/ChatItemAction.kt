package uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter

import android.widget.TextView
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity

sealed class ChatItemAction {
    data class Clicked(val chatEntity: ChatEntity, val sharedTextView: TextView) : ChatItemAction()
}