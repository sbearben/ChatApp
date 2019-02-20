package uk.co.victoriajanedavis.chatapp.domain.entities

import java.util.UUID

data class ChatEntity (
    val uuid: UUID,
    val lastMessage: MessageEntity,
    val friendship: FriendshipEntity
    //@Nullable private List<MessageEntity> messages;
) : Comparable<ChatEntity> {

    override fun compareTo(other: ChatEntity): Int {
        // Notice we swapped how compareTo is called so that when sorted ChatEntities will always be descending
        return other.lastMessage.created.compareTo(this.lastMessage.created)

    }
}
