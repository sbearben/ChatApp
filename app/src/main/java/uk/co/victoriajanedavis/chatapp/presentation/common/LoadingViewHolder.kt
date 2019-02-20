package uk.co.victoriajanedavis.chatapp.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.R

class LoadingViewHolder<T>(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup
) : BaseViewHolder<T>(layoutInflater.inflate(R.layout.item_progress_bar, parent, false)) {


    override fun bind(item: T) {
    }
}