package uk.co.victoriajanedavis.chatapp.presentation.common

sealed class ListState<in T> {
    object ShowLoading : ListState<Any?>()
    data class ShowContent<T>(val content: T) : ListState<T>()
    data class ShowError(val message: String) : ListState<Any?>()
    object ShowEmpty : ListState<Any?>()
}