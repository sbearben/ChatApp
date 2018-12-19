package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CreatedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class CreatedFriendRequestStoragePipe @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val createdFriendRequestMapper = CreatedFriendRequestWsFriendshipDbMapper()

    fun subscribeToCreatedFriendRequestsStream(): Disposable {
        return webSocketService.observerCreatedFriendRequests()
            .map(createdFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}