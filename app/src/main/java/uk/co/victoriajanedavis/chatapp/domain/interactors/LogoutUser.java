package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.DeleteInteractor;

public class LogoutUser implements DeleteInteractor<Void, TokenEntity> {

    @NonNull
    private final TokenRepository repository;


    @Inject
    public LogoutUser(@NonNull final TokenRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Single<TokenEntity> getSingle(@NonNull Void aVoid) {
        return repository.requestTokenSingle()
                .flatMap(tokenEntity -> repository.deleteTokenViaLogout()
                        .andThen(Single.just(tokenEntity)));
    }
}
