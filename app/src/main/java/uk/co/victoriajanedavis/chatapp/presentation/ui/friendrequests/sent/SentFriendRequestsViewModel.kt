package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.CancelSentFriendRequest
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetSentFriendRequestsList
import uk.co.victoriajanedavis.chatapp.domain.interactors.RefreshSentFriendRequests
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState.*
import java.util.UUID
import javax.inject.Inject

class SentFriendRequestsViewModel @Inject constructor(
    private val getSentFriendRequests: GetSentFriendRequestsList,
    private val refreshSentFriendRequests: RefreshSentFriendRequests,
    private val cancelFriendRequest: CancelSentFriendRequest
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

    fun getSentFriendRequestsLiveData(): LiveData<ListState<List<FriendshipEntity>>> = friendLiveData

    fun refreshItems() {
        refreshDisposable?.dispose()
        refreshDisposable = refreshSentFriendRequests.getRefreshSingle(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({friendLiveData.value = StopLoading}, { e -> onError(e) })
        compositeDisposable.add(refreshDisposable!!)
    }

    fun cancelFriendRequest(receiverUserUuid: UUID) {
        compositeDisposable.add(bindToCancelFriendRequest(receiverUserUuid))
    }

    private fun bindToUseCase() : Disposable {
        return getSentFriendRequests.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ streamState ->
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

    private fun bindToCancelFriendRequest(receiverUserUuid: UUID) : Disposable {
        return cancelFriendRequest.getActionCompletable(receiverUserUuid)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { e -> onError(e) })
    }
}