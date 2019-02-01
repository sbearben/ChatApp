package uk.co.victoriajanedavis.chatapp.presentation.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetReceivedFriendRequestsCount
import uk.co.victoriajanedavis.chatapp.domain.interactors.IsUserLoggedIn
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState.*
import javax.inject.Inject

class FriendRequestsToolbarViewModel @Inject constructor(
    private val receivedRequestsCount: GetReceivedFriendRequestsCount,
    private val isUserLoggedIn: IsUserLoggedIn
) : ViewModel() {

    private val friendRequestsCountLiveData = MutableLiveData<State<Int>>()
    private val isUserLoggedInLiveData = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    init {
        requestItems()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun requestItems() {
        compositeDisposable.clear()
        compositeDisposable.add(bindToUseCase())
        compositeDisposable.add(bindToIsUserLoggedIn())
    }

    fun getFriendRequestsCountLiveData(): LiveData<State<Int>> = friendRequestsCountLiveData

    fun getIsUserLoggedInLiveData(): LiveData<Boolean> = isUserLoggedInLiveData

    private fun bindToUseCase() : Disposable {
        return receivedRequestsCount.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { streamState ->
                when (streamState) {
                    is OnNext -> friendRequestsCountLiveData.value = ShowContent(streamState.content)
                    is OnError -> friendRequestsCountLiveData.value = ShowError("Error fetching friend requests from network")
                }
            }
    }

    private fun bindToIsUserLoggedIn() : Disposable {
        return isUserLoggedIn.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isUserLoggedIn -> isUserLoggedInLiveData.value = isUserLoggedIn },
                { e -> Log.e("MainViewModel", "Error getting isUserLoggedIn boolean", e)})
    }
}