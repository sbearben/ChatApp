package uk.co.victoriajanedavis.chatapp.presentation.ui.sendfriendrequest

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_send_friend_request.*
import kotlinx.android.synthetic.main.layout_content_progress_dim.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import javax.inject.Inject

class SendFriendRequestFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SendFriendRequestViewModel

    private var sendMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendFriendRequestViewModel::class.java)

        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_send_friend_request_toolbar, menu)
        sendMenuItem = menu.findItem(R.id.menu_send)
        sendMenuItem?.isEnabled = usernameEditText.text?.isNotEmpty() ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_send -> {
                sendFriendRequest()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
            sendMenuItem?.isEnabled = s.isNotEmpty()
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
    }

    private fun sendFriendRequest() {
        //val temp : MenuItem? = sendMenuItem  // Need this because of weird Kotlin things in the following if statement
        //if(temp != null && temp.isEnabled) {
        if(sendMenuItem?.isEnabled ?: false) {
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

    private fun onStateChanged(state: State<String>) = when(state) {
        is ShowContent -> showContent(state.content)
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun showContent(username: String) {
        showSnackbar("Friend request send to: $username ", Snackbar.LENGTH_LONG)
        findNavController().popBackStack()
    }

    private fun showLoading() {
        sendMenuItem?.isEnabled = false
        progressBarLayout.visible()
    }

    private fun showError(message: String) {
        sendMenuItem?.isEnabled = true
        progressBarLayout.gone()
        showSnackbar(message, Snackbar.LENGTH_LONG)
    }
}