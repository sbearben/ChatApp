package uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.item_chat.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class ChatViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val clickListener: OnClickListener
) : BaseViewHolder<ChatEntity>(layoutInflater.inflate(R.layout.item_chat, parent, false)) {

    private lateinit var chatEntity: ChatEntity

    init {
        itemView.setOnClickListener {
            clickListener.onChatClicked(
                chatEntity = chatEntity,
                sharedTextView = nameTextView
            )
        }
    }

    override fun bind(item: ChatEntity) {
        chatEntity = item
        iconTextView.text = item.friendship.username.get(0).toString()
        nameTextView.text = item.friendship.username
        val lastMessageText = (if(item.lastMessage.isFromCurrentUser) "You: " else "") + item.lastMessage.text
        lastMessageTextView.text = lastMessageText
        dateTextView.text = item.lastMessage.created.toString()

        ViewCompat.setTransitionName(nameTextView, chatEntity.uuid.toString())
    }

    interface OnClickListener {
        fun onChatClicked(chatEntity: ChatEntity, sharedTextView: TextView)
    }
}