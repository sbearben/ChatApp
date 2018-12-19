package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ActivityContext
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.ReceivedFriendRequestsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.SentFriendRequestsFragment
import javax.inject.Inject

@PerFragment
class FriendRequestsPagerAdapter @Inject constructor(
    @ActivityContext private val context: Context?,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) : Fragment = when(position) {
        0 -> ReceivedFriendRequestsFragment.newInstance()
        else -> SentFriendRequestsFragment.newInstance()
    }

    override fun getCount(): Int {
        return NUMBER_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? = when(position) {
        0 -> context?.getString(R.string.friend_requests_pager_adapter_received)
        else -> context?.getString(R.string.friend_requests_pager_adapter_sent)
    }

    companion object {
        const val NUMBER_TABS = 2
    }
}