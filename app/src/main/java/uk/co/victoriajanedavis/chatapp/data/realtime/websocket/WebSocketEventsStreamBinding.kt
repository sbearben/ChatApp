package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import android.util.Log
import com.tinder.scarlet.websocket.WebSocketEvent
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WebSocketEventsStreamBinding @Inject constructor(
    private val webSocketService: ChatAppWebSocketService
) {

    fun subscribeToWebSocketEventStream(): Disposable {
        return webSocketService.observeWebSocketEvent()
            .publish().autoConnect()
            .subscribeOn(Schedulers.io())
            .subscribe({ event -> when(event) {
                is WebSocketEvent.OnConnectionOpened -> Log.d("WebSocketRep", "Connection Opened")
                is WebSocketEvent.OnMessageReceived -> Log.d("WebSocketRep", "Message Received")
                is WebSocketEvent.OnConnectionClosing -> Log.d("WebSocketRep", "Connection Closing")
                is WebSocketEvent.OnConnectionClosed -> Log.d("WebSocketRep", "Connection Closed")
                is WebSocketEvent.OnConnectionFailed -> Log.d("WebSocketRep", "Connection Failed")
            }},
                { throwable: Throwable -> Log.d("WebSocketRep", throwable.message)
                })
    }
}