package uk.co.victoriajanedavis.chatapp.data.repositories

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.SentFriendRequestStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.domain.common.doOnErrorOrDispose
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import java.util.UUID
import javax.inject.Named

@ApplicationScope
class SentFriendRequestRepository @Inject constructor(
    @Named(SentFriendRequestStore) private val friendStore: BaseReactiveStore<FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val dbEntityMapper = FriendshipDbEntityMapper()
    private val nwSentDbMapper = UserNwFriendshipDbMapper(false, true)

    fun getAllSentFriendRequests(): Observable<List<FriendshipEntity>> {
        return friendStore.getAll(null)
            .mapList(dbEntityMapper::mapFrom)
    }

    fun fetchSentFriendRequests(): Completable {
        return chatService.sentFriendRequests
            .mapList(nwSentDbMapper::mapFrom)
            .flatMapCompletable { friendRequests -> friendStore.replaceAll(null, friendRequests) }
    }

    fun sendFriendRequest(username: String, message: String?): Completable {
        return chatService.sendFriendRequest(username)
            .map(nwSentDbMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
    }

    fun cancelSentFriendRequest(receiverUserUuid: UUID): Completable {
        return friendStore.getSingular(receiverUserUuid)
            .switchMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = CANCEL))
                .andThen(chatService.cancelSentFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(nwSentDbMapper::mapFrom)
            .flatMapCompletable(friendStore::delete)
    }
}
