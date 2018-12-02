package uk.co.victoriajanedavis.chatapp.domain.interactors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor;

import static io.reactivex.Single.just;

public class GetReceivedFriendRequestsList implements RetrieveInteractor<Void, List<FriendshipEntity>> {

    @NonNull
    private final ReceivedFriendRequestRepository repository;

    @Inject
    public GetReceivedFriendRequestsList(@NonNull final ReceivedFriendRequestRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<List<FriendshipEntity>> getBehaviorStream(@NonNull final Void params) {
        return repository.getAllReceivedFriendRequests()
                .flatMapSingle(this::fetchWhenEmptyAndThenReceivedFriendRequests);
    }

    @NonNull
    private Single<List<FriendshipEntity>> fetchWhenEmptyAndThenReceivedFriendRequests(
            @NonNull final List<FriendshipEntity> friendEntities) {
        return fetchWhenEmpty(friendEntities).andThen(just(friendEntities));
    }

    @NonNull
    private Completable fetchWhenEmpty(@NonNull final List<FriendshipEntity> friendEntities) {
        return friendEntities.isEmpty()
               ? repository.fetchReceivedFriendRequests()
               : Completable.complete();
    }
}
