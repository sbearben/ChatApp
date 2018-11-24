package uk.co.victoriajanedavis.chatapp.injection.module

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.MainActivitySubcomponent
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity

@Module(subcomponents = [MainActivitySubcomponent::class])
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}