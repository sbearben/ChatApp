package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class MessageNwDbMapper : Mapper<MessageNwModel, MessageDbModel>() {

    override fun mapFrom(from: MessageNwModel): MessageDbModel {
        return MessageDbModel(
            uuid = from.uuid,
            text = from.text,
            created = from.created,
            chatUuid = from.chatUuid,
            userUuid = from.userUuid,
            userUsername = from.userUsername,
            isFromCurrentUser = from.isFromCurrentUser
        )
    }
}
