package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity

class MessageDbEntityMapper : Mapper<MessageDbModel, MessageEntity>() {

    override fun mapFrom(from: MessageDbModel): MessageEntity {
        return MessageEntity(
            uuid = from.uuid,
            chatUuid = from.chatUuid,
            userUuid = from.userUuid,
            userUsername = from.userUsername,
            text = from.text,
            created = from.created,
            isFromCurrentUser = from.isFromCurrentUser
        )
    }
}
