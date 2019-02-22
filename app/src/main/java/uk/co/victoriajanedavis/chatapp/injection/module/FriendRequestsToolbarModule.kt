package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarFragment

@Module
class FriendRequestsToolbarModule {

    @Provides
    fun provideChildFragmentManager(
        fragment: FriendRequestsToolbarFragment
    ): FragmentManager = fragment.childFragmentManager

    /*
    @Provides
    @PerFragment
    fun provideChatItemActionLiveData(): MutableLiveData<ChatItemAction> = MutableLiveData()
    */
}