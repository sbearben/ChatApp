package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import androidx.lifecycle.ViewModel
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketEventStreamsLifeManager
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        private val webSocketEventStreamsLifeManager: WebSocketEventStreamsLifeManager
) : ViewModel() {

    init {
        webSocketEventStreamsLifeManager.initializeStreams()
    }

    override fun onCleared() {
        webSocketEventStreamsLifeManager.clearStreams()
    }
}