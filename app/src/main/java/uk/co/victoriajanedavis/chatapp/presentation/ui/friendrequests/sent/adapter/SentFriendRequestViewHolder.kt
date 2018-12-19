package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import androidx.lifecycle.MutableLiveData
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class SentFriendRequestViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val friendActionLiveData: MutableLiveData<SentFriendRequestAction>
) : BaseViewHolder<FriendshipEntity>(layoutInflater.inflate(R.layout.item_sent_friend_request, parent, false)) {

    private lateinit var friendshipEntity: FriendshipEntity

    init {
        itemView.setOnClickListener {
            friendActionLiveData.value = SentFriendRequestAction.Cancel(friendshipEntity)
        }
    }

    override fun bind(item: FriendshipEntity) {
        friendshipEntity = item
    }
}