package uk.co.victoriajanedavis.chatapp.domain.interactors

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor

class SendChatMessage @Inject constructor(
    private val repository: MessageRepository
) : SendInteractor<SendChatMessage.MessageParams, MessageEntity> {

    override fun getSingle(params: MessageParams): Single<MessageEntity> {
        return repository.postMessageToChat(params.chatUuid, params.message)
    }

    class MessageParams(val chatUuid: UUID, val message: String)
}
