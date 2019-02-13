package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.send

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendFriendRequest
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendFriendRequest.FriendRequestParams
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import javax.inject.Inject

class SendFriendRequestViewModel @Inject constructor(
    private val sendFriendRequest: SendFriendRequest
) : ViewModel() {

    private val requestLiveData = MutableLiveData<State<String>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getRequestLiveData(): LiveData<State<String>> = requestLiveData

    fun sendFriendRequest(username: String, message: String?) {
        requestLiveData.value = ShowLoading
        compositeDisposable.clear()
        compositeDisposable.add(bindToUseCase(FriendRequestParams(username, message)))
    }

    private fun bindToUseCase(requestParams: FriendRequestParams) : Disposable {
        return sendFriendRequest.getActionCompletable(requestParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { requestLiveData.value = ShowContent(requestParams.username) },
                { e ->
                    requestLiveData.value = ShowError(e.message ?: e.toString())
                    requestLiveData.value = null  // Clear it so that on rotation the error isn't shown again
                }
            )
    }
}