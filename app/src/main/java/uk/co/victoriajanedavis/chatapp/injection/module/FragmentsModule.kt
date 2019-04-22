package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import uk.co.victoriajanedavis.chatapp.injection.component.*
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendfriendrequest.SendFriendRequestFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage.SendMessageFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment

@Module(subcomponents = [
    FriendRequestsToolbarFragmentSubcomponent::class,
    FriendRequestsFragmentSubcomponent::class
])
abstract class FragmentsModule {

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


    /* Subcomponents */

    @Binds
    @IntoMap
    @FragmentKey(FriendRequestsToolbarFragment::class)
    abstract fun bindFriendRequestsToolbarFragmentInjectFactory(builder: FriendRequestsToolbarFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(FriendRequestsFragment::class)
    abstract fun bindFriendRequestsFragmentInjectFactory(builder: FriendRequestsFragmentSubcomponent.Builder):
            AndroidInjector.Factory<out Fragment>

}