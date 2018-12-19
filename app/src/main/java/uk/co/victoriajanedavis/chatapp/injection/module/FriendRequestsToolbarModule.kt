package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter.FriendAction

@Module
class FriendRequestsToolbarModule {

    @Provides
    @PerFragment
    fun provideFriendActionLiveData(): MutableLiveData<FriendAction> = MutableLiveData()
}