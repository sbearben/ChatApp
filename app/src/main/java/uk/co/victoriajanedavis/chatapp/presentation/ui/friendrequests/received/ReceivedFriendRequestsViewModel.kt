package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.AcceptReceivedFriendRequest
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetReceivedFriendRequestsList
import uk.co.victoriajanedavis.chatapp.domain.interactors.RefreshReceivedFriendRequests
import uk.co.victoriajanedavis.chatapp.domain.interactors.RejectReceivedFriendRequest
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState.*
import java.util.UUID
import javax.inject.Inject

class ReceivedFriendRequestsViewModel @Inject constructor(
    private val getReceivedFriendRequests: GetReceivedFriendRequestsList,
    private val refreshReceivedFriendRequests: RefreshReceivedFriendRequests,
    private val acceptFriendRequest: AcceptReceivedFriendRequest,
    private val rejectFriendRequest: RejectReceivedFriendRequest
) : ViewModel() {

    private val friendLiveData = MutableLiveData<ListState<List<FriendshipEntity>>>()
    private val compositeDisposable = CompositeDisposable()
    private var refreshDisposable: Disposable? = null

    init {
        friendLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun getReceivedFriendRequestsLiveData(): LiveData<ListState<List<FriendshipEntity>>> = friendLiveData

    fun refreshItems() {
        refreshDisposable?.let { it -> compositeDisposable.remove(it) }
        refreshDisposable = refreshReceivedFriendRequests.getRefreshSingle(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({friendLiveData.value = StopLoading}, { e -> onError(e) })
        compositeDisposable.add(refreshDisposable!!)
    }

    fun acceptFriendRequest(senderUserUuid: UUID) {
        compositeDisposable.add(bindToAcceptFriendRequest(senderUserUuid))
    }

    fun rejectFriendRequest(senderUserUuid: UUID) {
        compositeDisposable.add(bindToRejectFriendRequest(senderUserUuid))
    }

    private fun bindToUseCase() : Disposable {
        return getReceivedFriendRequests.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { streamState ->
                when (streamState) {
                    is OnNext -> onNext(streamState.content)
                    is OnError -> onError(streamState.throwable)
                }
            }
    }

    private fun onNext(friendEntities: List<FriendshipEntity>) {
        if (!friendEntities.isEmpty()) friendLiveData.value = ShowContent(friendEntities)
        else friendLiveData.value = ShowEmpty
    }

    private fun onError(e: Throwable) {
        friendLiveData.value = ShowError(e.message ?: e.toString())
    }

    private fun bindToAcceptFriendRequest(senderUserUuid: UUID) : Disposable {
        return acceptFriendRequest.getActionCompletable(senderUserUuid)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { e -> onError(e) })
    }

    private fun bindToRejectFriendRequest(senderUserUuid: UUID) : Disposable {
        return rejectFriendRequest.getActionCompletable(senderUserUuid)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { e -> onError(e) })
    }
}