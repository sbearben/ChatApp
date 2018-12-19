package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.AcceptedFriendRequestWsChatDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class AcceptedFriendRequestStoragePipe @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val chatStore: BaseReactiveStore<ChatDbModel>,
    @FriendshipStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {

    private val acceptedFriendRequestMapper = AcceptedFriendRequestWsChatDbMapper()


    fun subscribeToAcceptedFriendRequestStream(): Disposable {
        return webSocketService.observerAcceptedFriendRequests()
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