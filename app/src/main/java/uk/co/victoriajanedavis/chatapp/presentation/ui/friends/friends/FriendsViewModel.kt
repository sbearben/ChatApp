package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetChatList
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
        private val getChatList: GetChatList
) : ViewModel() {

    private val chatLiveData = MutableLiveData<State<List<ChatEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        chatLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getChatLiveData(): LiveData<State<List<ChatEntity>>> = chatLiveData

    private fun bindToUseCase() : Disposable {
        return getChatList.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entityList -> chatLiveData.value = ShowContent(entityList)
                    Log.d("FriendsViewModel", "entityList size: ${entityList.size}")
                },
                    { e -> chatLiveData.value = ShowError(e.toString()) })
    }
}