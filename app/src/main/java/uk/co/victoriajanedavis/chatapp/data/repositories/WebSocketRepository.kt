package uk.co.victoriajanedavis.chatapp.data.repositories

import android.util.Log
import com.tinder.scarlet.websocket.WebSocketEvent.*
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbChatDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageWsDbMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class WebSocketRepository @Inject constructor(
    private val webSocketService: ChatAppWebSocketService,
    private val messageStore: MessageReactiveStore,
    private val chatStore: BaseReactiveStore<ChatDbModel>
) {
    private val disposables = CompositeDisposable()
    private val messageMapper = MessageWsDbMapper()
    private val chatMapper = MessageDbChatDbMapper()


    fun initializeStreams() {
        disposables.addAll(
            subscribeToWebSocketEventStraeam(),
            subscribeToMessagesStream()
        )
    }

    fun clearStreams() {
        disposables.clear()
    }

    private fun subscribeToWebSocketEventStraeam(): Disposable {
        return webSocketService.observeWebSocketEvent()
            .subscribeOn(Schedulers.io())
            .subscribe({ event -> when(event) {
                is OnConnectionOpened -> Log.d("WebSocketRep", "Connection Opened")
                is OnMessageReceived -> Log.d("WebSocketRep", "Message Received")
                is OnConnectionClosing -> Log.d("WebSocketRep", "Connection Closing")
                is OnConnectionClosed -> Log.d("WebSocketRep", "Connection Closed")
                is OnConnectionFailed -> Log.d("WebSocketRep", "Connection Failed")
            }},
                { throwable: Throwable -> Log.d("WebSocketRep", throwable.message)
            })
    }

    private fun subscribeToMessagesStream(): Disposable {
        return webSocketService.observeMessages()
            .map(messageMapper::mapFrom)
            .flatMapCompletable { messageDbModel -> Completable.mergeArray(
                messageStore.storeSingular(messageDbModel),
                chatStore.storeSingular(chatMapper.mapFrom(messageDbModel))
            )}
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    /*
    private fun subscribeToMessagesStream(): Disposable {
        return webSocketService.observeMessages()
            .map(messageMapper::mapFrom)
            .flatMapCompletable(messageStore::storeSingular)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
    */
}