package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import uk.co.victoriajanedavis.chatapp.presentation.ext.toStreamState

class GetFirebaseToken @Inject constructor(
    private val repository: FirebaseTokenRepository
) : RetrieveInteractor<Void, String> {

    override fun getBehaviorStream(params: Void?): Observable<String> {
        return repository.getTokenStream()
            .flatMapSingle { token -> fetchWhenEmptyAndThenToken(token) }
    }

    private fun fetchWhenEmptyAndThenToken(token: String) : Single<String> {
        return fetchWhenEmpty(token).andThen(Single.just(token))
    }

    private fun fetchWhenEmpty(token: String) : Completable {
        return if (token.isEmpty()) {
            repository.fetchToken()
        } else {
            Completable.complete()
        }
    }
}
