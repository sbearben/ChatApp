package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.RejectedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore
import javax.inject.Inject

class RejectedFriendRequestStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @SentFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
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