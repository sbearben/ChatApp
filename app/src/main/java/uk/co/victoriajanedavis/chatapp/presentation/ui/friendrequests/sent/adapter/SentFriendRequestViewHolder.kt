package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter

import androidx.lifecycle.MutableLiveData
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_sent_friend_request.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder
import uk.co.victoriajanedavis.chatapp.presentation.ext.*

class SentFriendRequestViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val actionLiveData: MutableLiveData<SentFriendRequestAction>
) : BaseViewHolder<FriendshipEntity>(
    layoutInflater.inflate(R.layout.item_sent_friend_request, parent, false)
) {

    private lateinit var friendshipEntity: FriendshipEntity

    init {
        cancelButton.setOnClickListener {
            stateCanceling()
            actionLiveData.value = SentFriendRequestAction.Cancel(friendshipEntity.uuid!!)
        }
    }

    override fun bind(item: FriendshipEntity) {
        friendshipEntity = item
        iconTextView.text = item.username[0].toString()
        nameTextView.text = item.username

        val usernameText = "Username: ${item.username}"
        usernameTextView.text = usernameText
        val emailText = "Email: ${item.email}"
        emailTextView.text = emailText

        when(friendshipEntity.loadingState) {
            CANCEL -> stateCanceling()
            else -> stateNormal()
        }
    }

    private fun stateCanceling() {
        cancelButtonProgressBar.visible()
        cancelButton.disable()
    }

    private fun stateNormal() {
        cancelButtonProgressBar.gone()
        cancelButton.enable()
    }
}