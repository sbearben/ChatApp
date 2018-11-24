package uk.co.victoriajanedavis.chatapp.data.mappers;

import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;

public class FriendshipDbEntityMapper extends Mapper<FriendshipDbModel, FriendshipEntity> {

    @Override
    public FriendshipEntity mapFrom(FriendshipDbModel from) {
        FriendshipEntity entity = new FriendshipEntity();
        entity.setUuid(from.getUuid());
        entity.setUsername(from.getUsername());
        entity.setEmail(from.getEmail());
        entity.setFriendshipAccepted(from.isFriendshipAccepted());
        entity.setSentFromCurrentUser(from.isSentFromCurrentUser());
        entity.setChatUuid(from.getChatUuid());

        return entity;
    }
}
