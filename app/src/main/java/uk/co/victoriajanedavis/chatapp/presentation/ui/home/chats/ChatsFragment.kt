package uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.layout_message.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseFragment
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.gone
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.observe
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.makeSnackbar
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.visible
import uk.co.victoriajanedavis.chatapp.presentation.ui.chat.ChatFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter.ChatViewHolder
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.chats.adapter.ChatsAdapter
import javax.inject.Inject

@SuppressLint("ValidFragment")
class ChatsFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseFragment(), ChatViewHolder.OnClickListener {

    private val adapter = ChatsAdapter(this)
    private lateinit var viewModel: ChatsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViewModelStateObserver()
    }

    override fun onChatClicked(chatEntity: ChatEntity, sharedTextView: TextView) {
        val bundle = ChatFragment.createBundle(
            chatUuid = chatEntity.uuid,
            username = chatEntity.friendship.username,
            transitionName = sharedTextView.transitionName
        )

        val extras = FragmentNavigatorExtras(
            sharedTextView to ViewCompat.getTransitionName(sharedTextView)!!
        )
        findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle, null, extras)
    }

    private fun setupRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
    }

    private fun setupViewModelStateObserver() {
        viewModel.getChatLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: ListState<List<ChatEntity>>) = when(state) {
        is ShowContent -> showContent(state.content)
        is ShowLoading -> {}
        is StopLoading -> {}
        is ShowError -> showError(state.message)
        is ShowEmpty -> showEmpty()
    }

    private fun showContent(content: List<ChatEntity>) {
        adapter.submitList(content)
        message_layout.gone()
    }

    private fun showEmpty() {
        adapter.submitList(ArrayList())
        message_layout.visible()
        message_button.gone()
        message_imageview.setImageResource(R.drawable.ic_chat_black_72dp)
        message_textview.setText(R.string.error_no_items_to_display)
    }

    private fun showError(message: String) {
        makeSnackbar(message, Snackbar.LENGTH_LONG)
            ?.setAction("Retry") { _ -> viewModel.retry() }
    }
}