package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.CanceledFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class CanceledFriendRequestWsFriendshipDbMapper: Mapper<CanceledFriendRequestWsModel, FriendshipDbModel>() {

    override fun mapFrom(from: CanceledFriendRequestWsModel): FriendshipDbModel {
        return FriendshipDbModel(
            uuid = from.cancelerUuid,
            username = from.cancelerUsername,
            email = from.cancelerEmail,
            isFriendshipAccepted = false,
            isSentFromCurrentUser = false
        )
    }
}