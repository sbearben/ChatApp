package uk.co.victoriajanedavis.chatapp.injection.module

import com.google.gson.Gson
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.ChatAppWebSocketService
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.LoggedInLifecycle
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder
import uk.co.victoriajanedavis.chatapp.domain.interactors.IsUserLoggedIn
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@Module
class ChatAppWebSocketModule {

    @Provides
    @ApplicationScope
    fun chatAppWebSocketService(
        protocol: Protocol,
        configuration: Scarlet.Configuration
    ): ChatAppWebSocketService {
        return Scarlet(protocol, configuration)
            .create()
    }

    @Provides
    @ApplicationScope
    fun okHttpWebSocket(okHttpClient: OkHttpClient, tokenHolder: TokenEntityHolder): Protocol {
        return OkHttpWebSocket(okHttpClient,
            OkHttpWebSocket.SimpleRequestFactory(
                { Request.Builder()
                    .url("ws://10.0.2.2:8000/ws/chat/")
                    .addHeader("Authorization", "Token ${tokenHolder.tokenEntity!!}")
                    .build() },
                { ShutdownReason.GRACEFUL }
            ))
    }

    @Provides
    @ApplicationScope
    fun scarletConfiguration(lifecycle: Lifecycle): Scarlet.Configuration {
        return Scarlet.Configuration(
            lifecycle = lifecycle,
            streamAdapterFactories = listOf(RxJava2StreamAdapterFactory())
        )
    }

    @Provides
    @ApplicationScope
    fun scarletLifecycle(application: ChatApp, isUserLoggedIn: IsUserLoggedIn): Lifecycle {
        return AndroidLifecycle.ofApplicationForeground(application)
            .combineWith(LoggedInLifecycle(isUserLoggedIn))
    }
}