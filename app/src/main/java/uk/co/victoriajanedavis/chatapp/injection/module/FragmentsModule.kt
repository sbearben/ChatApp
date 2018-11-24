package uk.co.victoriajanedavis.chatapp.injection.module

import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainFragment

@Module(subcomponents = [
    MainFragmentSubcomponent::class,
    LoginFragmentSubcomponent::class,
    SignupFragmentSubcomponent::class,
    FriendRequestsToolbarFragmentSubcomponent::class,
    FriendsFragmentSubcomponent::class
])
abstract class FragmentsModule {
    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    abstract fun bindMainFragmentInjectorFactory(builder: MainFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragmentInjectorFactory(builder: LoginFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SignupFragment::class)
    abstract fun bindSignupFragmentInjectorFactory(builder: SignupFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(FriendRequestsToolbarFragment::class)
    abstract fun bindFriendRequestsToolbarFragmentInjectFactory(builder: FriendRequestsToolbarFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>
}