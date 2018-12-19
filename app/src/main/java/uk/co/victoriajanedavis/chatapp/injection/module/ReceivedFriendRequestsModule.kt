package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestAction

@Module
class ReceivedFriendRequestsModule {

    @Provides
    @PerChildFragment
    fun provideReceivedFriendRequestActionLiveData(): MutableLiveData<ReceivedFriendRequestAction> = MutableLiveData()
}