package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsFragment

@Module
abstract class FriendRequestsToolbarModule {

    @Binds
    @IntoMap
    @FragmentKey(ChatsFragment::class)
    abstract fun chatsFragment(fragment: ChatsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    abstract fun friendsFragment(fragment: FriendsFragment): Fragment
}