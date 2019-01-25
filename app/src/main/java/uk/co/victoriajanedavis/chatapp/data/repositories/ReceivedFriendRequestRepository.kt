package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.UUID

import javax.inject.Inject

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState.*
import uk.co.victoriajanedavis.chatapp.presentation.ext.doOnErrorOrDispose

@ApplicationScope
class ReceivedFriendRequestRepository @Inject constructor(
    @ReceivedFriendRequestStore private val friendStore: BaseReactiveStore<FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val dbEntityMapper = FriendshipDbEntityMapper()
    private val nwReceivedDbMapper = UserNwFriendshipDbMapper(false, false)
    private val nwAcceptedDbMapper = UserNwFriendshipDbMapper(true, null)

    fun allReceivedFriendRequests(): Observable<List<FriendshipEntity>> {
        return friendStore.getAll(null)
            .switchMapSingle { dbModels -> Observable.fromIterable(dbModels)
                .map(dbEntityMapper::mapFrom)
                .toList() }
        }

    fun fetchReceivedFriendRequests(): Completable {
        return chatService.receivedFriendRequests
            .flatMap { nwModels -> Observable.fromIterable(nwModels)
                .map(nwReceivedDbMapper::mapFrom)
                .toList() }
            .flatMapCompletable { friendRequests -> friendStore.replaceAll(null, friendRequests) }
    }

    fun acceptReceivedFriendRequest(userUuid: UUID): Completable {
        return friendStore.getSingular(userUuid)
            .flatMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = ACCEPT))
                .andThen(chatService.acceptFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        //Log.d("ReceivedRepo", "network doOnErrorOrDispose() ${friendDbModel.username}")
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(nwAcceptedDbMapper::mapFrom)
            .flatMapCompletable(friendStore::storeSingular)
    }

    fun rejectReceivedFriendRequest(userUuid: UUID): Completable {
        return friendStore.getSingular(userUuid)
            .flatMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = REJECT))
                .andThen(chatService.rejectFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(nwReceivedDbMapper::mapFrom)
            .flatMapCompletable(friendStore::delete)
    }
}
