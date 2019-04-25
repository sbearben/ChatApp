package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendfriendrequest.SendFriendRequestFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage.SendMessageFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment

@Module
abstract class FragmentsTestModule {

    @Binds
    abstract fun bindFragmentFactory(viewModelFactory : InjectingFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragmentInjectorFactory(loginFragment: LoginFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SignupFragment::class)
    abstract fun bindSignupFragment(signupFragment: SignupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ChatFragment::class)
    abstract fun bindChatFragment(chatFragment: ChatFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SendFriendRequestFragment::class)
    abstract fun bindSendFriendRequestFragment(sendFriendRequestFragment: SendFriendRequestFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SendMessageFragment::class)
    abstract fun bindMessageFragment(sendMessageFragment: SendMessageFragment): Fragment


    /* FriendRequestsToolbarFragment */

    @Binds
    @IntoMap
    @FragmentKey(ChatsFragment::class)
    abstract fun chatsFragment(fragment: ChatsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FriendsFragment::class)
    abstract fun friendsFragment(fragment: FriendsFragment): Fragment


    /* FriendRequestsFragment */

    @Binds
    @IntoMap
    @FragmentKey(ReceivedFriendRequestsFragment::class)
    abstract fun receivedFriendRequestsFragment(fragment: ReceivedFriendRequestsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SentFriendRequestsFragment::class)
    abstract fun sentFriendRequestsFragment(fragment: SentFriendRequestsFragment): Fragment
}