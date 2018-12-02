package uk.co.victoriajanedavis.chatapp.data.websocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder
import javax.inject.Inject

class WebSocketHolder @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenHolder: TokenEntityHolder
) {

    lateinit var webSocket: WebSocket

    private fun createRequest(): Request {
        return Request.Builder()
            .url("ws://10.0.2.2:8000/ws/chat/")
            .addHeader("Authorization", "Token ${tokenHolder.tokenEntity!!}")
            .build()
    }

    fun createWebSocketConnection() {
        webSocket = okHttpClient.newWebSocket(createRequest(), ChatWebSocketListener())
    }
}