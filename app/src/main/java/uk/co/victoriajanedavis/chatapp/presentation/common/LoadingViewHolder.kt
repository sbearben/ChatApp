package uk.co.victoriajanedavis.chatapp.presentation.common

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.BaseViewHolder

class LoadingViewHolder<T>(
    private val layoutInflater: LayoutInflater,
    private val parent: ViewGroup
) : BaseViewHolder<T>(layoutInflater.inflate(R.layout.item_progress_bar, parent, false)) {


    override fun bind(item: T) {
    }
}