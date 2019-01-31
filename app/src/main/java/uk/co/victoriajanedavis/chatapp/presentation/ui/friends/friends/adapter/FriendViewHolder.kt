package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.item_friend.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class FriendViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val friendActionLiveData: MutableLiveData<FriendAction>
) : BaseViewHolder<ChatEntity>(layoutInflater.inflate(R.layout.item_friend, parent, false)) {

    private lateinit var chatEntity: ChatEntity

    init {
        itemView.setOnClickListener {
            friendActionLiveData.value = FriendAction.Clicked(
                chatEntity = chatEntity,
                sharedTextView = nameTextView
            )
        }
    }

    override fun bind(item: ChatEntity) {
        chatEntity = item
        iconTextView.text = item.friendship?.username?.get(0).toString()
        nameTextView.text = item.friendship?.username
        lastMessageTextView.text = item.lastMessageText
        dateTextView.text = item.lastMessageDate?.toString()

        ViewCompat.setTransitionName(nameTextView, chatEntity.uuid.toString());
    }
}