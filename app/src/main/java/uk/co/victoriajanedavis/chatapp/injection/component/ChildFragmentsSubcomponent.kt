package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerChildFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsFragment

@PerChildFragment
@Subcomponent()
interface FriendsFragmentSubcomponent : AndroidInjector<FriendsFragment> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<FriendsFragment>()
}