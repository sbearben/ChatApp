package uk.co.victoriajanedavis.chatapp.presentation.ui.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.EndlessRecyclerViewOnScrollListener
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState
import uk.co.victoriajanedavis.chatapp.presentation.common.PaginatedState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.adapter.ChatAdapter
import java.util.*
import javax.inject.Inject

class ChatFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var adapter: ChatAdapter
    lateinit var viewModel: ChatViewModel

    lateinit var chatUuid: UUID
    lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel::class.java)

        chatUuid = UUID.fromString(arguments?.getString(ARG_CHAT_UUID))
        username = arguments?.getString(ARG_USERNAME) ?: ""
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
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = username
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

    private fun sendMessage() {
        if (!message_editText.isEmpty()) {
            viewModel.postMessage(chatUuid, message_editText.text.toString())
            message_editText.text.clear()
            activity?.hideKeyboard()
        }
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
        is ShowLoading -> showLoading()
        is ShowLoadingMore -> adapter.addLoadingView()
        is LoadingMoreComplete -> adapter.removeLoadingView()
        is ShowError -> showError(state.message)
    }

    private fun showLoading() {
    }

    private fun showError(message: String) {
        Log.e("LoginFragment", message)
    }

    companion object {
        const val ARG_CHAT_UUID = "chat_uuid"
        const val ARG_USERNAME = "username"

        fun createBundle(uuidStr: String, username: String): Bundle {
            val bundle = Bundle()
            bundle.putString(ARG_CHAT_UUID, uuidStr)
            bundle.putString(ARG_USERNAME, username)
            return bundle
        }
    }
}