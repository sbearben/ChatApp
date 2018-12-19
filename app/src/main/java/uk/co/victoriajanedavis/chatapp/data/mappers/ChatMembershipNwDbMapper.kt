package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class ChatMembershipNwDbMapper : Mapper<ChatMembershipNwModel, ChatDbModel>() {

    private val friendshipMapper = UserNwFriendshipDbMapper(true, null)
    private val messageMapper = MessageNwDbMapper()

    override fun mapFrom(from: ChatMembershipNwModel): ChatDbModel {
        val chatDbModel = ChatDbModel(uuid=from.chat.uuid)

        if (from.chat.messages.size > 0) {
            chatDbModel.lastMessageText = from.chat.messages[0].text
            chatDbModel.lastMessageDate = from.chat.messages[0].created
            chatDbModel.isLastMessageFromCurrentUser = from.chat.messages[0].isFromCurrentUser

            for (messageNwModel in from.chat.messages) {
                val messageDbModel = messageMapper.mapFrom(messageNwModel)
                chatDbModel.messages!!.add(messageDbModel)
            }
        }

        if (from.otherUser != null) {
            val friendDbModel = friendshipMapper.mapFrom(from.otherUser)
            friendDbModel.chatUuid = chatDbModel.uuid
            chatDbModel.friendship = friendDbModel
        }

        return chatDbModel
    }
}
