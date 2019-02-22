package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.ChatsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FriendsModule
import uk.co.victoriajanedavis.chatapp.injection.module.ReceivedFriendRequestsModule
import uk.co.victoriajanedavis.chatapp.injection.module.SentFriendRequestsModule
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsFragment

@PerChildFragment
@Subcomponent(modules = [
    ChatsModule::class
])
interface ChatsFragmentSubcomponent : AndroidInjector<ChatsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ChatsFragment>()
}

@PerChildFragment
@Subcomponent(modules = [
    FriendsModule::class
])
interface FriendsFragmentSubcomponent : AndroidInjector<FriendsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendsFragment>()
}


@PerChildFragment
@Subcomponent(modules = [
    ReceivedFriendRequestsModule::class
])
interface ReceivedFriendRequestsFragmentSubcomponent : AndroidInjector<ReceivedFriendRequestsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ReceivedFriendRequestsFragment>()
}

@PerChildFragment
@Subcomponent(modules = [
    SentFriendRequestsModule::class
])
interface SentFriendRequestsFragmentSubcomponent : AndroidInjector<SentFriendRequestsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SentFriendRequestsFragment>()
}