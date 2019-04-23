package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.ViewModelKey
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerActivity
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendfriendrequest.SendFriendRequestViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage.SendMessageViewModel
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupViewModel

@Module
abstract class ActivitiesViewModelModule {

    @Binds @PerActivity
    abstract fun bindViewModelFactory(viewModelFactory : ViewModelFactory): ViewModelProvider.Factory

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
    @ViewModelKey(SignupViewModel::class)
    abstract fun signupViewModel(viewModel: SignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendRequestsToolbarViewModel::class)
    abstract fun friendRequestsToolbarViewModel(viewModel: FriendRequestsToolbarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun chatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SendFriendRequestViewModel::class)
    abstract fun sendFriendRequestViewModel(viewModel: SendFriendRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SendMessageViewModel::class)
    abstract fun sendMessageViewModel(viewModel: SendMessageViewModel): ViewModel
}


@Module
abstract class FriendRequestsToolbarViewModelModule {

    @Binds @PerFragment
    abstract fun bindViewModelFactory(viewModelFactory : ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    abstract fun chatsViewModel(viewModel: ChatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun friendsViewModel(viewModel: FriendsViewModel): ViewModel
}


@Module
abstract class FriendRequestsViewModelModule {

    @Binds @PerFragment
    abstract fun bindViewModelFactory(viewModelFactory : ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReceivedFriendRequestsViewModel::class)
    abstract fun receivedFriendRequestsViewModel(viewModel: ReceivedFriendRequestsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SentFriendRequestsViewModel::class)
    abstract fun sentFriendRequestsViewModel(viewModel: SentFriendRequestsViewModel): ViewModel
}