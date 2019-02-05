package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CanceledFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import javax.inject.Inject

class CanceledFriendRequestStorageBinding @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val canceledFriendRequestMapper = CanceledFriendRequestWsFriendshipDbMapper()

    fun subscribeToCanceledFriendRequestsStream(): Disposable {
        return Flowable.merge(
            webSocketService.observerCanceledFriendRequests(),
            firebaseMessagingStreams.canceledFriendRequestStream())
            .doOnNext { Log.d("CanceledFriendReq", "WebSocket Emitted") }
            .map(canceledFriendRequestMapper::mapFrom)
            .publish().autoConnect()
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}