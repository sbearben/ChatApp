package uk.co.victoriajanedavis.chatapp.injection.module

import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@Module
class MockChatAppServiceModule {

    @Provides
    @ApplicationScope
    fun chatAppService(): ChatAppService = Mockito.mock(ChatAppService::class.java)
}