package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CanceledFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import javax.inject.Inject

class CanceledFriendRequestStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val canceledFriendRequestMapper = CanceledFriendRequestWsFriendshipDbMapper()

    fun subscribeToCanceledFriendRequestsStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.canceledFriendRequestStream(),
            firebaseMessagingStreams.canceledFriendRequestStream())
            .doOnNext { Log.d("CanceledFriendReq", "WebSocket Emitted") }
            .publish().autoConnect()
            .map(canceledFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}