package uk.co.victoriajanedavis.chatapp.presentation.ui.friends

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import javax.inject.Inject
import android.widget.TextView
import uk.co.victoriajanedavis.chatapp.presentation.ext.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsFragment


class FriendRequestsToolbarFragment : DaggerFragment() {

    @Inject lateinit var viewModel: FriendRequestsToolbarViewModel
    @Inject lateinit var testLiveData: MutableLiveData<Int>

    lateinit var friendRequestsBadge: TextView
    private var friendRequestsCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        attachChildFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
        Log.d("FriendRequestsFragment", "liveData: ${testLiveData.toString()}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_friend_requests_toolbar, menu)
        val menuItem = menu?.findItem(R.id.menu_friend_requests)

        friendRequestsBadge = menuItem?.actionView?.findViewById(R.id.friendRequestsBadge) as TextView
        friendRequestsBadge.setOnClickListener { onOptionsItemSelected(menuItem) }

        setupBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_friend_requests -> { return true }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = "Friends"
    }

    private fun setupBadge() {
        if (friendRequestsCount == 0) friendRequestsBadge.gone()
        else {
            friendRequestsBadge.text = Math.min(friendRequestsCount, 99).toString()
            friendRequestsBadge.visible()
        }
    }

    private fun setupViewModelObserver() {
        viewModel.getFriendRequestsCountLiveData().observe(viewLifecycleOwner) {
            it?.let { friendRequestsCount = it
                Log.d("FriendRequestsFragment", "friendRequestsCount: $friendRequestsCount")
            }
            activity?.invalidateOptionsMenu()
        }
    }

    private fun attachChildFragment() {
        val fm = childFragmentManager
        var fragment = fm.findFragmentById(R.id.friendRequestsFragmentContainer)

        if (fragment == null) {
            fragment = FriendsFragment.newInstance()
            fm.beginTransaction()
                .add(R.id.friendRequestsFragmentContainer, fragment, FriendsFragment.TAG)
                .commit()
        }
    }
}