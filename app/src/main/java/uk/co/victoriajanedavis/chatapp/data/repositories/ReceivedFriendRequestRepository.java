package uk.co.victoriajanedavis.chatapp.data.repositories;

import java.util.List;

import javax.inject.Inject;

import android.util.Log;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore;
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;

@ApplicationScope
public class ReceivedFriendRequestRepository {

    private final BaseReactiveStore<FriendshipDbModel> friendStore;
    private final ChatAppService chatService;

    private final FriendshipDbEntityMapper dbEntityMapper;
    private final UserNwFriendshipDbMapper nwReceivedDbMapper;
    private final UserNwFriendshipDbMapper nwAcceptedDbMapper;

    @Inject
    public ReceivedFriendRequestRepository(@NonNull @ReceivedFriendRequestStore BaseReactiveStore<FriendshipDbModel> friendStore,
                                           @NonNull ChatAppService service) {
        this.friendStore = friendStore;
        this.chatService = service;

        dbEntityMapper = new FriendshipDbEntityMapper();
        nwReceivedDbMapper = new UserNwFriendshipDbMapper(false, false);
        nwAcceptedDbMapper = new UserNwFriendshipDbMapper(true, null);
    }

    @NonNull
    public Observable<List<FriendshipEntity>> getAllReceivedFriendRequests() {
        return friendStore.getAll(null)
                .switchMapSingle(dbModels -> Observable.fromIterable(dbModels)
                    .map(dbEntityMapper::mapFrom)
                    .toList());
    }

    @NonNull
    public Completable fetchReceivedFriendRequests() {
        return chatService.getReceivedFriendRequests()
                .flatMap(nwModels -> Observable.fromIterable(nwModels)
                    .map(nwReceivedDbMapper::mapFrom)
                    .toList())
                .flatMapCompletable(friendRequests -> friendStore.replaceAll(null, friendRequests));
    }

    @NonNull
    public Single<FriendshipEntity> acceptReceivedFriendRequest(String username) {
        return chatService.acceptFriendRequest(username)
                .map(nwAcceptedDbMapper::mapFrom)
                .flatMap(friendDbModel -> friendStore.storeSingular(friendDbModel)
                        .andThen(Single.just(friendDbModel).map(dbEntityMapper::mapFrom)));
    }

    @NonNull
    public Single<FriendshipEntity> rejectReceivedFriendRequest(String username) {
        return chatService.rejectFriendRequest(username)
                .map(nwReceivedDbMapper::mapFrom)
                .flatMap(friendDbModel -> friendStore.delete(friendDbModel)
                        .andThen(Single.just(friendDbModel).map(dbEntityMapper::mapFrom)));
    }


}
