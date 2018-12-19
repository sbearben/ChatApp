package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CanceledFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class CanceledFriendRequestStoragePipe @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val canceledFriendRequestMapper = CanceledFriendRequestWsFriendshipDbMapper()

    fun subscribeToCanceledFriendRequestsStream(): Disposable {
        return webSocketService.observerCanceledFriendRequests()
            .map(canceledFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}