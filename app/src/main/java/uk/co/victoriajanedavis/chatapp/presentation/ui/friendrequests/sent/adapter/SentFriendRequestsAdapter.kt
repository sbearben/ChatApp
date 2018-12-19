package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import javax.inject.Inject

@PerChildFragment
class SentFriendRequestsAdapter @Inject constructor(
    private val actionLiveData: MutableLiveData<SentFriendRequestAction>
) : ListAdapter<FriendshipEntity, SentFriendRequestViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentFriendRequestViewHolder {
        return SentFriendRequestViewHolder(LayoutInflater.from(parent.context), parent, actionLiveData)
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
                return oldItem.uuid == newItem.uuid
            }
        }
    }
}