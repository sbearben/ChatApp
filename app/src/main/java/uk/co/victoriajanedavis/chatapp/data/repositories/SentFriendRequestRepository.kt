package uk.co.victoriajanedavis.chatapp.data.repositories

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.presentation.ext.doOnErrorOrDispose
import java.util.UUID

@ApplicationScope
class SentFriendRequestRepository @Inject constructor(
    @SentFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val dbEntityMapper = FriendshipDbEntityMapper()
    private val nwSentDbMapper = UserNwFriendshipDbMapper(false, true)

    fun getAllSentFriendRequests(): Observable<List<FriendshipEntity>> {
        return friendStore.getAll(null)
            .switchMapSingle { dbModels -> Observable.fromIterable(dbModels)
                .map(dbEntityMapper::mapFrom)
                .toList()
            }
    }

    fun fetchSentFriendRequests(): Completable {
        return chatService.sentFriendRequests
            .flatMap { nwModels -> Observable.fromIterable(nwModels)
                .map(nwSentDbMapper::mapFrom)
                .toList()
            }
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

    /*
    fun cancelSentFriendRequest(username: String): Single<FriendshipEntity> {
        return chatService.cancelSentFriendRequest(username)
            .map(nwSentDbMapper::mapFrom)
            .flatMap { friendEntity -> friendStore.delete(friendEntity)
                .andThen(Single.just(friendEntity).map(dbEntityMapper::mapFrom))
            }
    }
    */
}
