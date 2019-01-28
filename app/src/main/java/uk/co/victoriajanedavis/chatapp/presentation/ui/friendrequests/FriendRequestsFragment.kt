package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friend_requests.*
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.ext.getSupportActionBar
import uk.co.victoriajanedavis.chatapp.presentation.ext.setSupportActionBar
import javax.inject.Inject

class FriendRequestsFragment : DaggerFragment() {

    @Inject lateinit var pagerAdapter: FriendRequestsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerAdapter = FriendRequestsPagerAdapter(context, childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPager()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view_pager.adapter = null
    }

    private fun setupPager() {
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.fragment_friend_requests_title)
    }
}