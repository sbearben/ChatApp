package uk.co.victoriajanedavis.chatapp.presentation.notifications

interface Notification<T> {
    fun issueNotification(model: T)
}