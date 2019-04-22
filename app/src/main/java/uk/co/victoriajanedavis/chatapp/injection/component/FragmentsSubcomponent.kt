package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsToolbarModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsToolbarViewModelModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsViewModelModule
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarFragment

@PerFragment
@Subcomponent(modules = [
    FriendRequestsToolbarModule::class,
    FriendRequestsToolbarViewModelModule::class
])
interface FriendRequestsToolbarFragmentSubcomponent : AndroidInjector<FriendRequestsToolbarFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendRequestsToolbarFragment>()
}


@PerFragment
@Subcomponent(modules = [
    FriendRequestsModule::class,
    FriendRequestsViewModelModule::class
])
interface FriendRequestsFragmentSubcomponent : AndroidInjector<FriendRequestsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendRequestsFragment>()
}