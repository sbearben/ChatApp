package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment

@Module
class FriendRequestsModule {

    @Provides
    fun provideChildFragmentManager(fragment: FriendRequestsFragment): FragmentManager = fragment.childFragmentManager
}