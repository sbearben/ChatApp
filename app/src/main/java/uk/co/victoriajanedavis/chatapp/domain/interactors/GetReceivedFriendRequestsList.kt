package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState
import uk.co.victoriajanedavis.chatapp.domain.common.toStreamState

class GetReceivedFriendRequestsList @Inject constructor(
    private val repository: ReceivedFriendRequestRepository
) : RetrieveInteractor<Void, StreamState<List<FriendshipEntity>>> {

    override fun getBehaviorStream(params: Void?): Observable<StreamState<List<FriendshipEntity>>> {
        return repository.allReceivedFriendRequests()
            .switchMapSingle(::fetchWhenEmptyAndThenReceivedFriendRequests)
    }

    private fun fetchWhenEmptyAndThenReceivedFriendRequests(
        friendEntities: List<FriendshipEntity>
    ): Single<StreamState<List<FriendshipEntity>>> {
        return fetchWhenEmpty(friendEntities).andThen(just(friendEntities)).toStreamState()
    }

    private fun fetchWhenEmpty(friendEntities: List<FriendshipEntity>): Completable {
        return if (friendEntities.isEmpty())
            repository.fetchReceivedFriendRequests()
        else
            Completable.complete()
    }
}
