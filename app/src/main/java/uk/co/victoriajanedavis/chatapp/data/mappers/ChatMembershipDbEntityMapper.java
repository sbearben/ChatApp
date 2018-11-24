package uk.co.victoriajanedavis.chatapp.data.mappers;

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatMembershipDbEntityMapper extends Mapper<ChatMembershipDbModel, ChatMembershipEntity> {

    private FriendshipDbEntityMapper friendshipMapper = new FriendshipDbEntityMapper();
    private MessageDbEntityMapper messageMapper = new MessageDbEntityMapper();


    @Override
    public ChatMembershipEntity mapFrom(ChatMembershipDbModel from) {
        ChatMembershipEntity entity = new ChatMembershipEntity();

        entity.setUuid(from.getUuid());

        if (from.getFriendship() != null) {
            entity.setFriendship(friendshipMapper.mapFrom(from.getFriendship()));
        }

        if (from.getMessages() != null) {
            List<MessageEntity> messageModels = new ArrayList<>();
            for (MessageDbModel dbModel : from.getMessages()) {
                messageModels.add(messageMapper.mapFrom(dbModel));
            }
            entity.setMessages(messageModels);
        }

        return entity;
    }
}
