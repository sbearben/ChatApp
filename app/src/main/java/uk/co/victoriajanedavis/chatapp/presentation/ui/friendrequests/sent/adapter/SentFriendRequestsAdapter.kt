package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import javax.inject.Inject

class SentFriendRequestsAdapter @Inject constructor(
    private val clickListener: SentFriendRequestViewHolder.OnClickListener
) : ListAdapter<FriendshipEntity, SentFriendRequestViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentFriendRequestViewHolder {
        return SentFriendRequestViewHolder(LayoutInflater.from(parent.context), parent, clickListener)
    }

    override fun onBindViewHolder(viewHolder: SentFriendRequestViewHolder, position: Int) {
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