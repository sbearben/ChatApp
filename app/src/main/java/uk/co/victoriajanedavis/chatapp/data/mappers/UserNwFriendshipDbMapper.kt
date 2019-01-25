package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class UserNwFriendshipDbMapper(
    private val friendshipAccepted: Boolean,
    private val sentFromCurrentUser: Boolean?
) : Mapper<UserNwModel, FriendshipDbModel>() {

    override fun mapFrom(from: UserNwModel): FriendshipDbModel {
        return FriendshipDbModel(
            uuid = from.uuid,
            username = from.username,
            email = from.email,
            isFriendshipAccepted = friendshipAccepted,
            isSentFromCurrentUser = sentFromCurrentUser
        )
    }
}
