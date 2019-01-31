package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.send

import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_send_friend_request.*
import uk.co.victoriajanedavis.chatapp.presentation.ext.validate

class SendFriendRequestFormValidator constructor(
    rootView: View
) : LayoutContainer {

    override val containerView: View = rootView

    init {
        setupValidators()
    }

    private fun setupValidators() {
        usernameInputLayout.validate(
            "Username may only contain letters, numbers, and @/./+/-/_ characters.") { s ->
            s.isEmpty() || s.matches(Regex("^[\\w.@+-]+\$"))  // Regex from backend User model
        }
    }


}