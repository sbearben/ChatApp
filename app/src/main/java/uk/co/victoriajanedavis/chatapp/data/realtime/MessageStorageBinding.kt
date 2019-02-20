package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageWsDbMapper
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

class MessageStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val messageStore: MessageReactiveStore
) {
    private val messageMapper = MessageWsDbMapper()

    fun subscribeToMessagesStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.chatMessageStream().doOnNext { Log.d("MessageStorage", "Websocket message emitted") },
            firebaseMessagingStreams.chatMessageStream().doOnNext { Log.d("MessageStorage", "Firebase message emitted") })
            .map(messageMapper::mapFrom)
            .publish().autoConnect()
            .flatMapCompletable { messageDbModel ->
                    messageStore.storeSingular(messageDbModel)
                    .doOnComplete { Log.d("MessageStorage", "Realtime message stored: ${ThreadLocalRandom.current().nextInt(100000)}") }
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}