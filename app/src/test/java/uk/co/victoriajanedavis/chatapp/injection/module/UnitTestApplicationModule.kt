package uk.co.victoriajanedavis.chatapp.injection.module

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext

@Module
abstract class UnitTestApplicationModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @ApplicationContext
        fun provideContext(): Context {
            return ApplicationProvider.getApplicationContext<ChatApp>()
        }
    }

}