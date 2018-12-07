package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import androidx.lifecycle.ViewModel
import uk.co.victoriajanedavis.chatapp.data.repositories.WebSocketRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        private val webSocketRepository: WebSocketRepository
) : ViewModel() {

    init {
        webSocketRepository.initializeStreams()
    }

    override fun onCleared() {
        webSocketRepository.initializeStreams()
    }
}