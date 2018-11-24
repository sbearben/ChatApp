package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class ChatMembershipNwDbMapper extends Mapper<ChatMembershipNwModel, ChatMembershipDbModel> {

    private UserNwFriendshipDbMapper friendshipMapper = new UserNwFriendshipDbMapper(true, null);

    @Override
    public ChatMembershipDbModel mapFrom(@NonNull ChatMembershipNwModel from) {
        ChatMembershipDbModel chatDbModel = new ChatMembershipDbModel();
        chatDbModel.setUuid(from.getChatUuid());

        if(from.getOtherUser() != null) {
            FriendshipDbModel friendDbModel = friendshipMapper.mapFrom(from.getOtherUser());
            friendDbModel.setChatUuid(chatDbModel.getUuid());
            chatDbModel.setFriendship(friendDbModel);
        }

        return chatDbModel;
    }
}
