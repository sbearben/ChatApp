package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RefreshInteractor
import javax.inject.Inject

class RefreshReceivedFriendRequests @Inject constructor(
    private val repository: ReceivedFriendRequestRepository
) : RefreshInteractor<Void> {

    override fun getRefreshSingle(params: Void?): Completable {
        return repository.fetchReceivedFriendRequests()
    }
}