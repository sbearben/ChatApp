package uk.co.victoriajanedavis.chatapp.data.realtime

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
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.WebSocketStreams
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore
import javax.inject.Inject

class AcceptedFriendRequestStorageBinding @Inject constructor(
    private val webSocketStreams: WebSocketStreams,
    private val firebaseMessagingStreams: FirebaseMessagingStreams,
    private val chatStore: BaseReactiveStore<ChatDbModel>,
    @FriendshipStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val acceptedFriendRequestMapper = AcceptedFriendRequestWsChatDbMapper()


    fun subscribeToAcceptedFriendRequestStream(): Disposable {
        return Flowable.merge(
            webSocketStreams.acceptedFriendRequestStream(),
            firebaseMessagingStreams.acceptedFriendRequestStream())
            .doOnNext { Log.d("AcceptedFriendReq", "WebSocket Emitted") }
            .publish().autoConnect()
            .map(acceptedFriendRequestMapper::mapFrom)
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