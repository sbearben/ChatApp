package uk.co.victoriajanedavis.chatapp.data.mappers;

import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatMembershipDbEntityMapper extends Mapper<ChatDbModel, ChatEntity> {

    private FriendshipDbEntityMapper friendshipMapper = new FriendshipDbEntityMapper();
    private MessageDbEntityMapper messageMapper = new MessageDbEntityMapper();


    @Override
    public ChatEntity mapFrom(ChatDbModel from) {
        ChatEntity entity = new ChatEntity();

        entity.setUuid(from.getUuid());
        entity.setLastMessageText(from.getLastMessageText());
        entity.setLastMessageDate(from.getLastMessageDate());
        entity.setLastMessageFromCurrentUser(from.isLastMessageFromCurrentUser());

        /*
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
        */

        return entity;
    }
}
