package uk.co.victoriajanedavis.chatapp.presentation.ui.chat

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetPaginatedMessagesList
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage.MessageParams
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState.*

import java.util.*
import javax.inject.Inject

class ChatViewModel @Inject constructor(
        private val getPaginatedMessages: GetPaginatedMessagesList,
        private val sendChatMessage: SendChatMessage
) : ViewModel() {

    private val messageLiveData = MutableLiveData<PaginatedState<List<MessageEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getMessageLiveData(): LiveData<PaginatedState<List<MessageEntity>>> = messageLiveData

    fun fetchMessagesForChat(chatUuid: UUID) {
        messageLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase(chatUuid))
    }
    fun loadMore(chatUuid: UUID) {
        messageLiveData.value = ShowLoadingMore
        compositeDisposable.add(bindToLoadMore(chatUuid))
    }

    fun postMessage(chatUuid: UUID, message: String) {
        compositeDisposable.add(bindToSendMessage(MessageParams(chatUuid, message)))
    }

    private fun bindToUseCase(chatUuid: UUID) : Disposable {
        return getPaginatedMessages.getBehaviorStream(chatUuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ entityList -> messageLiveData.value = ShowContent(entityList)
                Log.d("ChatViewModel", "entityList size: ${entityList.size}")
            }, { e -> messageLiveData.value = ShowError(e.toString()) })
    }

    private fun bindToLoadMore(chatUuid: UUID) : Disposable {
        return getPaginatedMessages.fetchMoreItems(chatUuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ messageLiveData.value = LoadingMoreComplete
                Log.d("ChatViewModel", "Success") },
                { e -> messageLiveData.value = ShowError(e.toString()) })
    }

    private fun bindToSendMessage(messageParams: MessageParams) : Disposable {
        return sendChatMessage.getSingle(messageParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { e -> messageLiveData.value = ShowError(e.toString()) })
    }
}