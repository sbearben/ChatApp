package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity

class ChatMembershipDbEntityMapper : Mapper<ChatDbModel, ChatEntity>() {

    override fun mapFrom(from: ChatDbModel): ChatEntity {
        return ChatEntity(
            uuid = from.uuid,
            lastMessageText = from.lastMessageText,
            lastMessageDate = from.lastMessageDate,
            isLastMessageFromCurrentUser = from.isLastMessageFromCurrentUser
        )
    }
}
