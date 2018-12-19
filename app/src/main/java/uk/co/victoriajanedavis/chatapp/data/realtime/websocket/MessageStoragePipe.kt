package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbChatDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageWsDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class MessageStoragePipe @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val messageStore: MessageReactiveStore,
    private val chatStore: BaseReactiveStore<ChatDbModel>
) {

    private val messageMapper = MessageWsDbMapper()
    private val chatMapper = MessageDbChatDbMapper()


    fun subscribeToMessagesStream(): Disposable {
        return webSocketService.observeMessages()
            .map(messageMapper::mapFrom)
            .flatMapCompletable { messageDbModel ->
                Completable.mergeArray(
                    messageStore.storeSingular(messageDbModel),
                    chatStore.storeSingular(chatMapper.mapFrom(messageDbModel))
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}