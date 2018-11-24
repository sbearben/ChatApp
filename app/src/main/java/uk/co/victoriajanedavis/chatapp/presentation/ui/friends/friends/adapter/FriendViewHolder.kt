package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import android.arch.lifecycle.MutableLiveData
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class FriendViewHolder(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup,
    private val actionLiveData: MutableLiveData<Int>
) : BaseViewHolder<ChatMembershipEntity>(layoutInflater.inflate(R.layout.item_friend, parent, false)) {

    override fun bind(item: ChatMembershipEntity) {

    }
}