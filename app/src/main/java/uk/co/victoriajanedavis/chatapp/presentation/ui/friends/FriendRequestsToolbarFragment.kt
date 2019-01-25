package uk.co.victoriajanedavis.chatapp.presentation.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import android.util.Log
import android.view.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import javax.inject.Inject
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.FriendsFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter.FriendAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter.FriendAction.Clicked


class FriendRequestsToolbarFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var friendActionLiveData: MutableLiveData<FriendAction>
    lateinit var viewModel: FriendRequestsToolbarViewModel


    lateinit var friendRequestsBadge: TextView
    private var friendRequestsCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendRequestsToolbarViewModel::class.java)

        setHasOptionsMenu(true)
        attachChildFragment()

        /**
         *  We are calling setupFriendActionObserver() in onCreate() and using 'this' as the lifecycle owner in
         *  friendActionLiveData.observe since if we don't, the 'Up' button won't work in ChatFragment, and pressing
         *  the back button will crash the app since if we setup this observer in onViewCreated, the last action will
         *  be immediately emitted after we press back in ChatFragment, causing another call to
         *  findNavController().navigate(R.id.action_friendsFragment_to_chatFragment, bundle) which will be invalid
         *  since I believe the immediate emission doesn't allow the NavGraph to recognize this fragment as the "current"
         *  fragment (meaning that call to navigate(R.id.action_friendsFragment_to_chatFragment) will be made when the
         *  NavGraph still thinks ChatFragment is the "current" fragment - i.e. it's invalid causing the crash.
         */
        setupFriendActionObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
        viewModel.requestItems()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_friend_requests_toolbar, menu)
        val menuItem = menu.findItem(R.id.menu_friend_requests)

        friendRequestsBadge = menuItem.actionView.findViewById(R.id.friend_requests_badge) as TextView

        val badgeLayout =  menuItem.actionView.findViewById(R.id.friend_requests_badge_layout) as View
        badgeLayout.setOnClickListener { onOptionsItemSelected(menuItem) }

        setupBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_friend_requests -> {
                findNavController().navigate(R.id.action_friendsFragment_to_friendRequestsFragment)
                return true
            }
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
            it?.let { state ->
                when(state) {
                    is ShowContent -> onFriendRequestsCountReceived(state.content)
                    is ShowLoading -> {}
                    is ShowError -> showError(state.message)
                }
            }
        }
    }

    private fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    private fun onFriendRequestsCountReceived(count: Int) {
        friendRequestsCount = count
        Log.d("FriendRequestsFragment", "friendRequestsCount: $friendRequestsCount")
        activity?.invalidateOptionsMenu()
    }

    private fun setupFriendActionObserver() {
        friendActionLiveData.observe(this) {
            it?.let(::onFriendActionReceived)
        }
    }

    private fun onFriendActionReceived(action: FriendAction) = when(action) {
        is Clicked -> {
            val bundle = ChatFragment.createBundle(action.chatEntity.uuid.toString(),
                action.chatEntity.friendship!!.username)
            findNavController().navigate(R.id.action_friendsFragment_to_chatFragment, bundle)
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