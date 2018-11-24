package uk.co.victoriajanedavis.chatapp.injection.module

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsFragment

@Module(subcomponents = [
    FriendsFragmentSubcomponent::class
])
abstract class ChildFragmentsModule {

    @Binds
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    abstract fun bindFriendsFragmentInjectorFactory(builder: FriendsFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>
}