package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter.ChatItemAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter.ChatsAdapter
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendItemAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendsAdapter

@Module
object FriendRequestsToolbarModule {

    @Provides @JvmStatic
    @IntoMap
    @FragmentKey(ChatsFragment::class)
    fun chatsFragment(viewModelFactory: ViewModelFactory): Fragment {
        val liveData = MutableLiveData<ChatItemAction>()
        return ChatsFragment(
            viewModelFactory = viewModelFactory,
            actionLiveData = liveData,
            adapter = ChatsAdapter(liveData)
        )
    }

    @Provides @JvmStatic
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    fun friendsFragment(viewModelFactory: ViewModelFactory): Fragment {
        val liveData = MutableLiveData<FriendItemAction>()
        return FriendsFragment(
            viewModelFactory = viewModelFactory,
            actionLiveData = liveData,
            adapter = FriendsAdapter(liveData)
        )
    }

    @Provides @JvmStatic
    fun provideChildFragmentManager(
        fragment: FriendRequestsToolbarFragment
    ): FragmentManager = fragment.childFragmentManager
}