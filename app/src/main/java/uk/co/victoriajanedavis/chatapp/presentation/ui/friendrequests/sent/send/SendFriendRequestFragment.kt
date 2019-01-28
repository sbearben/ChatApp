package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.send

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friend_requests_tab.*
import kotlinx.android.synthetic.main.layout_message.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.*
import javax.inject.Inject

class SendFriendRequestFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SendFriendRequestViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendFriendRequestViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_friend_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelStateObserver()
    }

    private fun setupViewModelStateObserver() {
        viewModel.getRequestLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: State<Unit>) = when(state) {
        is ShowContent -> showContent()
        is ShowLoading -> {}
        is ShowError -> showError(state.message)
    }

    private fun showContent() {
    }

    private fun showEmpty() {
    }

    private fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_LONG)
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {
        fun newInstance() = SendFriendRequestFragment()
    }
}