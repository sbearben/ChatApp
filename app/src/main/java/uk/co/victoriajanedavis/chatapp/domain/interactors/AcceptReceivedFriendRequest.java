package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor;

public class AcceptReceivedFriendRequest implements SendInteractor<String, FriendshipEntity> {

    @NonNull
    private final ReceivedFriendRequestRepository repository;


    @Inject
    public AcceptReceivedFriendRequest(@NonNull final ReceivedFriendRequestRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Single<FriendshipEntity> getSingle(@NonNull String username) {
        return repository.acceptReceivedFriendRequest(username);
    }
}
