package uk.co.victoriajanedavis.chatapp.presentation.common

sealed class PaginatedState<in T> {
    object ShowLoading : PaginatedState<Any?>()
    object ShowLoadingMore : PaginatedState<Any?>()
    object LoadingMoreComplete : PaginatedState<Any?>()
    object ShowEmpty : PaginatedState<Any?>()
    data class ShowContent<T>(val content: T) : PaginatedState<T>()
    data class ShowError(val message: String) : PaginatedState<Any?>()
}