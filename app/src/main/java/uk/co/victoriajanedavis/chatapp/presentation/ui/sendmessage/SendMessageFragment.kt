package uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_send_message.*
import kotlinx.android.synthetic.main.layout_content_progress_dim.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import java.util.UUID
import javax.inject.Inject

class SendMessageFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SendMessageViewModel

    lateinit var chatUuid: UUID
    lateinit var username: String
    private var sendMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendMessageViewModel::class.java)

        chatUuid = UUID.fromString(arguments?.getString(ARG_CHAT_UUID)!!)
        username = arguments?.getString(ARG_USERNAME) ?: ""

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_message, container, false)
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
        sendMenuItem?.isEnabled = messageEditText.text?.isNotEmpty() ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_send -> {
                sendMessage()
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
            title = "Message: \'$username\'"
        }
    }

    private fun setupViewListeners() {
        messageInputLayout.afterTextChanged { s ->
            sendMenuItem?.isEnabled = s.isNotEmpty()
            messageInputLayout.error =
                    if(s.isEmpty()) "Message may not be empty."
                    else null
        }
        messageEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }
    }

    private fun sendMessage() {
        if(sendMenuItem?.isEnabled ?: false) {
            hideKeyboard()
            viewModel.sendMessage(
                chatUuid,
                messageEditText.text.toString()
            )
        }
    }

    private fun setupViewModelStateObserver() {
        viewModel.getSendMessageLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: State<MessageEntity>) = when(state) {
        is ShowContent -> showContent()
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun showContent() {
        showSnackbar("Message sent to: $username", Snackbar.LENGTH_LONG)
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

    companion object {
        const val ARG_CHAT_UUID = "chat_uuid"
        const val ARG_USERNAME = "username"

        fun createBundle(chatUuid: UUID, username: String): Bundle {
            return Bundle().apply {
                putString(ARG_CHAT_UUID, chatUuid.toString())
                putString(ARG_USERNAME, username)
            }
        }
    }
}