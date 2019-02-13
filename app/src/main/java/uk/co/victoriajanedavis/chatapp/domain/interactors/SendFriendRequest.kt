package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor

class SendFriendRequest @Inject constructor(
    private val repository: SentFriendRequestRepository
) : ActionInteractor<SendFriendRequest.FriendRequestParams> {

    override fun getActionCompletable(params: FriendRequestParams): Completable {
        return repository.sendFriendRequest(params.username, params.message)
    }

    class FriendRequestParams(val username: String, val message: String?)
}
