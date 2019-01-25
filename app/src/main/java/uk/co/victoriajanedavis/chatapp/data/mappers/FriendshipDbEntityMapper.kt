package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

class FriendshipDbEntityMapper : Mapper<FriendshipDbModel, FriendshipEntity>() {

    override fun mapFrom(from: FriendshipDbModel): FriendshipEntity {
        return FriendshipEntity(
            uuid = from.uuid,
            username = from.username,
            email = from.email,
            isFriendshipAccepted = from.isFriendshipAccepted,
            isSentFromCurrentUser = from.isSentFromCurrentUser,
            chatUuid = from.chatUuid,
            loadingState = from.loadingState
        )
    }
}