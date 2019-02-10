package uk.co.victoriajanedavis.chatapp.presentation.notifications

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import uk.co.victoriajanedavis.chatapp.data.model.websocket.MessageWsModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.RealtimeModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.notifications.message.MessageNotification
import javax.inject.Inject

class ChatAppNotificationManager @Inject constructor(
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val messageNotification: MessageNotification
) {
    private val disposables = CompositeDisposable()


    fun initializeStreams() {
        disposables.addAll(
            bindToChatMessageStream()
        )
    }

    fun clearStreams() {
        disposables.dispose()
    }

    private fun bindToChatMessageStream(): Disposable {
        return firebaseMessagingStreams.chatMessageStream()
            .subscribe(messageNotification::issueNotification)
    }
}