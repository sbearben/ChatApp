package uk.co.victoriajanedavis.chatapp.presentation.notifications

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.presentation.notifications.friend.FriendNotification
import uk.co.victoriajanedavis.chatapp.presentation.notifications.friendrequest.FriendRequestNotification
import uk.co.victoriajanedavis.chatapp.presentation.notifications.message.MessageNotification
import javax.inject.Inject

class ChatAppNotificationManager @Inject constructor(
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val messageNotification: MessageNotification,
    private val friendRequestNotification: FriendRequestNotification,
    private val friendNotification: FriendNotification
) {
    private val disposables = CompositeDisposable()


    fun initializeStreams() {
        disposables.addAll(
            bindToChatMessageStream(),
            bindToCreatedFriendRequestStream(),
            bindToAcceptedFriendRequestStream()
        )
    }

    fun clearStreams() {
        disposables.dispose()
    }

    private fun bindToChatMessageStream(): Disposable {
        return firebaseMessagingStreams.chatMessageStream()
            .subscribe(messageNotification::issueNotification)
    }

    private fun bindToCreatedFriendRequestStream(): Disposable {
        return firebaseMessagingStreams.createdFriendRequestStream()
            .subscribe(friendRequestNotification::issueNotification)
    }

    private fun bindToAcceptedFriendRequestStream(): Disposable {
        return firebaseMessagingStreams.acceptedFriendRequestStream()
            .subscribe(friendNotification::issueNotification)
    }
}