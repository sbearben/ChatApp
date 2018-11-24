package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor;

public class LoginUser implements SendInteractor<LoginUser.LoginParams, TokenEntity> {

    @NonNull
    private final TokenRepository repository;


    @Inject
    public LoginUser(@NonNull final TokenRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Single<TokenEntity> getSingle(@NonNull LoginParams loginParams) {
        //return repository.requestTokenSingle()
                //.flatMap(tokenEntity -> fetchWhenEmptyAndThenToken(loginParams, tokenEntity));
        return repository.fetchTokenViaLogin(loginParams.username, loginParams.password)
                .andThen(repository.requestTokenSingle());
    }

    /*
    @NonNull
    private Single<TokenEntity> fetchWhenEmptyAndThenToken(@NonNull LoginParams loginParams,
                                                           @NonNull final TokenEntity tokenEntity) {
        return fetchWhenEmpty(loginParams, tokenEntity).andThen(Single.just(tokenEntity));
    }

    @NonNull
    private Completable fetchWhenEmpty(@NonNull LoginParams loginParams,
                                       @NonNull final TokenEntity tokenEntity) {
        return tokenEntity.isEmpty()
                ? repository.fetchTokenViaLogin(loginParams.username, loginParams.email, loginParams.password)
                : Completable.complete();
    }
    */

    public static final class LoginParams {
        @NonNull private final String username;
        @NonNull private final String password;

        public LoginParams(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
