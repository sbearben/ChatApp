package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment

@Module
abstract class FriendRequestsModule {

    @Binds
    @IntoMap
    @FragmentKey(ReceivedFriendRequestsFragment::class)
    abstract fun receivedFriendRequestsFragment(fragment: ReceivedFriendRequestsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SentFriendRequestsFragment::class)
    abstract fun sentFriendRequestsFragment(fragment: SentFriendRequestsFragment): Fragment
}