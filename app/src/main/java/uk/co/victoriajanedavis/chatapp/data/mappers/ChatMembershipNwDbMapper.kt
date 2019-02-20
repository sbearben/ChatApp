package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class ChatMembershipNwDbMapper : Mapper<ChatMembershipNwModel, ChatDbModel>() {

    private val friendshipMapper = UserNwFriendshipDbMapper(true, null)
    private val messageMapper = MessageNwDbMapper()

    override fun mapFrom(from: ChatMembershipNwModel): ChatDbModel {
        val messagesList = from.chat.messages.map { messageMapper.mapFrom(it) }

        val friendDbModel = friendshipMapper.mapFrom(from.otherUser)
        friendDbModel.chatUuid = from.chat.uuid

        return ChatDbModel(
            friendship = friendDbModel,
            messages = messagesList
        )
    }
}