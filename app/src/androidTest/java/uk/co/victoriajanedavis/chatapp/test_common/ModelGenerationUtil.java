package uk.co.victoriajanedavis.chatapp.test_common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.*;

public class ModelGenerationUtil {

    public static List<ChatMembershipNwModel> createChatMembershipNwList(int number) {
        List<ChatMembershipNwModel> chatNwModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            chatNwModels.add(new ChatMembershipNwModel(createChatNwModel(), createUserNwModel(null)));
        }

        return chatNwModels;
    }

    public static ChatNwModel createChatNwModel() {
        return new ChatNwModel(UUID.randomUUID());
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

    public static List<ChatDbModel> createChatMembershipDbModelList(int number) {
        List<ChatDbModel> chatDbModelList = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            chatDbModelList.add(createChatMembershipDbModelWithFriendshipDbModel());
        }

        return chatDbModelList;
    }

    public static ChatDbModel createChatMembershipDbModelWithFriendshipDbModel() {
        ChatDbModel dbModel = createChatMembershipDbModel();

        dbModel.setFriendship(createFriendshipDbModel(dbModel));
        return dbModel;
    }

    public static ChatDbModel createChatMembershipDbModel() {
        ChatDbModel dbModel = new ChatDbModel();
        dbModel.setUuid(UUID.randomUUID());

        return dbModel;
    }

    public static FriendshipDbModel createFriendshipDbModel(ChatDbModel chatDbModel) {
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
        //model.setUser(createUserNwModel(null));
        return model;
    }

    public static TokenNwModel createTokenNwModel() {
        TokenNwModel model = new TokenNwModel();

        model.setToken(UUID.randomUUID().toString());
        model.setUser(createUserNwModel(null));
        return model;
    }
}
