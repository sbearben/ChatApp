package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;

public class MessageDbEntityMapper extends Mapper<MessageDbModel, MessageEntity> {

    @Override
    public MessageEntity mapFrom(MessageDbModel from) {
        MessageEntity entity = new MessageEntity();

        entity.setUuid(from.getUuid());
        entity.setChatUuid(from.getChatUuid());
        entity.setUserUuid(from.getUserUuid());
        entity.setUserUsername(from.getUserUsername());
        entity.setText(from.getText());
        entity.setCreated(from.getCreated());
        entity.setFromCurrentUser(from.isFromCurrentUser());

        return entity;
    }
}
