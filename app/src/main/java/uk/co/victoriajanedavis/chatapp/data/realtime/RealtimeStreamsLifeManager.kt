package uk.co.victoriajanedavis.chatapp.data.realtime

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RealtimeStreamsLifeManager @Inject constructor(
    private val messageBinding: MessageStorageBinding,
    private val createdFriendRequestBinding: CreatedFriendRequestStorageBinding,
    private val acceptedFriendRequestBinding: AcceptedFriendRequestStorageBinding,
    private val canceledFriendRequestBinding: CanceledFriendRequestStorageBinding,
    private val rejectedFriendRequestBinding: RejectedFriendRequestStorageBinding
) {
    private val disposables = CompositeDisposable()
    
    fun initializeStreams() {
        disposables.addAll(
            messageBinding.subscribeToMessagesStream(),
            createdFriendRequestBinding.subscribeToCreatedFriendRequestsStream(),
            acceptedFriendRequestBinding.subscribeToAcceptedFriendRequestStream(),
            canceledFriendRequestBinding.subscribeToCanceledFriendRequestsStream(),
            rejectedFriendRequestBinding.subscribeToRejectedFriendRequestsStream()
        )
    }

    fun clearStreams() {
        disposables.clear()
    }
}