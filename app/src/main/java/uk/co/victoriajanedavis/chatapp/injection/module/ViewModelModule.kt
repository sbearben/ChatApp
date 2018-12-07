package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.ViewModelKey
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.FriendRequestsToolbarViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivityViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory : ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun mainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendRequestsToolbarViewModel::class)
    abstract fun friendRequestsToolbarViewModel(viewModel: FriendRequestsToolbarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun friendsViewModel(viewModel: FriendsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun chatViewModel(viewModel: ChatViewModel): ViewModel
}