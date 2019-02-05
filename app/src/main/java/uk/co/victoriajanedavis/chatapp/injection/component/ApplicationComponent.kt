package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.module.*

@ApplicationScope
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ChatAppServiceModule::class,
    ChatAppWebSocketModule::class,
    DatabaseModule::class,
    MainActivityModule::class,
    MyFirebaseServiceModule::class,
    NetworkModule::class,
    ReactiveStoreModule::class,
    SyncServiceModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(app: ChatApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: ChatApp): Builder

        fun build(): ApplicationComponent
    }
}