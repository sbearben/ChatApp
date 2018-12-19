package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.CreatedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class CreatedFriendRequestWsFriendshipDbMapper: Mapper<CreatedFriendRequestWsModel, FriendshipDbModel>() {

    override fun mapFrom(from: CreatedFriendRequestWsModel): FriendshipDbModel {
        return FriendshipDbModel(
            uuid = from.senderUuid,
            username = from.senderUsername,
            email = from.senderEmail,
            isFriendshipAccepted = false,
            isSentFromCurrentUser = false
        )
    }
}