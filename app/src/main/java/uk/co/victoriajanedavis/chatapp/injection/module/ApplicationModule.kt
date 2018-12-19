package uk.co.victoriajanedavis.chatapp.injection.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext

@Module
abstract class ApplicationModule {

    @Binds
    @ApplicationContext
    abstract fun provideContext(app: ChatApp) : Context

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
                context.getSharedPreferences("chat_app_shared_pref", Context.MODE_PRIVATE)
    }

}