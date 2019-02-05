package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import androidx.lifecycle.ViewModel
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.RealtimeStreamsLifeManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val realtimeStreamsLifeManager: RealtimeStreamsLifeManager
) : ViewModel() {

    init {
        realtimeStreamsLifeManager.initializeStreams()
    }

    override fun onCleared() {
        realtimeStreamsLifeManager.clearStreams()
    }
}