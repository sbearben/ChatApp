package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor
import java.util.UUID

class CancelSentFriendRequest @Inject constructor(
    private val repository: SentFriendRequestRepository
) : ActionInteractor<UUID> {

    override fun getActionCompletable(receiverUserUuid: UUID): Completable {
        return repository.cancelSentFriendRequest(receiverUserUuid)
    }
}