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
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import javax.inject.Inject

class SentFriendRequestsViewModel @Inject constructor(
        private val getSentFriendRequests: GetSentFriendRequestsList
) : ViewModel() {

    private val friendLiveData = MutableLiveData<State<List<FriendshipEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        friendLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getSentFriendRequestsLiveData(): LiveData<State<List<FriendshipEntity>>> = friendLiveData

    private fun bindToUseCase() : Disposable {
        return getSentFriendRequests.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entityList -> friendLiveData.value = ShowContent(entityList)
                    Log.d("FriendsViewModel", "entityList size: ${entityList.size}")
                },
                    { e -> friendLiveData.value = ShowError(e.toString()) })
    }
}