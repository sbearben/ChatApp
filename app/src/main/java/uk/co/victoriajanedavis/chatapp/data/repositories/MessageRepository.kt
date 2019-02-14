package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.NonNull
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

    /* TODO: this code exposes structural issues with the app and the way we're storing and displaying Chats:
     * - When storing the new message in order for the corresponding Chat to update its most recent message to appear
     *  on the fragment that displays chats we also need to create a ChatDbModel to update the chat room database row.
     *  If we were to do all that here the MessageRepository would end up getting polluted with things like more mappers
     *  and a chatStore to store the chat, so we are relying on receiving a Websocket event to do that (where we've put
     *  that ugly code). The same issues described in ReceivedFriendRequestRepository apply here.
     */
    fun postMessageToChat(chatUuid: UUID, message: String): Single<MessageEntity> {
        return chatService.postMessageToChat(chatUuid.toString(), message)
            .map(nwDbMapper::mapFrom)
            .map(dbEntityMapper::mapFrom)  // added this temporarily and commented out below section
            //.flatMap { messageDbModel -> messageStore.storeSingular(messageDbModel)
                //.andThen(Single.just(messageDbModel)
                    //.map(dbEntityMapper::mapFrom))
            //}
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
