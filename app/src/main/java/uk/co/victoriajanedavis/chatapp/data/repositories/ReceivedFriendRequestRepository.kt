package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.UUID

import javax.inject.Inject

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
import uk.co.victoriajanedavis.chatapp.domain.common.doOnErrorOrDispose
import uk.co.victoriajanedavis.chatapp.domain.common.mapList

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
            .mapList(dbEntityMapper::mapFrom)
        }

    fun fetchReceivedFriendRequests(): Completable {
        return chatService.receivedFriendRequests
            .mapList(nwReceivedDbMapper::mapFrom)
            .flatMapCompletable { friendRequests -> friendStore.replaceAll(null, friendRequests) }
    }

    /* TODO: this code exposes structural issues with the app and the way we're storing and displaying Chats:
     * - When storing the accepted friend in order for the new Chat to appear on the fragment that displays chats
     *   we also need a new ChatDbModel to store in the chats room database and includes a chatUuid to identify
     *  the conversation. We don't receive that from the "accepting friend request" REST call below.  Because of this
     *  flaw we have to rely on getting that from a websocket event. We should really have a separate fragments for
     *  "Friends" and "Chats" with the ability to send messages to friends that don't currently have "Chats" in order
     *  to alleviate this. We should also consider getting rid of the ChatDbModel and table all together and just
     *  getting the elements on the "Chats" fragment by getting the latest message by each unique chatUuid from the
     *  Messages Room DB table.
     */
    fun acceptReceivedFriendRequest(userUuid: UUID): Completable {
        return friendStore.getSingular(userUuid)
            .switchMapSingle { friendDbModel -> friendStore.storeSingular(friendDbModel.copy(loadingState = ACCEPT))
                .andThen(chatService.acceptFriendRequest(friendDbModel.username)
                    .doOnErrorOrDispose {
                        //Log.d("ReceivedRepo", "network doOnErrorOrDispose() ${friendDbModel.username}")
                        friendStore.storeSingular(friendDbModel.copy(loadingState = NONE)).blockingAwait()
                    })
            }
            .map(nwAcceptedDbMapper::mapFrom)
            .ignoreElements()
            //.flatMapCompletable(friendStore::storeSingular)
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
