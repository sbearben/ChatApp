package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_friend.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class FriendViewHolder(
    layoutInflater: LayoutInflater,
    parent: ViewGroup,
    private val clickListener: OnClickListener
) : BaseViewHolder<FriendshipEntity>(layoutInflater.inflate(R.layout.item_friend, parent, false)) {

    private lateinit var friendEntity: FriendshipEntity

    init {
        messageButton.setOnClickListener {
            clickListener.onMessageClicked(friendEntity)
        }
    }

    override fun bind(item: FriendshipEntity) {
        friendEntity = item
        iconTextView.text = item.username[0].toString()
        nameTextView.text = item.username

        val usernameText = "Username: ${item.username}"
        usernameTextView.text = usernameText
        val emailText = "Email: ${item.email}"
        emailTextView.text = emailText
    }

    interface OnClickListener {
        fun onMessageClicked(friendEntity: FriendshipEntity)
    }
}