package uk.co.victoriajanedavis.chatapp.data.realtime

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CreatedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import javax.inject.Inject

class CreatedFriendRequestStorageBinding @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val createdFriendRequestMapper = CreatedFriendRequestWsFriendshipDbMapper()

    fun subscribeToCreatedFriendRequestsStream(): Disposable {
        return Flowable.merge(
            webSocketService.observerCreatedFriendRequests(),
            firebaseMessagingStreams.createdFriendRequestStream())
            .doOnNext { Log.d("CreatedFriendReq", "WebSocket Emitted") }
            .publish().autoConnect()
            .map(createdFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}