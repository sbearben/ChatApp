package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState.*
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetFriendList
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    private val getFriendList: GetFriendList
) : ViewModel() {

    private val friendsLiveData = MutableLiveData<ListState<List<FriendshipEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        retry()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun getFriendsLiveData(): LiveData<ListState<List<FriendshipEntity>>> = friendsLiveData


    fun retry() {
        friendsLiveData.value = ShowLoading
        compositeDisposable.clear()
        compositeDisposable.add(bindToUseCase())
    }

    private fun bindToUseCase() : Disposable {
        return getFriendList.getBehaviorStream(null)
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
        if (!friendEntities.isEmpty()) friendsLiveData.value = ShowContent(friendEntities)
        else friendsLiveData.value = ShowEmpty
    }

    private fun onError(e: Throwable) {
        friendsLiveData.value = ShowError(e.message ?: e.toString())
    }
}