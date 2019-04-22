package uk.co.victoriajanedavis.chatapp.presentation.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ActivityContext
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.ChatsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.FriendsFragment
import javax.inject.Inject

@PerFragment
class FriendRequestsToolbarPagerAdapter @Inject constructor(
    @ActivityContext private val context: Context?,
    private val fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) : Fragment = when(position) {
        0 -> {
            fm.fragmentFactory.instantiate(
                ChatsFragment::class.java.classLoader!!,
                ChatsFragment::class.java.name
            )
        }
        else -> {
            fm.fragmentFactory.instantiate(
                FriendsFragment::class.java.classLoader!!,
                FriendsFragment::class.java.name
            )
        }
    }

    override fun getCount(): Int {
        return NUMBER_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? = when(position) {
        0 -> context?.getString(R.string.friend_requests_toolbar_pager_adapter_chats)
        else -> context?.getString(R.string.friend_requests_toolbar_pager_adapter_friends)
    }

    companion object {
        const val NUMBER_TABS = 2
    }
}