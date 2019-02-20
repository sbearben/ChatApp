package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.AcceptedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.CreatedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class AcceptedFriendRequestWsFriendshipDbMapper: Mapper<AcceptedFriendRequestWsModel, FriendshipDbModel>() {

    override fun mapFrom(from: AcceptedFriendRequestWsModel): FriendshipDbModel {
        return FriendshipDbModel(
            uuid = from.acceptorUuid,
            username = from.acceptorUsername,
            email = from.acceptorEmail,
            chatUuid = from.chatUuid,
            isFriendshipAccepted = true,
            isSentFromCurrentUser = null
        )
    }
}