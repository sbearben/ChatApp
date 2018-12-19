package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsFragment

@Module(subcomponents = [
    FriendsFragmentSubcomponent::class
])
abstract class FriendRequestsToolbarChildFragmentsModule {

    @Binds
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    abstract fun bindFriendsFragmentInjectorFactory(builder: FriendsFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>
}


@Module(subcomponents = [
    ReceivedFriendRequestsFragmentSubcomponent::class,
    SentFriendRequestsFragmentSubcomponent::class
])
abstract class FriendRequestsChildFragmentsModule {

    @Binds
    @IntoMap
    @FragmentKey(ReceivedFriendRequestsFragment::class)
    abstract fun bindReceivedFriendRequestsFragmentInjectorFactory(
        builder: ReceivedFriendRequestsFragmentSubcomponent.Builder
    ): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SentFriendRequestsFragment::class)
    abstract fun bindSentFriendRequestsFragmentInjectorFactory(
        builder: SentFriendRequestsFragmentSubcomponent.Builder
    ): AndroidInjector.Factory<out Fragment>
}