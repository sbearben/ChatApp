package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_friends.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.observe
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter.FriendsAdapter
import javax.inject.Inject

class FriendsFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var adapter: FriendsAdapter
    lateinit var viewModel: FriendsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsViewModel::class.java)
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
        viewModel.getChatLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: State<List<ChatEntity>>) = when(state) {
        is ShowContent -> {
            adapter.submitList(state.content)
        }
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun showLoading() {
    }

    private fun showError(message: String) {
        Log.e("LoginFragment", message)
    }

    companion object {
        const val TAG = "FriendsFragment"
        fun newInstance() = FriendsFragment()
    }
}