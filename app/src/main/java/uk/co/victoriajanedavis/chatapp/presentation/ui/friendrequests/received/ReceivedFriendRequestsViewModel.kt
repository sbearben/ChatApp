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
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetReceivedFriendRequestsList
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import javax.inject.Inject

class ReceivedFriendRequestsViewModel @Inject constructor(
        private val getReceivedFriendRequests: GetReceivedFriendRequestsList
) : ViewModel() {

    private val friendLiveData = MutableLiveData<ListState<List<FriendshipEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        friendLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getReceiviedFriendRequestsLiveData() = friendLiveData

    private fun bindToUseCase() : Disposable {
        return getReceivedFriendRequests.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entityList ->
                    if (!entityList.isEmpty()) friendLiveData.value = ShowContent(entityList)
                    else friendLiveData.value = ShowEmpty
                    Log.d("FriendsViewModel", "entityList size: ${entityList.size}")
                }, { e ->
                    friendLiveData.value = ShowError(e.toString())
                })
    }
}