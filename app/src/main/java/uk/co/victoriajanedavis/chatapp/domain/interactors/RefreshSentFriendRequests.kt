package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RefreshInteractor
import javax.inject.Inject

class RefreshSentFriendRequests @Inject constructor(
    private val repository: SentFriendRequestRepository
) : RefreshInteractor<Void> {

    override fun getRefreshSingle(params: Void?): Completable {
        return repository.fetchSentFriendRequests()
    }
}