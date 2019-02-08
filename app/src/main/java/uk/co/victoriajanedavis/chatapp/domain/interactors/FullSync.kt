package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FullSync @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedIn,
    private val chatRepository: ChatRepository,
    private val receivedFriendRequestRepository: ReceivedFriendRequestRepository,
    private val sentFriendRequestRepository: SentFriendRequestRepository
) : ReactiveInteractor.ActionInteractor<Long> {

    override fun getActionCompletable(delayMilli: Long) : Completable {
        return Single.timer(delayMilli, TimeUnit.MILLISECONDS)
            .flatMapObservable { _ -> isUserLoggedIn.getBehaviorStream(null) }
            .flatMapCompletable(::initiateSyncIfLoggedIn)
    }

    private fun initiateSyncIfLoggedIn(isLoggedIn: Boolean): Completable {
        return if(isLoggedIn) {
            initiateSync()
        }
        else {
            Completable.complete()
        }
    }

    private fun initiateSync() : Completable {
        return Completable.mergeArray(
            chatRepository.fetchChatMemberships(),
            receivedFriendRequestRepository.fetchReceivedFriendRequests(),
            sentFriendRequestRepository.fetchSentFriendRequests()
        )
    }

    companion object {
        private const val RETRY_TIMES: Long = 3
    }
}