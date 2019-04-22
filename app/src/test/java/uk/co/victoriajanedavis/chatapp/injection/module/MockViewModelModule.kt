package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.mockito.Mockito
import uk.co.victoriajanedavis.chatapp.injection.ViewModelKey
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
abstract class MockViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory : ViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        fun mainViewModel(): ViewModel = Mockito.mock(MainViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        fun loginViewModel(): ViewModel = Mockito.mock(LoginViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(SignupViewModel::class)
        fun signupViewModel(): ViewModel = Mockito.mock(SignupViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(FriendRequestsToolbarViewModel::class)
        fun friendRequestsToolbarViewModel(): ViewModel
                = Mockito.mock(FriendRequestsToolbarViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(ChatsViewModel::class)
        fun chatsViewModel(): ViewModel = Mockito.mock(ChatsViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(FriendsViewModel::class)
        fun friendsViewModel(): ViewModel = Mockito.mock(FriendsViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(ChatViewModel::class)
        fun chatViewModel(): ViewModel = Mockito.mock(ChatViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(ReceivedFriendRequestsViewModel::class)
        fun receivedFriendRequestsViewModel(): ViewModel
                = Mockito.mock(ReceivedFriendRequestsViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(SentFriendRequestsViewModel::class)
        fun sentFriendRequestsViewModel(): ViewModel
                = Mockito.mock(SentFriendRequestsViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(SendFriendRequestViewModel::class)
        fun sendFriendRequestViewModel(): ViewModel
                = Mockito.mock(SendFriendRequestViewModel::class.java)

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(SendMessageViewModel::class)
        fun sendMessageViewModel(): ViewModel
                = Mockito.mock(SendMessageViewModel::class.java)
    }
}