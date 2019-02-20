package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageNwDbMapper
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity

@ApplicationScope
class MessageRepository @Inject constructor(
    private val messageStore: MessageReactiveStore,
    private val chatService: ChatAppService
) {
    private val dbEntityMapper = MessageDbEntityMapper()
    private val nwDbMapper = MessageNwDbMapper()


    fun getAllMessagesInChat(chatUuid: UUID): Observable<List<MessageEntity>> {
        return messageStore.getAll(chatUuid)
            .mapList(dbEntityMapper::mapFrom)
    }

    fun fetchInitialMessagesInChat(chatUuid: UUID): Completable {
        return chatService.getNewestChatMessages(chatUuid.toString(), PAGE_SIZE)
            .mapList(nwDbMapper::mapFrom)
            .flatMapCompletable { messages -> messageStore.replaceAll(chatUuid, messages) }
    }

    fun fetchMoreMessagesInChatOlderThanOldestInDb(chatUuid: UUID): Completable {
        return messageStore.getDateOfOldestMessage(chatUuid)
            .flatMap { date -> chatService.getChatMessagesOlderThanGivenDate(chatUuid.toString(), date, PAGE_SIZE) }
            .mapList(nwDbMapper::mapFrom)
            .flatMapCompletable(messageStore::storeAll)
    }

    fun postMessageToChat(chatUuid: UUID, message: String): Single<MessageEntity> {
        return chatService.postMessageToChat(chatUuid.toString(), message)
            .map(nwDbMapper::mapFrom)
            .flatMap { messageDbModel -> messageStore.storeSingular(messageDbModel)
                .andThen(Single.just(messageDbModel)
                    .map(dbEntityMapper::mapFrom))
            }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
