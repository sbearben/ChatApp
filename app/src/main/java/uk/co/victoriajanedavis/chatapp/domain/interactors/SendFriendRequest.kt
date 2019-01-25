package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor

class SendFriendRequest @Inject constructor(
    private val repository: SentFriendRequestRepository
) : SendInteractor<String, FriendshipEntity> {

    override fun getSingle( username: String): Single<FriendshipEntity> {
        return repository.sendFriendRequest(username)
    }
}
