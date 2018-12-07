package uk.co.victoriajanedavis.chatapp.presentation.common

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    protected val context: Context = itemView.context
    protected val resources: Resources = itemView.resources

    override val containerView: View = itemView

    abstract fun bind(item: T)
}