package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.disposables.CompositeDisposable
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.AcceptedFriendRequestStoragePipe
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.CreatedFriendRequestStoragePipe
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.MessageStoragePipe
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketEventsStream
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class WebSocketEventStreamsLifeManager @Inject constructor(
    private val eventsStream: WebSocketEventsStream,
    private val messagePipe: MessageStoragePipe,
    private val createdFriendRequestPipe: CreatedFriendRequestStoragePipe,
    private val acceptedFriendRequestPipe: AcceptedFriendRequestStoragePipe,
    private val canceledFriendRequestPipe: CanceledFriendRequestStoragePipe,
    private val rejectedFriendRequestPipe: RejectedFriendRequestStoragePipe
) {
    private val disposables = CompositeDisposable()


    fun initializeStreams() {
        disposables.addAll(
            eventsStream.subscribeToWebSocketEventStream(),
            messagePipe.subscribeToMessagesStream(),
            createdFriendRequestPipe.subscribeToCreatedFriendRequestsStream(),
            acceptedFriendRequestPipe.subscribeToAcceptedFriendRequestStream(),
            canceledFriendRequestPipe.subscribeToCanceledFriendRequestsStream(),
            rejectedFriendRequestPipe.subscribeToRejectedFriendRequestsStream()
        )
    }

    fun clearStreams() {
        disposables.clear()
    }
}