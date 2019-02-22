package uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage.MessageParams
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import java.util.UUID
import javax.inject.Inject

class SendMessageViewModel @Inject constructor(
    private val sendMessage: SendChatMessage
) : ViewModel() {

    private val messageLiveData = MutableLiveData<State<MessageEntity>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getSendMessageLiveData(): LiveData<State<MessageEntity>> = messageLiveData

    fun sendMessage(chatUuid: UUID, message: String) {
        messageLiveData.value = ShowLoading
        compositeDisposable.clear()
        compositeDisposable.add(bindToUseCase(MessageParams(chatUuid, message)))
    }

    private fun bindToUseCase(messageParams: MessageParams) : Disposable {
        return sendMessage.getSingle(messageParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { message -> messageLiveData.value = ShowContent(message) },
                { e ->
                    messageLiveData.value = ShowError(e.message ?: e.toString())
                    messageLiveData.value = null  // Clear it so that on rotation the error isn't shown again
                }
            )
    }
}