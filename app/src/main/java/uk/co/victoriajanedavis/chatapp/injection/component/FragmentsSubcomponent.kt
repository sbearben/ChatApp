package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsChildFragmentsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsToolbarChildFragmentsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendRequestsToolbarModule
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.FriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendfriendrequest.SendFriendRequestFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.FriendRequestsToolbarFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage.SendMessageFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.signup.SignupFragment

@Subcomponent()
interface LoginFragmentSubcomponent : AndroidInjector<LoginFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<LoginFragment>()
}

@Subcomponent()
interface SignupFragmentSubcomponent : AndroidInjector<SignupFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SignupFragment>()
}

@PerFragment
@Subcomponent(modules = [
    FriendRequestsToolbarModule::class,
    FriendRequestsToolbarChildFragmentsModule::class
])
interface FriendRequestsToolbarFragmentSubcomponent : AndroidInjector<FriendRequestsToolbarFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendRequestsToolbarFragment>()
}

@PerFragment
@Subcomponent()
interface ChatFragmentSubcomponent : AndroidInjector<ChatFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ChatFragment>()
}

@PerFragment
@Subcomponent(modules = [
    FriendRequestsModule::class,
    FriendRequestsChildFragmentsModule::class
])
interface FriendRequestsFragmentSubcomponent : AndroidInjector<FriendRequestsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendRequestsFragment>()
}

@Subcomponent()
interface SendFriendRequestFragmentSubcomponent : AndroidInjector<SendFriendRequestFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SendFriendRequestFragment>()
}

@Subcomponent()
interface SendMessageFragmentSubcomponent : AndroidInjector<SendMessageFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SendMessageFragment>()
}