package uk.co.victoriajanedavis.chatapp.data.repositories;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore;
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;

@ApplicationScope
public class SentFriendRequestRepository {

    private final BaseReactiveStore<FriendshipDbModel> friendStore;
    private final ChatAppService chatService;

    private final FriendshipDbEntityMapper dbEntityMapper;
    private final UserNwFriendshipDbMapper nwSentDbMapper;

    @Inject
    public SentFriendRequestRepository(@NonNull @SentFriendRequestStore BaseReactiveStore<FriendshipDbModel> friendStore,
                                       @NonNull ChatAppService service) {
        this.friendStore = friendStore;
        this.chatService = service;

        dbEntityMapper = new FriendshipDbEntityMapper();
        nwSentDbMapper = new UserNwFriendshipDbMapper(false, true);
    }

    @NonNull
    public Observable<List<FriendshipEntity>> getAllSentFriendRequests() {
        return friendStore.getAll(null)
                .switchMapSingle(dbModels -> Observable.fromIterable(dbModels)
                        .map(dbEntityMapper::mapFrom)
                        .toList());
    }

    @NonNull
    public Completable fetchSentFriendRequests() {
        return chatService.getSentFriendRequests()
                .flatMap(nwModels -> Observable.fromIterable(nwModels)
                        .map(nwSentDbMapper::mapFrom)
                    .toList())
                .flatMapCompletable(friendRequests -> friendStore.replaceAll(null, friendRequests));
    }

    @NonNull
    public Single<FriendshipEntity> sendFriendRequest(String username) {
        return chatService.sendFriendRequest(username)
                .map(nwSentDbMapper::mapFrom)
                .flatMap(friendEntity -> friendStore.storeSingular(friendEntity)
                        .andThen(Single.just(friendEntity).map(dbEntityMapper::mapFrom)));
    }

    @NonNull
    public Single<FriendshipEntity> cancelSentFriendRequest(String username) {
        return chatService.cancelSentFriendRequest(username)
                .map(nwSentDbMapper::mapFrom)
                .flatMap(friendEntity -> friendStore.delete(friendEntity)
                        .andThen(Single.just(friendEntity).map(dbEntityMapper::mapFrom)));
    }


}
