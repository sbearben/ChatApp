package uk.co.victoriajanedavis.chatapp.domain.interactors

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.PaginatedRetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import uk.co.victoriajanedavis.chatapp.presentation.ext.toStreamState

class GetPaginatedMessagesList @Inject constructor(
    private val messageRepository: MessageRepository
) : PaginatedRetrieveInteractor<UUID, StreamState<List<MessageEntity>>> {

    override fun getBehaviorStream(chatUuid: UUID?): Observable<StreamState<List<MessageEntity>>> {
        return messageRepository.getAllMessagesInChat(chatUuid)
            .flatMapSingle { messages -> fetchWhenEmptyAndThenMessages(messages, chatUuid) }
    }

    override fun fetchMoreItems(chatUuid: UUID?): Completable {
        return messageRepository.fetchMoreMessagesInChatOlderThanOldestInDb(chatUuid)
    }

    private fun fetchWhenEmptyAndThenMessages(
        messages: List<MessageEntity>,
        chatUuid: UUID?
    ): Single<StreamState<List<MessageEntity>>> {
        return fetchWhenEmpty(messages, chatUuid).andThen(just(messages)).toStreamState()
    }

    private fun fetchWhenEmpty(messages: List<MessageEntity>, chatUuid: UUID?): Completable {
        return if (messages.isEmpty())
            messageRepository.fetchInitialMessagesInChat(chatUuid)
        else
            Completable.complete()
    }
}
