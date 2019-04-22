package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestsAdapter
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter.SentFriendRequestAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.adapter.SentFriendRequestsAdapter

@Module
object FriendRequestsModule {

    @Provides @JvmStatic
    @IntoMap
    @FragmentKey(ReceivedFriendRequestsFragment::class)
    fun receivedFriendRequestsFragment(viewModelFactory: ViewModelFactory): Fragment {
        val liveData = MutableLiveData<ReceivedFriendRequestAction>()
        return ReceivedFriendRequestsFragment(
            viewModelFactory = viewModelFactory,
            actionLiveData = liveData,
            adapter = ReceivedFriendRequestsAdapter(liveData)
        )
    }

    @Provides @JvmStatic
    @IntoMap
    @FragmentKey(SentFriendRequestsFragment::class)
    fun sentFriendRequestsFragment(viewModelFactory: ViewModelFactory): Fragment {
        val liveData = MutableLiveData<SentFriendRequestAction>()
        return SentFriendRequestsFragment(
            viewModelFactory = viewModelFactory,
            actionLiveData = liveData,
            adapter = SentFriendRequestsAdapter(liveData)
        )
    }

    @Provides @JvmStatic
    fun provideChildFragmentManager(fragment: FriendRequestsFragment): FragmentManager = fragment.childFragmentManager
}