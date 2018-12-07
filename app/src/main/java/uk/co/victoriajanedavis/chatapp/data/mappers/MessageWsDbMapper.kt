package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.MessageWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class MessageWsDbMapper: Mapper<MessageWsModel, MessageDbModel>() {

    override fun mapFrom(from: MessageWsModel): MessageDbModel {
        return MessageDbModel(
            uuid = from.uuid,
            text = from.message,
            created = from.date,
            chatUuid = from.chatUuid,
            userUuid = from.senderUuid,
            userUsername = from.senderUsername,
            isFromCurrentUser = false
        )
    }
}