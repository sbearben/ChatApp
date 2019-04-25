package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friend_requests.*
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.getSupportActionBar
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.setSupportActionBar
import javax.inject.Inject

class FriendRequestsFragment : DaggerFragment() {

    @Inject lateinit var injectingFragmentFactory: InjectingFragmentFactory
    private lateinit var pagerAdapter: FriendRequestsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = injectingFragmentFactory
        pagerAdapter = FriendRequestsPagerAdapter(context, childFragmentManager)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPagerAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.adapter = null
    }

    private fun setupPagerAdapter() {
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.fragment_friend_requests_title)
        }
    }
}