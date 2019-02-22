package uk.co.victoriajanedavis.chatapp.presentation.ui.chat.adapter

import android.os.Handler

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder
import uk.co.victoriajanedavis.chatapp.presentation.common.LoadingViewHolder
import javax.inject.Inject

@PerFragment
class MessagesAdapter @Inject  constructor()
    : ListAdapter<MessageEntity, BaseViewHolder<MessageEntity>>(MessageDiffCallback()) {

    private val handler: Handler = Handler()
    private var loadingMore: Boolean = false

    override fun getItemViewType(position: Int): Int {
        if (loadingMore && position == itemCount-1) return VIEW_TYPE_LOADING
        else if (getItem(position).isFromCurrentUser) return VIEW_TYPE_MESSAGE_SENT
        else return VIEW_TYPE_MESSAGE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageEntity> {
        return when(viewType) {
            VIEW_TYPE_MESSAGE_RECEIVED -> ReceivedMessageViewHolder(LayoutInflater.from(parent.context), parent)
            VIEW_TYPE_MESSAGE_SENT -> SentMessageViewHolder(LayoutInflater.from(parent.context), parent)
            else -> LoadingViewHolder(LayoutInflater.from(parent.context), parent)
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder<MessageEntity>, position: Int) {
        if (viewHolder.itemViewType == VIEW_TYPE_LOADING) {
            return
        }
        viewHolder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if(loadingMore) (count+1) else count
    }

    fun addLoadingView() {
        if (!isLoadingViewAdded()) {
            handlerPost {
                loadingMore = true
                notifyItemInserted(itemCount - 1)
            }
            Log.d("ChatFragment", "added loading view")
        }

    }

    fun removeLoadingView() {
        if (loadingMore && isLoadingViewAdded()) {
            handlerPost {
                notifyItemRemoved(itemCount-1)
                loadingMore = false
            }
            Log.d("ChatFragment", "removed loading view")
        }
    }

    fun isLoadingViewAdded(): Boolean {
        return getItemViewType(itemCount-1) == VIEW_TYPE_LOADING
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    private fun handlerPost(task: () -> Unit) {
        val r = Runnable(task)
        handler.post(r)
    }

    companion object {
        const val VIEW_TYPE_MESSAGE_RECEIVED = 0
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_LOADING = 2

        class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
            override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
                return (oldItem.created == newItem.created) && (oldItem.text == newItem.text)
            }
        }
    }
}