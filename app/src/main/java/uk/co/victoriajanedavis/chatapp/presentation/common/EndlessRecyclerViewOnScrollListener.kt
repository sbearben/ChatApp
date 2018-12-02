package uk.co.victoriajanedavis.chatapp.presentation.common

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class EndlessRecyclerViewOnScrollListener constructor(
    private val layoutManager: LinearLayoutManager,
    private val onLoadMore: (Int) -> Unit
    ) : RecyclerView.OnScrollListener()
{
    /**
     * Low threshold to show the onLoad()/Spinner functionality. If using for a
     * production app set a higher value for better UX
     */
    private val visibleThreshold = 2

    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (totalItemCount < previousTotalItemCount) { // List was cleared
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        /**
         * If it’s still loading, we check to see if the DataSet count has
         * changed, if so we conclude it has finished loading and update the current page
         * number and total item count (+ 1 due to loading view being added).
         */
        if (loading && totalItemCount > previousTotalItemCount + 1) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        /**
         * If it isn’t currently loading, we check to see if we have breached
         * + the visibleThreshold and need to reload more data.
         */
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            onLoadMore.invoke(totalItemCount)
            loading = true
        }
    }
}