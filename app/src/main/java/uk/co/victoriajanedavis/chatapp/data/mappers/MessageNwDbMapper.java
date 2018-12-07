package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class MessageNwDbMapper extends Mapper<MessageNwModel, MessageDbModel> {

    @Override
    public MessageDbModel mapFrom(@NonNull MessageNwModel from) {
        return new MessageDbModel(
                from.getUuid(),
                from.getText(),
                from.getCreated(),
                from.getChatUuid(),
                from.getUserUuid(),
                from.getUserUsername(),
                from.isFromCurrentUser()
        );

        /*
        dbModel.setUuid(from.getUuid());
        dbModel.setChatUuid(from.getChatUuid());
        dbModel.setText(from.getText());
        dbModel.setCreated(from.getCreated());
        dbModel.setUserUuid(from.getUserUuid());
        dbModel.setUserUsername(from.getUserUsername());
        dbModel.setFromCurrentUser(from.isFromCurrentUser());

        return dbModel;
        */
    }
}
