package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetSentFriendRequestsList
import uk.co.victoriajanedavis.chatapp.domain.interactors.RefreshSentFriendRequests
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import javax.inject.Inject

class SentFriendRequestsViewModel @Inject constructor(
    private val getSentFriendRequests: GetSentFriendRequestsList,
    private val refreshSentFriendRequests: RefreshSentFriendRequests
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

    fun refreshItems() {
        refreshDisposable?.dispose()
        refreshDisposable = refreshSentFriendRequests.getRefreshSingle(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({friendLiveData.value = StopLoading}, { e -> onError(e) })
        compositeDisposable.add(refreshDisposable!!)
    }

    fun getSentFriendRequestsLiveData(): LiveData<ListState<List<FriendshipEntity>>> = friendLiveData

    private fun bindToUseCase() : Disposable {
        return getSentFriendRequests.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ streamState ->
                when (streamState) {
                    is StreamState.OnNext -> onNext(streamState.content)
                    is StreamState.OnError -> onError(streamState.throwable)
                }
            }
    }

    private fun onNext(friendEntities: List<FriendshipEntity>) {
        if (!friendEntities.isEmpty()) friendLiveData.value = ShowContent(friendEntities)
        else friendLiveData.value = ShowEmpty
    }

    private fun onError(throwable: Throwable) {
        friendLiveData.value = ShowError(throwable.toString())
    }
}