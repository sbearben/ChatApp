package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class FriendshipCache implements DiskCache<UUID, FriendshipDbModel> {

    private final FriendshipDao dao;


    @Inject
    public FriendshipCache(ChatAppDatabase database) {
        this.dao = database.friendshipDao();
    }

    @Override
    public void putSingular(@NonNull FriendshipDbModel friendshipDbModel) {
        dao.insertFriendship(friendshipDbModel);
    }

    @Override
    public void putAll(@NonNull List<FriendshipDbModel> friendshipDbModels) {
        dao.insertFriendships(friendshipDbModels);
    }

    @Override
    public void replaceAll(@Nullable UUID uuid,
                           @NonNull List<FriendshipDbModel> friendshipDbModels) {
        dao.replaceAllAcceptedFriendships(friendshipDbModels);
    }

    @Override
    public void delete(@NonNull FriendshipDbModel friendshipDbModel) {
        dao.deleteFriendship(friendshipDbModel);
    }

    @Override
    public void clear() {
        dao.clear();
    }

    @NonNull
    @Override
    public Observable<FriendshipDbModel> getSingular(@NonNull UUID chat_uuid) {
        return dao.getFriendshipByChatUuid(chat_uuid).toObservable();
    }

    @NonNull
    @Override
    public Observable<List<FriendshipDbModel>> getAll(@Nullable UUID uuid) {
        return dao.getAllAcceptedFriendships().toObservable();
    }
}
