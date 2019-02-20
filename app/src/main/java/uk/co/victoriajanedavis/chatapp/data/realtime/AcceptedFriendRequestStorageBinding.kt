package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.AcceptedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.FriendshipStore
import javax.inject.Inject
import javax.inject.Named

class AcceptedFriendRequestStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @Named(FriendshipStore) private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val acceptedFriendRequestMapper = AcceptedFriendRequestWsFriendshipDbMapper()

    fun subscribeToAcceptedFriendRequestStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.acceptedFriendRequestStream(),
            firebaseMessagingStreams.acceptedFriendRequestStream())
            .doOnNext { Log.d("AcceptedFriendReq", "WebSocket Emitted") }
            .publish().autoConnect()
            .map(acceptedFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}