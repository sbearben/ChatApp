package uk.co.victoriajanedavis.chatapp.presentation.ui.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.EndlessRecyclerViewOnScrollListener
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.adapter.MessagesAdapter
import java.util.UUID
import javax.inject.Inject

class ChatFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var adapter: MessagesAdapter
    lateinit var viewModel: ChatViewModel

    lateinit var chatUuid: UUID
    lateinit var username: String
    lateinit var transitionName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel::class.java)

        chatUuid = UUID.fromString(arguments?.getString(ARG_CHAT_UUID)!!)
        username = arguments?.getString(ARG_USERNAME) ?: ""
        transitionName = arguments?.getString(ARG_TRANSITION_NAME) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupMessageSendViews()
        setupViewModelStateObserver()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbarTitle.text = username

        // Set transitionName on the toolbar and start the enter transition
        toolbarTitle.transitionName = transitionName
        startPostponedEnterTransition()

        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            //setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""  // This is required as for some reason the app name was being appended next to the username
        }
    }

    private fun setupRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            true
        )
        recyclerview.adapter = adapter
        recyclerview.addOnScrollListener(EndlessRecyclerViewOnScrollListener(
            recyclerview.layoutManager as LinearLayoutManager) {
            Log.d("ChatFragment", "scrollListener triggered")
            if (!(adapter.isEmpty()) && !adapter.isLoadingViewAdded()) {
                viewModel.loadMore(chatUuid)
            }
        })
    }

    private fun setupMessageSendViews() {
        message_editText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }

        send_button.setOnClickListener { _ -> sendMessage() }
    }

    private fun setupViewModelStateObserver() {
        viewModel.fetchMessagesForChat(chatUuid)
        viewModel.getMessageLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: PaginatedState<List<MessageEntity>>) = when(state) {
        is ShowContent -> {
            adapter.submitList(state.content) {
                recyclerview.scrollToPosition(0)
            }
        }
        is ShowLoading -> {}
        is ShowLoadingMore -> adapter.addLoadingView()
        is LoadingMoreComplete -> adapter.removeLoadingView()
        is ShowEmpty -> {}
        is ShowError -> showError(state.message)
    }

    private fun sendMessage() {
        if (!message_editText.isEmpty()) {
            viewModel.sendMessage(chatUuid, message_editText.text.toString())
            message_editText.text.clear()
            hideKeyboard()
        }
    }

    private fun showError(message: String) {
        showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    companion object {
        const val ARG_CHAT_UUID = "chat_uuid"
        const val ARG_USERNAME = "username"
        const val ARG_TRANSITION_NAME = "transition_name"

        fun createBundle(
            chatUuid: UUID,
            username: String,
            transitionName: String
        ): Bundle {
            return Bundle().apply {
                putString(ARG_CHAT_UUID, chatUuid.toString())
                putString(ARG_USERNAME, username)
                putString(ARG_TRANSITION_NAME, transitionName)
            }
        }
    }
}