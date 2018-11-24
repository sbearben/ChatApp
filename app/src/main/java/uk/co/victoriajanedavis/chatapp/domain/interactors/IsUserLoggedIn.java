package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder;

public class IsUserLoggedIn implements ReactiveInteractor.RetrieveInteractor<Void, Boolean> {

    @NonNull private final TokenRepository repository;
    @NonNull private final TokenEntityHolder holder;


    @Inject
    public IsUserLoggedIn(@NonNull final TokenRepository repository,
                          @NonNull final TokenEntityHolder holder) {
        this.repository = repository;
        this.holder = holder;
    }

    @NonNull
    @Override
    public Observable<Boolean> getBehaviorStream(@Nullable Void aVoid) {
        return repository.getTokenStream()
                .doOnNext(holder::setTokenEntity) // TODO: I don't love this way of setting the TokenHolder
                .map(token -> !token.isEmpty());
    }
}
