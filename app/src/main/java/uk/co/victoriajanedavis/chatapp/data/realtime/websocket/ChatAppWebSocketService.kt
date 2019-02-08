package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import com.tinder.scarlet.StateTransition
import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.ws.Receive
import io.reactivex.Flowable
import uk.co.victoriajanedavis.chatapp.data.model.websocket.*

interface ChatAppWebSocketService {
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocketEvent>
}