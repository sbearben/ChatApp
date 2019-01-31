package uk.co.victoriajanedavis.chatapp.presentation.ui.friendrequests.sent.send

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_send_friend_request.*
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
        setupViewListeners()
        setupViewModelStateObserver()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayShowTitleEnabled(true)
            title = "Send Friend Request"
        }
    }

    private fun setupViewListeners() {
        usernameInputLayout.afterTextChanged { s ->
            sendTextView.isEnabled = s.isNotEmpty()
            usernameInputLayout.error =
                    if(s.isEmpty() || s.matches(Regex("^[\\w.@+-]+\$"))) null
                    else "Username may only contain letters, numbers, and @/./+/-/_ characters."
        }
        usernameEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendFriendRequest()
                    true
                }
                else -> false
            }
        }
        sendTextView.setOnClickListener { sendFriendRequest() }
    }

    private fun sendFriendRequest() {
        if(sendTextView.isEnabled) {
            hideKeyboard()
            viewModel.sendFriendRequest(
                usernameEditText.text.toString(),
                messageEditText.text.toString()
            )
        }
    }

    private fun setupViewModelStateObserver() {
        viewModel.getRequestLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: State<Unit>) = when(state) {
        is ShowContent -> showContent()
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun showContent() {
        sendTextView.enable()
        progressBarLayout.gone()
    }

    private fun showLoading() {
        sendTextView.disable()
        progressBarLayout.visible()
    }

    private fun showError(message: String) {
        sendTextView.enable()
        progressBarLayout.gone()
        showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    companion object {
        fun newInstance() = SendFriendRequestFragment()
    }
}