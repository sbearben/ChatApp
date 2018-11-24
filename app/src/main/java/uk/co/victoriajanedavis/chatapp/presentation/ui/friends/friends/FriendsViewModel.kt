package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetChatMembershipList
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
        private val getChatMembershipList: GetChatMembershipList
) : ViewModel() {

    private val chatMembershipLiveData = MutableLiveData<State<List<ChatMembershipEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        chatMembershipLiveData.postValue(ShowLoading)
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getChatMembershipLiveData(): LiveData<State<List<ChatMembershipEntity>>> = chatMembershipLiveData

    private fun bindToUseCase() : Disposable {
        return getChatMembershipList.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entityList -> chatMembershipLiveData.postValue(ShowContent(entityList)) },
                        { e -> chatMembershipLiveData.postValue(ShowError(e.toString())) })
    }
}