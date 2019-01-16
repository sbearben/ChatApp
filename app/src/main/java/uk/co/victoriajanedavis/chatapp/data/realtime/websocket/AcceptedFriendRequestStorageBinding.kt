package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.AcceptedFriendRequestWsChatDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore
import javax.inject.Inject

class AcceptedFriendRequestStorageBinding @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val chatStore: BaseReactiveStore<ChatDbModel>,
    @FriendshipStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val acceptedFriendRequestMapper = AcceptedFriendRequestWsChatDbMapper()


    fun subscribeToAcceptedFriendRequestStream(): Disposable {
        return Flowable.merge(
            webSocketService.observerAcceptedFriendRequests(),
            firebaseMessagingStreams.acceptedFriendRequestStream())
            .doOnNext { Log.d("AcceptedFriendReq", "WebSocket Emitted") }
            .map(acceptedFriendRequestMapper::mapFrom)
            .publish().autoConnect()
            .flatMapCompletable { chatDbModel ->
                Completable.mergeArray(
                    chatStore.storeSingular(chatDbModel),
                    friendStore.storeSingular(chatDbModel.friendship!!)
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}