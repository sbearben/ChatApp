package uk.co.victoriajanedavis.chatapp.presentation.ui.friends

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetReceivedFriendRequestsCount
import javax.inject.Inject

class FriendRequestsToolbarViewModel @Inject constructor(
        private val receivedRequestsCount: GetReceivedFriendRequestsCount
) : ViewModel() {

    private val friendRequestsCountLiveData = MutableLiveData<Int>()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getFriendRequestsCountLiveData(): LiveData<Int> = friendRequestsCountLiveData

    private fun bindToUseCase() : Disposable {
        return receivedRequestsCount.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendRequestsCountLiveData::postValue,
                    { e -> Log.e("RequestsTBarViewModel", (e.toString())) })
    }
}