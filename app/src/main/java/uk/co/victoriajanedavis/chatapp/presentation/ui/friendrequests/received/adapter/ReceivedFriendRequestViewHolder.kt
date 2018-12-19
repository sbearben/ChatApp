package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter

import androidx.lifecycle.MutableLiveData
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class ReceivedFriendRequestViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val friendActionLiveData: MutableLiveData<ReceivedFriendRequestAction>
) : BaseViewHolder<FriendshipEntity>(layoutInflater.inflate(R.layout.item_received_friend_request, parent, false)) {

    private lateinit var friendshipEntity: FriendshipEntity

    init {
        itemView.setOnClickListener {
            friendActionLiveData.value = ReceivedFriendRequestAction.Reject(friendshipEntity)
        }
    }

    override fun bind(item: FriendshipEntity) {
        friendshipEntity = item
    }
}