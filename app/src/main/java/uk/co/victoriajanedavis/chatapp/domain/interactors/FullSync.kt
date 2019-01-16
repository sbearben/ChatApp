package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository
import javax.inject.Inject

class FullSync @Inject constructor(
    private val chatRepository: ChatRepository
) {

    fun initiateSync() : Completable {
        return chatRepository.fetchChatMemberships()
    }
}