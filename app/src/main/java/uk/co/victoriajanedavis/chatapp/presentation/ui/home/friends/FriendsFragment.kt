package uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.layout_message.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseFragment
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState
import uk.co.victoriajanedavis.chatapp.presentation.common.ListState.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.gone
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.observe
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.makeSnackbar
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.visible
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendItemAction
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendItemAction.*
import uk.co.victoriajanedavis.chatapp.presentation.ui.home.friends.adapter.FriendsAdapter
import uk.co.victoriajanedavis.chatapp.presentation.ui.sendmessage.SendMessageFragment
import javax.inject.Inject

@SuppressLint("ValidFragment")
class FriendsFragment constructor(
    private val viewModelFactory: ViewModelFactory,
    private val actionLiveData: MutableLiveData<FriendItemAction>,
    private val adapter: FriendsAdapter
) : BaseFragment() {  //DaggerFragment() {

    //@Inject lateinit var viewModelFactory: ViewModelFactory
    //@Inject lateinit var friendActionLiveData: MutableLiveData<FriendItemAction>
    //@Inject lateinit var adapter: FriendsAdapter
    private lateinit var viewModel: FriendsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsViewModel::class.java)
        setupFriendItemActionObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
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
        viewModel.getFriendsLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: ListState<List<FriendshipEntity>>) = when(state) {
        is ShowContent -> showContent(state.content)
        is ShowLoading -> {}
        is StopLoading -> {}
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
        message_imageview.setImageResource(R.drawable.ic_chat_black_72dp)
        message_textview.setText(R.string.error_no_items_to_display)
    }

    private fun showError(message: String) {
        makeSnackbar(message, Snackbar.LENGTH_LONG)
            ?.setAction("Retry") { _ -> viewModel.retry() }
    }

    private fun setupFriendItemActionObserver() {
        actionLiveData.observe(this) {
            it?.let(::onFriendItemActionReceived)
        }
    }

    private fun onFriendItemActionReceived(action: FriendItemAction) = when(action) {
        is Clicked -> {
            val bundle = SendMessageFragment.createBundle(
                chatUuid = action.friendEntity.chatUuid!!,
                username = action.friendEntity.username
            )
            findNavController().navigate(R.id.action_homeFragment_to_sendMessageFragment, bundle)
        }
    }

    companion object {
        const val TAG = "FriendsFragment"
        //fun newInstance() = FriendsFragment()
    }
}