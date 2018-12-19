package uk.co.victoriajanedavis.chatapp.data.realtime.websocket

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.CanceledFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.RejectedFriendRequestWsFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class RejectedFriendRequestStoragePipe @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    @SentFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>
) {
    private val rejectedFriendRequestMapper = RejectedFriendRequestWsFriendshipDbMapper()

    fun subscribeToRejectedFriendRequestsStream(): Disposable {
        return webSocketService.observerRejectedFriendRequests()
            .map(rejectedFriendRequestMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}