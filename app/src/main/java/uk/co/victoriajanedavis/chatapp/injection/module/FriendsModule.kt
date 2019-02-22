package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendItemAction

@Module
class FriendsModule {

    @Provides
    @PerChildFragment
    fun provideFriendItemActionLiveData(): MutableLiveData<FriendItemAction> = MutableLiveData()
}