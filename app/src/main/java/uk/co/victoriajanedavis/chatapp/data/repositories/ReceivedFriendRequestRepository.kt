package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipNwDbMapper
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.domain.common.doOnErrorOrDispose
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import javax.inject.Named

@ApplicationScope
class ReceivedFriendRequestRepository @Inject constructor(
    @Named(ReceivedFriendRequestStore) private val friendStore: ReactiveStore<UUID, FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val dbEntityMapper = FriendshipDbEntityMapper()
    private val nwReceivedDbMapper = UserNwFriendshipDbMapper(false, false)
    private val chatNwDbMapper = ChatMembershipNwDbMapper()

    fun allReceivedFriendRequests(): Observable<List<FriendshipEntity>> {
        return friendStore.getAll(null)
            .mapList(dbEntityMapper::mapFrom)
        }

    fun fetchReceivedFriendRequests(): Completable {
        return chatService.receivedFriendRequests
            .mapList(nwReceivedDbMapper::mapFrom)
            .flatMapCompletable { friendRequests -> friendStore.replaceAll(null, friendRequests) }
    }

    fun acceptReceivedFriendRequest(userUuid: UUID): Completable {
        return friendStore.getSingular(userUuid)
            .switchMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = ACCEPT))
                .andThen(chatService.acceptFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        //Log.d("ReceivedRepo", "network doOnErrorOrDispose() ${friendDbModel.username}")
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(chatNwDbMapper::mapFrom)
            .flatMapCompletable { chatDbModel -> friendStore.storeSingular(chatDbModel.friendship) }
    }

    fun rejectReceivedFriendRequest(userUuid: UUID): Completable {
        return friendStore.getSingular(userUuid)
            .switchMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = REJECT))
                .andThen(chatService.rejectFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(nwReceivedDbMapper::mapFrom)
            .flatMapCompletable(friendStore::delete)
    }
}
