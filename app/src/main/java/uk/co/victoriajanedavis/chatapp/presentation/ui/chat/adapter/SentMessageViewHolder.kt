package uk.co.victoriajanedavis.chatapp.presentation.ui.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_message_sent.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class SentMessageViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder<MessageEntity>(layoutInflater.inflate(R.layout.item_message_sent, parent, false)) {

    private lateinit var messageEntity: MessageEntity


    override fun bind(item: MessageEntity) {
        messageEntity = item
        message_sent_textview.text = messageEntity.text
    }
}