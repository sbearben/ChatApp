package uk.co.victoriajanedavis.chatapp.injection.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.data.services.QueryConverterFactory
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@Module(includes = [NetworkModule::class])
object ChatAppServiceModule {

    @Provides @JvmStatic
    @ApplicationScope
    fun chatAppService(retrofit: Retrofit): ChatAppService =  retrofit.create(ChatAppService::class.java)

    @Provides @JvmStatic
    @ApplicationScope
    fun retrofit(okHttpClient: OkHttpClient,
                 gson: Gson,
                 queryConverterFactory: QueryConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(queryConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides @JvmStatic
    @ApplicationScope
    fun gson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    @Provides @JvmStatic
    @ApplicationScope
    fun tokenEntityHolder(): TokenEntityHolder {
        return TokenEntityHolder()
    }
}