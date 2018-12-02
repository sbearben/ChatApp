package uk.co.victoriajanedavis.chatapp.presentation.ui.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_message_received.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class ReceivedMessageViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder<MessageEntity>(layoutInflater.inflate(R.layout.item_message_received, parent, false)) {

    private lateinit var messageEntity: MessageEntity


    override fun bind(item: MessageEntity) {
        messageEntity = item
        message_received_textview.text = messageEntity.text
        friend_icon_textview.text = messageEntity.userUsername.get(0).toString()
    }
}