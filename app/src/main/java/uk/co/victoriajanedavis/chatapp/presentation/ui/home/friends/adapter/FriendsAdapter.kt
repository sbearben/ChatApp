package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import javax.inject.Inject

@PerChildFragment
class FriendsAdapter @Inject constructor(
    private val actionLiveData: MutableLiveData<FriendItemAction>
) : ListAdapter<FriendshipEntity, FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(LayoutInflater.from(parent.context), parent, actionLiveData)
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