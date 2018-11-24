package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class MessageNwDbMapper extends Mapper<MessageNwModel, MessageDbModel> {

    @Override
    public MessageDbModel mapFrom(@NonNull MessageNwModel from) {
        MessageDbModel dbModel = new MessageDbModel();

        dbModel.setUuid(from.getUuid());
        dbModel.setUserUuid(from.getUser().getUuid());
        dbModel.setText(from.getText());
        dbModel.setCreated(from.getCreated());
        dbModel.setChatUuid(from.getChatUuid());
        return dbModel;
    }
}
