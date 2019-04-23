package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import javax.inject.Inject

class ReceivedFriendRequestsAdapter @Inject constructor(
    private val clickListener: ReceivedFriendRequestViewHolder.OnClickListener
) : ListAdapter<FriendshipEntity, ReceivedFriendRequestViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedFriendRequestViewHolder {
        return ReceivedFriendRequestViewHolder(LayoutInflater.from(parent.context), parent, clickListener)
    }

    override fun onBindViewHolder(viewHolder: ReceivedFriendRequestViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    companion object {
        class ChatDiffCallback : DiffUtil.ItemCallback<FriendshipEntity>() {
            override fun areItemsTheSame(oldItem: FriendshipEntity, newItem: FriendshipEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: FriendshipEntity, newItem: FriendshipEntity): Boolean {
                return oldItem.loadingState == newItem.loadingState
            }
        }
    }
}