package uk.co.victoriajanedavis.chatapp.injection.module

import android.app.Service
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.MyFirebaseServiceSubcomponent
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ServiceContext
import uk.co.victoriajanedavis.chatapp.presentation.fcm.MyFirebaseService

@Module(subcomponents = [MyFirebaseServiceSubcomponent::class])
abstract class MyFirebaseServiceModule {

    @Binds
    @ServiceContext
    abstract fun provideContext(service: MyFirebaseService) : Context

    @Binds
    @IntoMap
    @ServiceKey(MyFirebaseService::class)
    abstract fun bindMyFirebaseServiceInjectorFactory(builder: MyFirebaseServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}