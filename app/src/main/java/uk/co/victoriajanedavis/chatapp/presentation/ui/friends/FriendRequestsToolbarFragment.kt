package uk.co.victoriajanedavis.chatapp.presentation.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import javax.inject.Inject
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_content_progress_dim.*
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
    private lateinit var viewModel: FriendRequestsToolbarViewModel

    private var badgeLayout: View? = null
    private lateinit var friendRequestsBadge: TextView
    private var friendRequestsCount: Int = 0

    private var logoutUserMenuItem: MenuItem? = null


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
        setupIsUserLoggedInObserver()
        setupLogoutUserViewModelObserver()
        setupViewModelObserver()
        //viewModel.requestItems()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_friend_requests_toolbar, menu)

        logoutUserMenuItem = menu.findItem(R.id.menu_logout)

        val friendRequestsMenuItem = menu.findItem(R.id.menu_friend_requests)
        friendRequestsBadge = friendRequestsMenuItem.actionView.findViewById(R.id.friend_requests_badge) as TextView

        badgeLayout = friendRequestsMenuItem.actionView.findViewById(R.id.friend_requests_badge_layout) as View
        badgeLayout?.setOnClickListener { findNavController().navigate(R.id.action_friendsFragment_to_friendRequestsFragment) }

        setupBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_friend_requests -> {
            findNavController().navigate(R.id.action_friendsFragment_to_friendRequestsFragment)
            true
        }
        R.id.menu_logout -> {
            logoutUser()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        badgeLayout?.setOnClickListener(null)
        badgeLayout = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayShowTitleEnabled(true)
            title = "Friends"
        }
    }

    private fun logoutUser() {
        if(logoutUserMenuItem?.isEnabled ?: false) {
            viewModel.logoutUser()
        }
    }

    private fun setupBadge() {
        if (friendRequestsCount == 0) friendRequestsBadge.gone()
        else {
            friendRequestsBadge.text = Math.min(friendRequestsCount, 99).toString()
            friendRequestsBadge.visible()
        }
    }

    private fun setupIsUserLoggedInObserver() {
        //viewModel.getIsUserLoggedInLiveData().observe(this) {
        viewModel.getIsUserLoggedInLiveData().observe(viewLifecycleOwner) {
            when(it) {
                false -> findNavController().navigate(R.id.action_friendsFragment_to_loginFlowGraph)
                true -> {} //findNavController().navigate(R.id.action_mainFragment_to_friendsFragment)
            }
        }
    }

    private fun setupViewModelObserver() {
        viewModel.getFriendRequestsCountLiveData().observe(viewLifecycleOwner) {
            when(it) {
                is ShowContent -> onFriendRequestsCountReceived(it.content)
                is ShowLoading -> {}
                is ShowError -> showError(it.message)
            }
        }
    }

    private fun setupLogoutUserViewModelObserver() {
        viewModel.getLogoutUserLiveData().observe(viewLifecycleOwner) {
           when(it) {
               is ShowContent -> {}  // IsUserLoggedInLiveData observer will handle this (token is deleted trigger)
               is ShowLoading -> {
                   logoutUserMenuItem?.isEnabled = false
                   progressBarLayout.visible() }
               is ShowError -> {
                   logoutUserMenuItem?.isEnabled = true
                   progressBarLayout.gone()
                   showError(it.message)
               }
           }
        }
    }

    private fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_INDEFINITE)
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
            val bundle = ChatFragment.createBundle(
                action.chatEntity.uuid.toString(),
                action.chatEntity.friendship!!.username,
                action.sharedTextView.transitionName
            )

            val extras = FragmentNavigatorExtras(
                action.sharedTextView to ViewCompat.getTransitionName(action.sharedTextView)!!
            )
            findNavController().navigate(R.id.action_friendsFragment_to_chatFragment, bundle, null, extras)
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