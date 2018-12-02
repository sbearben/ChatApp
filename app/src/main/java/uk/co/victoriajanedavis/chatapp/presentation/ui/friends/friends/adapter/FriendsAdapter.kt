package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter

import android.arch.lifecycle.MutableLiveData
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import javax.inject.Inject

@PerChildFragment
class FriendsAdapter @Inject constructor(private val actionLiveData: MutableLiveData<FriendAction>)
    : ListAdapter<ChatEntity, FriendViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(LayoutInflater.from(parent.context), parent, actionLiveData)
    }

    override fun onBindViewHolder(viewHolder: FriendViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    companion object {
        class ChatDiffCallback : DiffUtil.ItemCallback<ChatEntity>() {
            override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return (oldItem.uuid == newItem.uuid) && (oldItem.lastMessageDate == newItem.lastMessageDate)
            }
        }
    }
}