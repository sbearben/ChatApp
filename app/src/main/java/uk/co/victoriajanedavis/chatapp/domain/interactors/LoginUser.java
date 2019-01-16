package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor;

public class LoginUser implements SendInteractor<LoginUser.LoginParams, TokenEntity> {

    @NonNull private final TokenRepository backendTokenRepo;


    @Inject
    public LoginUser(@NonNull final TokenRepository backendTokenRepo) {
        this.backendTokenRepo = backendTokenRepo;
    }

    @NonNull
    @Override
    public Single<TokenEntity> getSingle(@NonNull LoginParams loginParams) {
        return backendTokenRepo.fetchTokenViaLogin(loginParams.username, loginParams.password)
                .andThen(backendTokenRepo.requestTokenSingle());
    }

    public static final class LoginParams {
        @NonNull private final String username;
        @NonNull private final String password;

        public LoginParams(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
