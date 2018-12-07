package uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_friends.*
import uk.co.victoriajanedavis.chatapp.presentation.common.viewslice.BaseViewSlice
import uk.co.victoriajanedavis.chatapp.presentation.ui.friends.friends.adapter.FriendsAdapter
import javax.inject.Inject

class FriendsListViewSlice @Inject constructor(
    private val actionLiveData: MutableLiveData<Int>,
    private val layoutManager: LinearLayoutManager,
    private val adapter: FriendsAdapter
): BaseViewSlice() {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
    }

    /*
    override fun getAction(): LiveData<Int> = actionLiveData

    override fun showItems(items: List<Meme>) {
        adapter.setMemes(memes)
    }
    */
}