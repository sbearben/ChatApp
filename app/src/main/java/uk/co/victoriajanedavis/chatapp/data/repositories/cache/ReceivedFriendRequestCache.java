package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class ReceivedFriendRequestCache implements DiskCache<UUID, FriendshipDbModel> {

    private static long CACHE_MAX_AGE = 5 * 60 * 1000; // 5 minutes

    private final FriendshipDao dao;


    @Inject
    public ReceivedFriendRequestCache(ChatAppDatabase database) {
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
        Log.d("ReceivedRepo", "cache: replaceAll called: " + friendshipDbModels.size());
        dao.replaceAllReceivedFriendRequests(friendshipDbModels);
    }

    @Override
    public void delete(@NonNull FriendshipDbModel friendshipDbModel) {
        dao.deleteFriendship(friendshipDbModel);
    }


    @Override
    public void clear() {
    }

    @NonNull
    @Override
    public Observable<FriendshipDbModel> getSingular(@Nullable UUID uuid) {
        return dao.get(uuid)
                .filter(this::notExpired)
                .toObservable();
    }

    @NonNull
    @Override
    public Observable<List<FriendshipDbModel>> getAll(@Nullable UUID uuid) {
        return dao.getReceivedFriendRequests()
                //.flatMap(Flowable::fromIterable)
                //.filter(this::notExpired)
                //.toList()
                .toObservable();
        //.defaultIfEmpty(new ArrayList<>());
    }

    private boolean notExpired(FriendshipDbModel friendModel) {
        return (friendModel.getTimestamp() + CACHE_MAX_AGE) > TimestampProvider.Companion.currentTimeMillis();
    }
}
