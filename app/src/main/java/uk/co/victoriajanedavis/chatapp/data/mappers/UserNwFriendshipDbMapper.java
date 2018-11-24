package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class UserNwFriendshipDbMapper extends Mapper<UserNwModel, FriendshipDbModel> {

    private final boolean friendshipAccepted;

    @Nullable
    private final Boolean sentFromCurrentUser;


    public UserNwFriendshipDbMapper(boolean friendshipAccepted,
                                    @Nullable Boolean sentFromCurrentUser) {
        this.friendshipAccepted = friendshipAccepted;
        this.sentFromCurrentUser = sentFromCurrentUser;
    }

    @Override
    public FriendshipDbModel mapFrom(@NonNull UserNwModel from) {
        FriendshipDbModel dbModel = new FriendshipDbModel();
        dbModel.setUuid(from.getUuid());
        dbModel.setUsername(from.getUsername());
        dbModel.setEmail(from.getEmail());
        dbModel.setFriendshipAccepted(friendshipAccepted);
        dbModel.setSentFromCurrentUser(sentFromCurrentUser);
        
        return dbModel;
    }
}
