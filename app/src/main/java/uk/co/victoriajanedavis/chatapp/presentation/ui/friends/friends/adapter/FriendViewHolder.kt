package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        itemView.setOnClickListener({ friendActionLiveData.value = FriendAction.Clicked(chatEntity) })
    }

    override fun bind(item: ChatEntity) {
        chatEntity = item
        item_friend_icon_textview.text = item.friendship?.username?.get(0).toString()
        item_friend_name_textview.text = item.friendship?.username
        item_friend_lastmsg_textview.text = item.lastMessageText
        item_friend_date_textview.text = item.lastMessageDate?.toString()
    }
}