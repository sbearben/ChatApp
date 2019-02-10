package uk.co.victoriajanedavis.chatapp.injection.module

import android.app.Service
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.MyFirebaseServiceSubcomponent
import uk.co.victoriajanedavis.chatapp.injection.component.ReplyActionServiceSubcomponent
import uk.co.victoriajanedavis.chatapp.injection.component.SyncServiceSubcomponent
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ServiceContext
import uk.co.victoriajanedavis.chatapp.presentation.fcm.MyFirebaseService
import uk.co.victoriajanedavis.chatapp.presentation.notifications.message.ReplyActionService
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.SyncService


@Module(subcomponents = [MyFirebaseServiceSubcomponent::class])
abstract class MyFirebaseServiceModule {

    @Binds
    @IntoMap
    @ServiceKey(MyFirebaseService::class)
    abstract fun bindMyFirebaseServiceInjectorFactory(builder: MyFirebaseServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}


@Module(subcomponents = [SyncServiceSubcomponent::class])
abstract class SyncServiceModule {

    @Binds
    @IntoMap
    @ServiceKey(SyncService::class)
    abstract fun bindSyncServiceInjectorFactory(builder: SyncServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}


@Module(subcomponents = [ReplyActionServiceSubcomponent::class])
abstract class ReplyActionServiceModule {

    @Binds
    @IntoMap
    @ServiceKey(ReplyActionService::class)
    abstract fun bindReplyActionServiceInjectorFactory(builder: ReplyActionServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}