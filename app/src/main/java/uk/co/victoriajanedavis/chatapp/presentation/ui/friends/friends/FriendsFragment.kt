package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.ext.observe
import javax.inject.Inject

class FriendsFragment : DaggerFragment() {

    @Inject lateinit var viewModel: FriendsViewModel
    @Inject lateinit var testLiveData: MutableLiveData<Int>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelStateObserver()
        Log.d("FriendsFragment", "liveData: ${testLiveData.toString()}")

    }

    private fun setupViewModelStateObserver() {
        viewModel.getChatMembershipLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun onStateChanged(state: State<List<ChatMembershipEntity>>) = when(state) {
        is ShowContent -> {}
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