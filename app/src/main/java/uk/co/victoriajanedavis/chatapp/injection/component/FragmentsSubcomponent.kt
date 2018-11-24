package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.ChildFragmentsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendClickedModule
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainFragment

@Subcomponent()
interface MainFragmentSubcomponent : AndroidInjector<MainFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MainFragment>()
}

@Subcomponent()
interface LoginFragmentSubcomponent : AndroidInjector<LoginFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<LoginFragment>()
}

@Subcomponent()
interface SignupFragmentSubcomponent : AndroidInjector<SignupFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SignupFragment>()
}

@PerFragment
@Subcomponent(modules = [
    FriendClickedModule::class,
    ChildFragmentsModule::class
])
interface FriendRequestsToolbarFragmentSubcomponent : AndroidInjector<FriendRequestsToolbarFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendRequestsToolbarFragment>()
}