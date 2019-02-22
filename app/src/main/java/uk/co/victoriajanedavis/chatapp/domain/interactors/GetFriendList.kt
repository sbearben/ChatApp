package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.data.repositories.FriendRepository
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState
import uk.co.victoriajanedavis.chatapp.domain.common.toStreamState
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

class GetFriendList @Inject constructor(
    private val friendRepository: FriendRepository
) : RetrieveInteractor<Void, StreamState<List<FriendshipEntity>>> {

    override fun getBehaviorStream(params: Void?): Observable<StreamState<List<FriendshipEntity>>> {
        return friendRepository.allFriends()
            .flatMapSingle(::fetchWhenEmptyAndThenFriendEntities)
    }

    private fun fetchWhenEmptyAndThenFriendEntities(
        friendEntities: List<FriendshipEntity>
    ): Single<StreamState<List<FriendshipEntity>>> {
        return fetchWhenEmpty(friendEntities).andThen(just(friendEntities)).toStreamState()
    }

    private fun fetchWhenEmpty(friendEntities: List<FriendshipEntity>): Completable {
        return if (friendEntities.isEmpty())
            friendRepository.fetchChatMemberships()
        else
            Completable.complete()
    }
}
