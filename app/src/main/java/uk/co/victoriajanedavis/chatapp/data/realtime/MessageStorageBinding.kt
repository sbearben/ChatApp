package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbChatDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageWsDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

class MessageStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val messageStore: MessageReactiveStore,
    private val chatStore: BaseReactiveStore<ChatDbModel>
) {
    private val messageMapper = MessageWsDbMapper()
    private val chatMapper = MessageDbChatDbMapper()


    fun subscribeToMessagesStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.chatMessageStream().doOnNext { Log.d("MessageStorage", "Websocket message emitted") },
            firebaseMessagingStreams.chatMessageStream().doOnNext { Log.d("MessageStorage", "Firebase message emitted") })
            .publish().autoConnect()
            .map(messageMapper::mapFrom)
            .flatMapCompletable { messageDbModel ->
                Completable.mergeArray(
                    messageStore.storeSingular(messageDbModel),
                    chatStore.storeSingular(chatMapper.mapFrom(messageDbModel))
                )
                    .doOnComplete { Log.d("MessageStorage", "Realtime message stored: ${ThreadLocalRandom.current().nextInt(100000)}") }
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}