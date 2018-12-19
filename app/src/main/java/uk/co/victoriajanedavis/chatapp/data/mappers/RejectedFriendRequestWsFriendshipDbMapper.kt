package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.RejectedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class RejectedFriendRequestWsFriendshipDbMapper: Mapper<RejectedFriendRequestWsModel, FriendshipDbModel>() {

    override fun mapFrom(from: RejectedFriendRequestWsModel): FriendshipDbModel {
        return FriendshipDbModel(
            uuid = from.rejectorUuid,
            username = from.rejectorUsername,
            email = from.rejectorEmail,
            isFriendshipAccepted = false,
            isSentFromCurrentUser = true
        )
    }
}