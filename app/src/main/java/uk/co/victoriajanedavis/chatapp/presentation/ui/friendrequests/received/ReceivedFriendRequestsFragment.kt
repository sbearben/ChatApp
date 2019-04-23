package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_friend_requests_tab.*
import kotlinx.android.synthetic.main.layout_message.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseFragment
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestViewHolder
import uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.received.adapter.ReceivedFriendRequestsAdapter
import java.util.*
import javax.inject.Inject

@SuppressLint("ValidFragment")
class ReceivedFriendRequestsFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseFragment(), ReceivedFriendRequestViewHolder.OnClickListener {

    private val adapter = ReceivedFriendRequestsAdapter(this)
    private lateinit var viewModel: ReceivedFriendRequestsViewModel


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

    override fun onAcceptClicked(senderUserUuid: UUID) = viewModel.acceptFriendRequest(senderUserUuid)

    override fun onRejectClicked(senderUserUuid: UUID) = viewModel.rejectFriendRequest(senderUserUuid)

    private fun setupRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshItems()
        }
    }

    private fun setupViewModelStateObserver() {
        viewModel.getReceivedFriendRequestsLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: ListState<List<FriendshipEntity>>) = when(state) {
        is ShowContent -> showContent(state.content)
        is ShowLoading -> {}
        is StopLoading -> swipeRefreshLayout.isRefreshing = false
        is ShowError -> showError(state.message)
        is ShowEmpty -> showEmpty()
    }

    private fun showContent(content: List<FriendshipEntity>) {
        adapter.submitList(content)
        message_layout.gone()
    }

    private fun showEmpty() {
        adapter.submitList(ArrayList())
        message_layout.visible()
        message_button.gone()
        message_imageview.setImageResource(R.drawable.ic_friend_request_none_72dp)
        message_textview.setText(R.string.error_no_items_to_display)
    }

    private fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_LONG)
        swipeRefreshLayout.isRefreshing = false
    }
}