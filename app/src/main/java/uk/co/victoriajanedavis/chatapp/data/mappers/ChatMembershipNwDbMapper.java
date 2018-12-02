package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class ChatMembershipNwDbMapper extends Mapper<ChatMembershipNwModel, ChatDbModel> {

    private UserNwFriendshipDbMapper friendshipMapper = new UserNwFriendshipDbMapper(true, null);
    private MessageNwDbMapper messageMapper = new MessageNwDbMapper();

    @Override
    public ChatDbModel mapFrom(@NonNull ChatMembershipNwModel from) {
        ChatDbModel chatDbModel = new ChatDbModel();
        chatDbModel.setUuid(from.getChat().getUuid());

        if (from.getChat().getMessages().size() > 0) {
            chatDbModel.setLastMessageText(from.getChat().getMessages().get(0).getText());
            chatDbModel.setLastMessageDate(from.getChat().getMessages().get(0).getCreated());
            chatDbModel.setLastMessageFromCurrentUser(from.getChat().getMessages().get(0).isFromCurrentUser());

            for (MessageNwModel messageNwModel : from.getChat().getMessages()) {
                MessageDbModel messageDbModel = messageMapper.mapFrom(messageNwModel);
                chatDbModel.getMessages().add(messageDbModel);
            }
        }
        else {
            chatDbModel.setLastMessageText("");
            chatDbModel.setLastMessageDate(null);
            chatDbModel.setLastMessageFromCurrentUser(null);
        }

        if(from.getOtherUser() != null) {
            FriendshipDbModel friendDbModel = friendshipMapper.mapFrom(from.getOtherUser());
            friendDbModel.setChatUuid(chatDbModel.getUuid());
            chatDbModel.setFriendship(friendDbModel);
        }

        return chatDbModel;
    }
}
