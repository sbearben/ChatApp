package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friend_requests_tab.*
import kotlinx.android.synthetic.main.layout_message.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.gone
import uk.co.victoriajanedavis.chatapp.presentation.ext.observe
import uk.co.victoriajanedavis.chatapp.presentation.ext.visible
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestsAdapter
import javax.inject.Inject

class ReceivedFriendRequestsFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var actionLiveData: MutableLiveData<ReceivedFriendRequestAction>
    @Inject lateinit var adapter: ReceivedFriendRequestsAdapter
    lateinit var viewModel: ReceivedFriendRequestsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceivedFriendRequestsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_requests_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModelStateObserver()
    }

    private fun setupRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
    }

    private fun setupViewModelStateObserver() {
        viewModel.getReceiviedFriendRequestsLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: ListState<List<FriendshipEntity>>) = when(state) {
        is ShowContent -> {
            message_layout.gone()
            adapter.submitList(state.content)
        }
        is ShowLoading -> {}
        is ShowError -> showError(state.message)
        is ShowEmpty -> showEmpty()
    }

    private fun showEmpty() {
        message_layout.visible()
        message_button.gone()
        message_imageview.setImageResource(R.drawable.ic_friend_request_none_48dp)
        message_textview.setText(R.string.error_no_items_to_display)
    }

    private fun showError(message: String) {
        message_layout.visible()
        message_button.visible()
        message_imageview.setImageResource(R.drawable.ic_error_outline_black_48dp)
        message_textview.text = message
    }

    companion object {
        fun newInstance() = ReceivedFriendRequestsFragment()
    }
}