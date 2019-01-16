package uk.co.victoriajanedavis.chatapp.data.model.websocket

enum class RealtimeMessageTypes(val type: String) {
    ACCEPTED_FRIEND_REQUEST("accepted_friend_request"),
    CANCELED_FRIEND_REQUEST("canceled_friend_request"),
    CREATED_FRIEND_REQUEST("created_friend_request"),
    REJECTED_FRIEND_REQUEST("rejected_friend_request"),
    CHAT_MESSAGE("chat_message")
}