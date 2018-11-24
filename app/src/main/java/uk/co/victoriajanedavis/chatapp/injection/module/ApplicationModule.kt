package uk.co.victoriajanedavis.chatapp.injection.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.ChatApp

@Module
abstract class ApplicationModule {

    @Binds
    abstract fun provideContext(app: ChatApp) : Context

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences =
                context.getSharedPreferences("chat_app_shared_pref", Context.MODE_PRIVATE)
    }

}