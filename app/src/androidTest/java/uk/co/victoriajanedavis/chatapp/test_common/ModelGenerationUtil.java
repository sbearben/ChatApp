package uk.co.victoriajanedavis.chatapp.test_common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;

public class ModelGenerationUtil {

    public static List<ChatMembershipNwModel> createChatMembershipNwList(int number) {
        List<ChatMembershipNwModel> chatNwModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            chatNwModels.add(new ChatMembershipNwModel(UUID.randomUUID(), createUserNwModel(null)));
        }

        return chatNwModels;
    }

    public static UserNwModel createUserNwModel(@Nullable String username) {
        UserNwModel model = new UserNwModel(UUID.randomUUID());
        model.setUsername(username);
        return model;
    }

    public static List<UserNwModel> createUserNwList(int number) {
        List<UserNwModel> userNwModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            userNwModels.add(createUserNwModel(null));
        }

        return userNwModels;
    }

    public static List<ChatMembershipDbModel> createChatMembershipDbModelList(int number) {
        List<ChatMembershipDbModel> chatDbModelList = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            chatDbModelList.add(createChatMembershipDbModelWithFriendshipDbModel());
        }

        return chatDbModelList;
    }

    public static ChatMembershipDbModel createChatMembershipDbModelWithFriendshipDbModel() {
        ChatMembershipDbModel dbModel = createChatMembershipDbModel();

        dbModel.setFriendship(createFriendshipDbModel(dbModel));
        return dbModel;
    }

    public static ChatMembershipDbModel createChatMembershipDbModel() {
        ChatMembershipDbModel dbModel = new ChatMembershipDbModel();
        dbModel.setUuid(UUID.randomUUID());

        return dbModel;
    }

    public static FriendshipDbModel createFriendshipDbModel(ChatMembershipDbModel chatDbModel) {
        FriendshipDbModel friendshipDbModel = new FriendshipDbModel();
        friendshipDbModel.setUuid(UUID.randomUUID());

        friendshipDbModel.setChatUuid(chatDbModel.getUuid());
        return friendshipDbModel;
    }

    public static List<MessageNwModel> createMessageNwList(UUID chatUuid, int number) {
        List<MessageNwModel> messageNwModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            messageNwModels.add(createMessageNwModel(chatUuid));
        }

        return messageNwModels;
    }

    public static MessageNwModel createMessageNwModel(@Nullable UUID chatUuid) {
        MessageNwModel model = new MessageNwModel(UUID.randomUUID());
        model.setChatUuid(chatUuid);
        model.setUser(createUserNwModel(null));
        return model;
    }

    public static TokenNwModel createTokenNwModel() {
        TokenNwModel model = new TokenNwModel();

        model.setToken(UUID.randomUUID().toString());
        model.setUser(createUserNwModel(null));
        return model;
    }
}
