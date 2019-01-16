package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.RejectedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore
import javax.inject.Inject

class RejectedFriendRequestStorageBinding @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @SentFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val rejectedFriendRequestMapper = RejectedFriendRequestWsFriendshipDbMapper()

    fun subscribeToRejectedFriendRequestsStream(): Disposable {
        return Flowable.merge(
            webSocketService.observerRejectedFriendRequests(),
            firebaseMessagingStreams.rejectedFriendRequestStream())
            .doOnNext { Log.d("RejectedFriendReq", "WebSocket Emitted") }
            .map(rejectedFriendRequestMapper::mapFrom)
            .publish().autoConnect()
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}