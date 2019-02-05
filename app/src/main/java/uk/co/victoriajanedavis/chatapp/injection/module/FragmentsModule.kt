package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.send.SendFriendRequestFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment

@Module(subcomponents = [
    LoginFragmentSubcomponent::class,
    SignupFragmentSubcomponent::class,
    FriendRequestsToolbarFragmentSubcomponent::class,
    FriendsFragmentSubcomponent::class,
    ChatFragmentSubcomponent::class,
    FriendRequestsFragmentSubcomponent::class,
    SendFriendRequestFragmentSubcomponent::class
])
abstract class FragmentsModule {

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragmentInjectorFactory(builder: LoginFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SignupFragment::class)
    abstract fun bindSignupFragmentInjectorFactory(builder: SignupFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(FriendRequestsToolbarFragment::class)
    abstract fun bindFriendRequestsToolbarFragmentInjectFactory(builder: FriendRequestsToolbarFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(ChatFragment::class)
    abstract fun bindChatFragmentInjectorFactory(builder: ChatFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(FriendRequestsFragment::class)
    abstract fun bindFriendRequestsFragmentInjectFactory(builder: FriendRequestsFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(SendFriendRequestFragment::class)
    abstract fun bindSendFriendRequestFragmentInjectFactory(builder: SendFriendRequestFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

}