package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor
import java.util.UUID

class AcceptReceivedFriendRequest @Inject constructor(
    private val repository: ReceivedFriendRequestRepository
) : ActionInteractor<UUID> {

    override fun getActionCompletable(userUuid: UUID): Completable {
        return repository.acceptReceivedFriendRequest(userUuid)
    }
}
