package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_received_friend_request.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import java.util.*

class ReceivedFriendRequestViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val clickListener: OnClickListener
) : BaseViewHolder<FriendshipEntity>(
    layoutInflater.inflate(R.layout.item_received_friend_request, parent, false)
) {

    private lateinit var friendshipEntity: FriendshipEntity

    init {
        acceptButton.setOnClickListener {
            stateAccepting()
            clickListener.onAcceptClicked(senderUserUuid = friendshipEntity.uuid!!)
        }
        rejectButton.setOnClickListener {
            stateRejecting()
            clickListener.onRejectClicked(senderUserUuid = friendshipEntity.uuid!!)
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
            ACCEPT -> stateAccepting()
            REJECT -> stateRejecting()
            else -> stateNormal()
        }
    }

    private fun stateAccepting() {
        acceptButtonProgressBar.visible()
        acceptButton.disable()
        rejectButton.disable()
    }

    private fun stateRejecting() {
        rejectButtonProgressBar.visible()
        rejectButton.disable()
        acceptButton.disable()
    }

    private fun stateNormal() {
        acceptButtonProgressBar.gone()
        acceptButton.enable()
        rejectButtonProgressBar.gone()
        rejectButton.enable()
    }

    interface OnClickListener {
        fun onAcceptClicked(senderUserUuid: UUID)
        fun onRejectClicked(senderUserUuid: UUID)
    }
}