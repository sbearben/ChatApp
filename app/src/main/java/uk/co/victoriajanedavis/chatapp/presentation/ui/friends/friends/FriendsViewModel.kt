package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetChatList
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState.*
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    private val getChatList: GetChatList
) : ViewModel() {

    private val chatLiveData = MutableLiveData<ListState<List<ChatEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        retry()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun getChatLiveData(): LiveData<ListState<List<ChatEntity>>> = chatLiveData


    fun retry() {
        chatLiveData.value = ShowLoading
        compositeDisposable.clear()
        compositeDisposable.add(bindToUseCase())
    }

    private fun bindToUseCase() : Disposable {
        return getChatList.getBehaviorStream(null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { streamState ->
                when (streamState) {
                    is OnNext -> onNext(streamState.content)
                    is OnError -> onError(streamState.throwable)
                }
            }
    }

    private fun onNext(chatEntities: List<ChatEntity>) {
        if (!chatEntities.isEmpty()) chatLiveData.value = ShowContent(chatEntities)
        else chatLiveData.value = ShowEmpty
    }

    private fun onError(e: Throwable) {
        chatLiveData.value = ShowError(e.message ?: e.toString())
    }
}