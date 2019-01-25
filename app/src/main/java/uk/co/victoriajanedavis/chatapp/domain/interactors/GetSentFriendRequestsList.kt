package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import uk.co.victoriajanedavis.chatapp.presentation.ext.toStreamState

class GetSentFriendRequestsList @Inject constructor(
    private val repository: SentFriendRequestRepository
) : RetrieveInteractor<Void, StreamState<List<FriendshipEntity>>> {

    override fun getBehaviorStream(params: Void?): Observable<StreamState<List<FriendshipEntity>>> {
        return repository.getAllSentFriendRequests()
            .flatMapSingle(::fetchWhenEmptyAndThenReceivedFriendRequests)
    }

    private fun fetchWhenEmptyAndThenReceivedFriendRequests(
        friendEntities: List<FriendshipEntity>
    ): Single<StreamState<List<FriendshipEntity>>> {
        return fetchWhenEmpty(friendEntities).andThen(just(friendEntities)).toStreamState()
    }

    private fun fetchWhenEmpty(friendEntities: List<FriendshipEntity>): Completable {
        return if (friendEntities.isEmpty())
            repository.fetchSentFriendRequests()
        else
            Completable.complete()
    }
}
