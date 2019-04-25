package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import uk.co.victoriajanedavis.chatapp.TestChatApp
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepositoryTest
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.store.RecentMessagesReactiveStoreTest
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.module.*

@ApplicationScope
@Component(modules = [
    AndroidSupportInjectionModule::class,
    UnitTestApplicationModule::class,
    MockChatAppServiceModule::class,
    FakeDatabaseModule::class,
    ReactiveStoreModule::class,
    FragmentsModule::class
])
interface UnitTestApplicationComponent : ApplicationComponent {

    fun inject(test: RecentMessagesReactiveStoreTest)
    fun inject(test: MessageRepositoryTest)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: TestChatApp): Builder

        fun build(): UnitTestApplicationComponent
    }
}