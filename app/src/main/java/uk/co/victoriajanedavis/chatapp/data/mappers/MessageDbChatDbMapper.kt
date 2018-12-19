package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.MessageWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class MessageDbChatDbMapper : Mapper<MessageDbModel, ChatDbModel>() {

    override fun mapFrom(from: MessageDbModel): ChatDbModel {
        return ChatDbModel(
            uuid = from.chatUuid,
            lastMessageText = from.text,
            lastMessageDate = from.created,
            isLastMessageFromCurrentUser = false
        )
    }
}