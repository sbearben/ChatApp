package uk.co.victoriajanedavis.chatapp.presentation.common

sealed class State<in T> {
    object ShowLoading : State<Any?>()
    data class ShowContent<T>(val content: T) : State<T>()
    data class ShowError<T>(val message: String) : State<T>()
}