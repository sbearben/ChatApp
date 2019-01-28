package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor

class SendFriendRequest @Inject constructor(
    private val repository: SentFriendRequestRepository
) : ActionInteractor<SendFriendRequest.FriendRequestParams> {

    override fun getActionCompletable(requestParams: FriendRequestParams): Completable {
        return repository.sendFriendRequest(requestParams.username, requestParams.message)
    }

    class FriendRequestParams(val username: String, val message: String?)
}
