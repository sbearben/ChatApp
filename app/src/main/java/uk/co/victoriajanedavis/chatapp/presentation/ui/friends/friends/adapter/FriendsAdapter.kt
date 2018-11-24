package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import android.arch.lifecycle.MutableLiveData
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity
import javax.inject.Inject

class FriendsAdapter @Inject constructor(private val actionLiveData: MutableLiveData<Int>)
    : ListAdapter<ChatMembershipEntity, FriendViewHolder>(ChatMembershipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(LayoutInflater.from(parent.context), parent, actionLiveData)
    }

    override fun onBindViewHolder(viewHolder: FriendViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }


    companion object {
        class ChatMembershipDiffCallback : DiffUtil.ItemCallback<ChatMembershipEntity>() {
            override fun areItemsTheSame(oldItem: ChatMembershipEntity, newItem: ChatMembershipEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: ChatMembershipEntity, newItem: ChatMembershipEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }
        }
    }
}