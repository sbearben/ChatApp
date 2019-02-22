package uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import javax.inject.Inject

@PerChildFragment
class ChatsAdapter @Inject constructor(
    private val actionLiveData: MutableLiveData<ChatItemAction>
) : ListAdapter<ChatEntity, ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context), parent, actionLiveData)
    }

    override fun onBindViewHolder(viewHolder: ChatViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    companion object {
        class ChatDiffCallback : DiffUtil.ItemCallback<ChatEntity>() {
            override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
                return oldItem.lastMessage.uuid == newItem.lastMessage.uuid
            }
        }
    }
}