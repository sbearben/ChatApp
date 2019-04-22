package uk.co.victoriajanedavis.chatapp.injection.module

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import uk.co.victoriajanedavis.chatapp.data.services.AuthenticationInterceptor
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                     authenticationInterceptor: AuthenticationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(loggingInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @ApplicationScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}