package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import javax.inject.Inject

class FriendsAdapter @Inject constructor(
    private val clickListener: FriendViewHolder.OnClickListener
) : ListAdapter<FriendshipEntity, FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(LayoutInflater.from(parent.context), parent, clickListener)
    }

    override fun onBindViewHolder(viewHolder: FriendViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    companion object {
        class FriendDiffCallback : DiffUtil.ItemCallback<FriendshipEntity>() {
            override fun areItemsTheSame(oldItem: FriendshipEntity, newItem: FriendshipEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: FriendshipEntity, newItem: FriendshipEntity): Boolean {
                return oldItem.username == newItem.username
            }
        }
    }
}