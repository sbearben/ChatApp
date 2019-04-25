package uk.co.victoriajanedavis.chatapp.presentation.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import javax.inject.Inject
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_friend_requests_toolbar.*
import kotlinx.android.synthetic.main.layout_content_progress_dim.*
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import java.lang.ref.WeakReference

class FriendRequestsToolbarFragment : DaggerFragment() {

    @Inject lateinit var injectingFragmentFactory: InjectingFragmentFactory
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var pagerAdapter: FriendRequestsToolbarPagerAdapter
    private lateinit var viewModel: FriendRequestsToolbarViewModel

    private var badgeActionView: View? = null
    private lateinit var friendRequestsBadge: TextView
    private var friendRequestsCount: Int = 0

    private var logoutUserMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = injectingFragmentFactory
        pagerAdapter = FriendRequestsToolbarPagerAdapter(context, childFragmentManager)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendRequestsToolbarViewModel::class.java)
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPagerAdapter()

        setupIsUserLoggedInObserver()
        setupLogoutUserViewModelObserver()
        setupViewModelObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.adapter = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_friend_requests_toolbar, menu)

        logoutUserMenuItem = menu.findItem(R.id.menu_logout)

        menu.findItem(R.id.menu_friend_requests).also { menuItem ->
            friendRequestsBadge = menuItem.actionView.findViewById(R.id.friend_requests_badge) as TextView
            badgeActionView = menuItem.actionView
        }

        badgeActionView?.setOnClickListener(BadgeClickListener(WeakReference(this)))
        setupBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_logout -> {
            logoutUser()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupPagerAdapter() {
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayShowTitleEnabled(true)
            title = "ChatApp"
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
                false -> findNavController().navigate(R.id.action_homeFragment_to_loginFlowGraph)
                true -> {}
            }
        }
    }

    private fun setupViewModelObserver() {
        viewModel.getFriendRequestsCountLiveData().observe(viewLifecycleOwner) {
            when(it) {
                is ShowContent -> {
                    friendRequestsCount = it.content
                    activity?.invalidateOptionsMenu()
                }
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
                   progressBarLayout.visible()
               }
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

    companion object {
        class BadgeClickListener constructor(
            private val fragmentReference: WeakReference<Fragment>
        ) : View.OnClickListener {
            override fun onClick(view: View?) {
                fragmentReference.get()?.apply {
                    findNavController().navigate(R.id.action_homeFragment_to_friendRequestsFragment)
                }
            }
        }
    }
}