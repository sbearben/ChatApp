package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import com.tinder.scarlet.Message
import com.tinder.scarlet.websocket.WebSocketEvent
import io.reactivex.Flowable
import uk.co.victoriajanedavis.chatapp.data.model.websocket.*
import uk.co.victoriajanedavis.chatapp.data.realtime.RealtimeEventResolver
import javax.inject.Inject

class WebSocketStreams @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val eventResolver: RealtimeEventResolver
) {

    fun chatMessageStream(): Flowable<MessageWsModel> {
        return allEventsStream().ofType(MessageWsModel::class.java)
    }

    fun acceptedFriendRequestStream(): Flowable<AcceptedFriendRequestWsModel> {
        return allEventsStream().ofType(AcceptedFriendRequestWsModel::class.java)
    }

    fun canceledFriendRequestStream(): Flowable<CanceledFriendRequestWsModel> {
        return allEventsStream().ofType(CanceledFriendRequestWsModel::class.java)
    }

    fun createdFriendRequestStream(): Flowable<CreatedFriendRequestWsModel> {
        return allEventsStream().ofType(CreatedFriendRequestWsModel::class.java)
    }

    fun rejectedFriendRequestStream(): Flowable<RejectedFriendRequestWsModel> {
        return allEventsStream().ofType(RejectedFriendRequestWsModel::class.java)
    }

    private fun allEventsStream(): Flowable<RealtimeModel> {
        return webSocketService.observeWebSocketEvent()
            .ofType(WebSocketEvent.OnMessageReceived::class.java)
            //.filter {event -> event is WebSocketEvent.OnMessageReceived }
            .map { messageReceivedEvent -> messageReceivedEvent.message }
            .ofType(Message.Text::class.java)
            .map { messageText -> messageText.value }
            .map(eventResolver::resolveEventFromJson)
    }
}