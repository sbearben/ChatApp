package uk.co.victoriajanedavis.chatapp.injection.module

import android.arch.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment

@Module
class FriendClickedModule {

    @Provides
    @PerFragment
    fun provideFriendClickedLiveData(): MutableLiveData<Int> = MutableLiveData()
}