package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.RejectedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.SentFriendRequestStore
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class RejectedFriendRequestStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @Named(SentFriendRequestStore) private val friendStore: ReactiveStore<UUID, FriendshipDbModel>
) {
    private val rejectedFriendRequestMapper = RejectedFriendRequestWsFriendshipDbMapper()

    fun subscribeToRejectedFriendRequestsStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.rejectedFriendRequestStream(),
            firebaseMessagingStreams.rejectedFriendRequestStream())
            .doOnNext { Log.d("RejectedFriendReq", "WebSocket Emitted") }
            .publish().autoConnect()
            .map(rejectedFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}